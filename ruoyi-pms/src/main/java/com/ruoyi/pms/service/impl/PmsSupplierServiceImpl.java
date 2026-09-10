package com.ruoyi.pms.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.pms.domain.PmsPurchase;
import com.ruoyi.pms.domain.PmsProduct;
import com.ruoyi.pms.domain.PmsSupplier;
import com.ruoyi.pms.mapper.PmsPurchaseMapper;
import com.ruoyi.pms.mapper.PmsProductMapper;
import com.ruoyi.pms.mapper.PmsSupplierMapper;
import com.ruoyi.pms.service.IPmsSupplierService;

@Service
public class PmsSupplierServiceImpl implements IPmsSupplierService
{
    @Autowired
    private PmsSupplierMapper supplierMapper;
    @Autowired
    private PmsProductMapper productMapper;
    @Autowired
    private PmsPurchaseMapper purchaseMapper;

    @Override
    public PmsSupplier selectSupplierById(Long supplierId)
    {
        return supplierMapper.selectSupplierById(supplierId);
    }

    @Override
    public List<PmsSupplier> selectSupplierList(PmsSupplier supplier)
    {
        return supplierMapper.selectSupplierList(supplier);
    }

    private void checkNameUnique(PmsSupplier supplier)
    {
        PmsSupplier exist = supplierMapper.checkSupplierNameUnique(supplier.getSupplierName());
        if (exist != null && !exist.getSupplierId().equals(supplier.getSupplierId()))
        {
            throw new ServiceException("厂家名称已存在");
        }
    }

    @Override
    public int insertSupplier(PmsSupplier supplier)
    {
        if (StringUtils.isEmpty(supplier.getStatus()))
        {
            supplier.setStatus("0");
        }
        checkNameUnique(supplier);
        return supplierMapper.insertSupplier(supplier);
    }

    @Override
    public int updateSupplier(PmsSupplier supplier)
    {
        checkNameUnique(supplier);
        return supplierMapper.updateSupplier(supplier);
    }

    @Override
    public int deleteSupplierByIds(Long[] supplierIds)
    {
        for (Long id : supplierIds)
        {
            if (supplierMapper.countProductBySupplierId(id) > 0)
            {
                throw new ServiceException("厂家存在关联商品，无法删除");
            }
            if (supplierMapper.countPurchaseBySupplierId(id) > 0)
            {
                throw new ServiceException("厂家存在进货记录，无法删除");
            }
        }
        return supplierMapper.deleteSupplierByIds(supplierIds);
    }

    @Override
    public List<PmsProduct> selectProducts(Long supplierId)
    {
        PmsProduct q = new PmsProduct();
        q.setSupplierId(supplierId);
        return productMapper.selectProductList(q);
    }

    @Override
    public List<PmsPurchase> selectPurchases(Long supplierId)
    {
        PmsPurchase q = new PmsPurchase();
        q.setSupplierId(supplierId);
        return purchaseMapper.selectPurchaseList(q);
    }
}
