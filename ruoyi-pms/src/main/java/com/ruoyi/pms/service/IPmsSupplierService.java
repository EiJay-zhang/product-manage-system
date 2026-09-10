package com.ruoyi.pms.service;

import java.util.List;
import com.ruoyi.pms.domain.PmsPurchase;
import com.ruoyi.pms.domain.PmsProduct;
import com.ruoyi.pms.domain.PmsSupplier;

public interface IPmsSupplierService
{
    public PmsSupplier selectSupplierById(Long supplierId);
    public List<PmsSupplier> selectSupplierList(PmsSupplier supplier);
    public int insertSupplier(PmsSupplier supplier);
    public int updateSupplier(PmsSupplier supplier);
    public int deleteSupplierByIds(Long[] supplierIds);
    public List<PmsProduct> selectProducts(Long supplierId);
    public List<PmsPurchase> selectPurchases(Long supplierId);
}
