package com.ruoyi.pms.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.pms.constant.PmsConstants;
import com.ruoyi.pms.domain.PmsProduct;
import com.ruoyi.pms.domain.PmsPurchase;
import com.ruoyi.pms.domain.PmsSupplier;
import com.ruoyi.pms.mapper.PmsProductMapper;
import com.ruoyi.pms.mapper.PmsPurchaseMapper;
import com.ruoyi.pms.mapper.PmsSupplierMapper;
import com.ruoyi.pms.service.IPmsInventoryService;
import com.ruoyi.pms.service.IPmsPurchaseService;
import com.ruoyi.pms.util.PmsNoUtils;

@Service
public class PmsPurchaseServiceImpl implements IPmsPurchaseService
{
    @Autowired
    private PmsPurchaseMapper purchaseMapper;
    @Autowired
    private PmsProductMapper productMapper;
    @Autowired
    private PmsSupplierMapper supplierMapper;
    @Autowired
    private IPmsInventoryService inventoryService;

    @Override
    public PmsPurchase selectPurchaseById(Long purchaseId)
    {
        return purchaseMapper.selectPurchaseById(purchaseId);
    }

    @Override
    public List<PmsPurchase> selectPurchaseList(PmsPurchase purchase)
    {
        return purchaseMapper.selectPurchaseList(purchase);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertPurchase(PmsPurchase purchase)
    {
        if (purchase.getQty() == null || purchase.getQty() <= 0)
        {
            throw new ServiceException("进货数量必须为正整数");
        }
        if (purchase.getPurchasePrice() == null || purchase.getPurchasePrice().compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new ServiceException("进价必须为正数");
        }
        PmsProduct product = productMapper.selectProductById(purchase.getProductId());
        if (product == null || PmsConstants.DEL_REMOVED.equals(product.getDelFlag()))
        {
            throw new ServiceException("商品不存在");
        }
        Long supplierId = purchase.getSupplierId() == null ? product.getSupplierId() : purchase.getSupplierId();
        PmsSupplier supplier = supplierMapper.selectSupplierById(supplierId);
        if (supplier == null)
        {
            throw new ServiceException("厂家不存在");
        }
        if (purchase.getPurchaseTime() == null)
        {
            purchase.setPurchaseTime(new Date());
        }
        String prefix = "IN" + DateUtils.parseDateToStr("yyyyMMdd", new Date());
        purchase.setPurchaseNo(PmsNoUtils.next("IN", purchaseMapper.selectMaxPurchaseNo(prefix)));
        purchase.setProductName(product.getProductName());
        purchase.setSpec(product.getSpec());
        purchase.setSupplierId(supplierId);
        purchase.setSupplierName(supplier.getSupplierName());
        purchase.setAmount(purchase.getPurchasePrice().multiply(new BigDecimal(purchase.getQty())));
        int rows = purchaseMapper.insertPurchase(purchase);

        int after = (product.getStockQty() == null ? 0 : product.getStockQty()) + purchase.getQty();
        PmsProduct snap = new PmsProduct();
        snap.setProductId(product.getProductId());
        snap.setPurchasePrice(purchase.getPurchasePrice());
        snap.setSupplierId(supplierId);
        snap.setPurchaseTime(purchase.getPurchaseTime());
        snap.setUpdateBy(purchase.getCreateBy());
        snap.setStockQty(after);
        productMapper.updateProductStock(snap);
        product.setStockQty(product.getStockQty() == null ? 0 : product.getStockQty());
        inventoryService.changeStock(product, after, PmsConstants.STOCK_IN, "purchase", purchase.getPurchaseId(), "进货入库", purchase.getCreateBy());
        return rows;
    }
}
