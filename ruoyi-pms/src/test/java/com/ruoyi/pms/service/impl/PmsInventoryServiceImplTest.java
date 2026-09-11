package com.ruoyi.pms.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.pms.constant.PmsConstants;
import com.ruoyi.pms.domain.PmsProduct;
import com.ruoyi.pms.mapper.PmsProductMapper;
import com.ruoyi.pms.mapper.PmsStockLogMapper;
import com.ruoyi.pms.service.IPmsDeviceService;

@ExtendWith(MockitoExtension.class)
class PmsInventoryServiceImplTest
{
    @Mock
    private PmsProductMapper productMapper;
    @Mock
    private PmsStockLogMapper stockLogMapper;
    @Mock
    private IPmsDeviceService deviceService;
    @InjectMocks
    private PmsInventoryServiceImpl inventoryService;

    @Test
    void changeStock_negativeQty_rejected()
    {
        PmsProduct product = new PmsProduct();
        product.setProductId(Long.valueOf(1));
        product.setStockQty(Integer.valueOf(1));

        ServiceException ex = assertThrows(ServiceException.class,
            () -> inventoryService.changeStock(product, -1, PmsConstants.STOCK_LOSS, "adjust", Long.valueOf(1), "损耗", "admin"));
        assertTrue(ex.getMessage().contains("库存数量不可为负数"));
        verify(productMapper, never()).updateProductStock(any());
        verify(stockLogMapper, never()).insertStockLog(any());
    }
}
