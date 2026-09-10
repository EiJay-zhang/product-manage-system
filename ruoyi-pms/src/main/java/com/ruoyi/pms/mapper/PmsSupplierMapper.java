package com.ruoyi.pms.mapper;

import java.util.List;
import com.ruoyi.pms.domain.PmsSupplier;

public interface PmsSupplierMapper
{
    public PmsSupplier selectSupplierById(Long supplierId);
    public List<PmsSupplier> selectSupplierList(PmsSupplier supplier);
    public PmsSupplier checkSupplierNameUnique(String supplierName);
    public int insertSupplier(PmsSupplier supplier);
    public int updateSupplier(PmsSupplier supplier);
    public int deleteSupplierByIds(Long[] supplierIds);
    public int countProductBySupplierId(Long supplierId);
    public int countPurchaseBySupplierId(Long supplierId);
}
