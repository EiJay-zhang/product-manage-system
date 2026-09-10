package com.ruoyi.pms.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.pms.constant.PmsConstants;
import com.ruoyi.pms.domain.PmsProduct;
import com.ruoyi.pms.domain.PmsPurchase;
import com.ruoyi.pms.domain.PmsSetting;
import com.ruoyi.pms.domain.PmsSupplier;
import com.ruoyi.pms.mapper.PmsDeviceMapper;
import com.ruoyi.pms.mapper.PmsProductMapper;
import com.ruoyi.pms.mapper.PmsPurchaseMapper;
import com.ruoyi.pms.mapper.PmsSettingMapper;
import com.ruoyi.pms.mapper.PmsSupplierMapper;
import com.ruoyi.pms.service.IPmsDeviceService;
import com.ruoyi.pms.service.IPmsInventoryService;
import com.ruoyi.pms.service.IPmsProductService;
import com.ruoyi.pms.util.PmsNoUtils;

@Service
public class PmsProductServiceImpl implements IPmsProductService
{
    @Autowired
    private PmsProductMapper productMapper;
    @Autowired
    private PmsSupplierMapper supplierMapper;
    @Autowired
    private PmsPurchaseMapper purchaseMapper;
    @Autowired
    private PmsDeviceMapper deviceMapper;
    @Autowired
    private PmsSettingMapper settingMapper;
    @Autowired
    private IPmsInventoryService inventoryService;
    @Autowired
    @Lazy
    private IPmsDeviceService deviceService;

    @Override
    public PmsProduct selectProductById(Long productId)
    {
        return productMapper.selectProductById(productId);
    }

    @Override
    public List<PmsProduct> selectProductList(PmsProduct product)
    {
        fillWarn(product);
        return productMapper.selectProductList(product);
    }

    private void fillWarn(PmsProduct product)
    {
        if (product.getWarnThreshold() == null)
        {
            PmsSetting setting = settingMapper.selectSetting();
            if (setting != null && setting.getStockWarnThreshold() != null)
            {
                product.setWarnThreshold(setting.getStockWarnThreshold());
            }
        }
    }

    private void validate(PmsProduct product)
    {
        if (product.getPurchasePrice() == null || product.getPurchasePrice().compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new ServiceException("进价必须为正数");
        }
        if (product.getSalePrice() == null || product.getSalePrice().compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new ServiceException("售价必须为正数");
        }
        if (product.getStockQty() == null || product.getStockQty() < 0)
        {
            throw new ServiceException("库存数量不可为负数");
        }
        PmsSupplier supplier = supplierMapper.selectSupplierById(product.getSupplierId());
        if (supplier == null || PmsConstants.DEL_REMOVED.equals(supplier.getDelFlag()))
        {
            throw new ServiceException("进货厂家不存在");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertProduct(PmsProduct product)
    {
        validate(product);
        String prefix = "P" + DateUtils.parseDateToStr("yyyyMMdd", new Date());
        product.setProductCode(PmsNoUtils.next("P", productMapper.selectMaxProductCode(prefix)));
        int rows = productMapper.insertProduct(product);
        if (product.getStockQty() != null && product.getStockQty() > 0)
        {
            createFirstPurchase(product);
        }
        return rows;
    }

    private void createFirstPurchase(PmsProduct product)
    {
        PmsSupplier supplier = supplierMapper.selectSupplierById(product.getSupplierId());
        PmsPurchase purchase = new PmsPurchase();
        String prefix = "IN" + DateUtils.parseDateToStr("yyyyMMdd", new Date());
        purchase.setPurchaseNo(PmsNoUtils.next("IN", purchaseMapper.selectMaxPurchaseNo(prefix)));
        purchase.setProductId(product.getProductId());
        purchase.setProductName(product.getProductName());
        purchase.setSpec(product.getSpec());
        purchase.setSupplierId(product.getSupplierId());
        purchase.setSupplierName(supplier == null ? "" : supplier.getSupplierName());
        purchase.setQty(product.getStockQty());
        purchase.setPurchasePrice(product.getPurchasePrice());
        purchase.setAmount(product.getPurchasePrice().multiply(new BigDecimal(product.getStockQty())));
        purchase.setPurchaseTime(product.getPurchaseTime() == null ? new Date() : product.getPurchaseTime());
        purchase.setCreateBy(product.getCreateBy());
        purchase.setRemark("商品建档入库");
        purchaseMapper.insertPurchase(purchase);

        PmsProduct snap = productMapper.selectProductById(product.getProductId());
        snap.setStockQty(0);
        inventoryService.changeStock(snap, product.getStockQty(), PmsConstants.STOCK_IN, "purchase", purchase.getPurchaseId(), "商品建档入库", product.getCreateBy());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateProduct(PmsProduct product)
    {
        PmsProduct old = productMapper.selectProductById(product.getProductId());
        if (old == null)
        {
            throw new ServiceException("商品不存在");
        }
        validate(product);
        int rows = productMapper.updateProduct(product);
        if (product.getStockQty() != null && !product.getStockQty().equals(old.getStockQty()))
        {
            inventoryService.changeStock(old, product.getStockQty(), PmsConstants.STOCK_CHECK, "product", product.getProductId(), "编辑商品调整库存", product.getUpdateBy());
        }
        else
        {
            boolean displayChanged = !StringUtils.equals(old.getProductName(), product.getProductName())
                || !StringUtils.equals(old.getSpec(), product.getSpec())
                || (old.getSalePrice() != null && product.getSalePrice() != null && old.getSalePrice().compareTo(product.getSalePrice()) != 0)
                || !StringUtils.equals(old.getIntro(), product.getIntro());
            if (displayChanged)
            {
                deviceService.refreshByProductId(product.getProductId(), "refresh");
            }
        }
        return rows;
    }

    @Override
    public int deleteProductByIds(Long[] productIds)
    {
        for (Long id : productIds)
        {
            if (deviceMapper.countByProductId(id) > 0)
            {
                throw new ServiceException("商品已绑定墨水屏，请先解绑设备");
            }
        }
        return productMapper.deleteProductByIds(productIds);
    }
}
