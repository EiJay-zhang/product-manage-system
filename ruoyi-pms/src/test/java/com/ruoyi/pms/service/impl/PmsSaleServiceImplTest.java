package com.ruoyi.pms.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.pms.domain.PmsProduct;
import com.ruoyi.pms.domain.PmsSale;
import com.ruoyi.pms.mapper.PmsProductMapper;
import com.ruoyi.pms.mapper.PmsSaleMapper;
import com.ruoyi.pms.service.IPmsInventoryService;

@ExtendWith(MockitoExtension.class)
class PmsSaleServiceImplTest
{
    @Mock
    private PmsSaleMapper saleMapper;
    @Mock
    private PmsProductMapper productMapper;
    @Mock
    private IPmsInventoryService inventoryService;
    @InjectMocks
    private PmsSaleServiceImpl saleService;

    @Test
    void insertSale_oversell_rejected()
    {
        PmsProduct product = new PmsProduct();
        product.setProductId(Long.valueOf(1));
        product.setStockQty(Integer.valueOf(2));
        product.setDelFlag("0");
        when(productMapper.selectProductById(Long.valueOf(1))).thenReturn(product);

        PmsSale sale = new PmsSale();
        sale.setProductId(Long.valueOf(1));
        sale.setQty(Integer.valueOf(3));

        ServiceException ex = assertThrows(ServiceException.class, () -> saleService.insertSale(sale));
        assertTrue(ex.getMessage().contains("库存不足"));
        verify(saleMapper, never()).insertSale(any());
        verify(inventoryService, never()).changeStock(any(), org.mockito.ArgumentMatchers.anyInt(), any(), any(), any(), any(), any());
    }
}
