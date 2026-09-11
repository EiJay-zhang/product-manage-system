package com.ruoyi.pms.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ruoyi.pms.constant.PmsConstants;
import com.ruoyi.pms.domain.PmsProduct;
import com.ruoyi.pms.domain.PmsPurchase;
import com.ruoyi.pms.domain.PmsSupplier;
import com.ruoyi.pms.mapper.PmsProductMapper;
import com.ruoyi.pms.mapper.PmsPurchaseMapper;
import com.ruoyi.pms.mapper.PmsSupplierMapper;
import com.ruoyi.pms.service.IPmsInventoryService;

@ExtendWith(MockitoExtension.class)
class PmsPurchaseServiceImplTest
{
    @Mock
    private PmsPurchaseMapper purchaseMapper;
    @Mock
    private PmsProductMapper productMapper;
    @Mock
    private PmsSupplierMapper supplierMapper;
    @Mock
    private IPmsInventoryService inventoryService;
    @InjectMocks
    private PmsPurchaseServiceImpl purchaseService;

    @Test
    void insertPurchase_increasesStock()
    {
        PmsProduct product = new PmsProduct();
        product.setProductId(Long.valueOf(1));
        product.setProductName("测试商品");
        product.setSpec("500g");
        product.setSupplierId(Long.valueOf(2));
        product.setStockQty(Integer.valueOf(10));
        product.setDelFlag("0");
        when(productMapper.selectProductById(Long.valueOf(1))).thenReturn(product);

        PmsSupplier supplier = new PmsSupplier();
        supplier.setSupplierId(Long.valueOf(2));
        supplier.setSupplierName("测试厂家");
        when(supplierMapper.selectSupplierById(Long.valueOf(2))).thenReturn(supplier);
        when(purchaseMapper.selectMaxPurchaseNo(any())).thenReturn(null);
        when(purchaseMapper.insertPurchase(any())).thenReturn(1);

        PmsPurchase purchase = new PmsPurchase();
        purchase.setProductId(Long.valueOf(1));
        purchase.setQty(Integer.valueOf(5));
        purchase.setPurchasePrice(new BigDecimal("8.50"));
        purchase.setCreateBy("admin");

        purchaseService.insertPurchase(purchase);

        verify(inventoryService).changeStock(any(PmsProduct.class), eq(15), eq(PmsConstants.STOCK_IN),
            eq("purchase"), any(), any(), eq("admin"));
    }
}
