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
import com.ruoyi.pms.domain.PmsCategory;
import com.ruoyi.pms.domain.PmsProduct;
import com.ruoyi.pms.domain.PmsPurchase;
import com.ruoyi.pms.domain.PmsSetting;
import com.ruoyi.pms.domain.PmsSupplier;
import com.ruoyi.pms.mapper.PmsCategoryMapper;
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
    private PmsCategoryMapper categoryMapper;
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
        if (StringUtils.isEmpty(product.getProductCode()))
        {
            String prefix = "P" + DateUtils.parseDateToStr("yyyyMMdd", new Date());
            product.setProductCode(PmsNoUtils.next("P", productMapper.selectMaxProductCode(prefix)));
        }
        else
        {
            product.setProductCode(product.getProductCode().trim());
            if (productMapper.selectProductByCode(product.getProductCode()) != null)
            {
                throw new ServiceException("商品编号已存在：" + product.getProductCode());
            }
        }
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
        purchase.setStatus(PmsConstants.BILL_NORMAL);
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

    @Override
    public String importProduct(List<PmsProduct> products, Boolean updateSupport, String operName)
    {
        if (products == null || products.isEmpty())
        {
            throw new ServiceException("导入数据不能为空");
        }
        int success = 0;
        int failure = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failMsg = new StringBuilder();
        for (int i = 0; i < products.size(); i++)
        {
            PmsProduct row = products.get(i);
            int excelRow = i + 2;
            try
            {
                if (StringUtils.isEmpty(row.getProductName()) && StringUtils.isEmpty(row.getSpec()) && StringUtils.isEmpty(row.getSupplierName()))
                {
                    continue;
                }
                if (StringUtils.isEmpty(row.getProductName()) || StringUtils.isEmpty(row.getSpec()))
                {
                    throw new ServiceException("商品名称和规格不能为空");
                }
                if (StringUtils.isEmpty(row.getSupplierName()))
                {
                    throw new ServiceException("进货厂家不能为空");
                }
                PmsSupplier supplier = supplierMapper.checkSupplierNameUnique(row.getSupplierName().trim());
                if (supplier == null || PmsConstants.DEL_REMOVED.equals(supplier.getDelFlag()))
                {
                    throw new ServiceException("进货厂家不存在：" + row.getSupplierName());
                }
                row.setSupplierId(supplier.getSupplierId());
                if (StringUtils.isNotEmpty(row.getCategoryName()))
                {
                    PmsCategory category = categoryMapper.checkCategoryNameUnique(row.getCategoryName().trim());
                    if (category == null)
                    {
                        throw new ServiceException("分类不存在：" + row.getCategoryName());
                    }
                    row.setCategoryId(category.getCategoryId());
                }
                if (row.getStockQty() == null)
                {
                    row.setStockQty(Integer.valueOf(0));
                }
                PmsProduct exist = null;
                if (StringUtils.isNotEmpty(row.getProductCode()))
                {
                    exist = productMapper.selectProductByCode(row.getProductCode().trim());
                }
                else
                {
                    exist = productMapper.selectProductByNameAndSpec(row.getProductName().trim(), row.getSpec().trim());
                }
                if (exist == null)
                {
                    row.setCreateBy(operName);
                    insertProduct(row);
                    success++;
                    successMsg.append("<br/>").append(success).append("、").append(row.getProductName()).append(" 导入成功");
                }
                else if (Boolean.TRUE.equals(updateSupport))
                {
                    row.setProductId(exist.getProductId());
                    row.setProductCode(exist.getProductCode());
                    row.setStockQty(exist.getStockQty());
                    row.setUpdateBy(operName);
                    updateProduct(row);
                    success++;
                    successMsg.append("<br/>").append(success).append("、").append(row.getProductName()).append(" 更新成功");
                }
                else
                {
                    throw new ServiceException("商品已存在：" + exist.getProductCode());
                }
            }
            catch (Exception e)
            {
                failure++;
                failMsg.append("<br/>第").append(excelRow).append("行：").append(e.getMessage());
            }
        }
        if (failure > 0)
        {
            throw new ServiceException("很抱歉，导入失败！共 " + failure + " 条数据不正确，错误如下：" + failMsg);
        }
        if (success == 0)
        {
            throw new ServiceException("导入数据不能为空");
        }
        successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + success + " 条，数据如下：");
        return successMsg.toString();
    }
}
