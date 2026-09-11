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
import com.ruoyi.pms.mapper.PmsCarrierMapper;
import com.ruoyi.pms.mapper.PmsLogisticsMapper;

@ExtendWith(MockitoExtension.class)
class PmsCarrierServiceImplTest
{
    @Mock
    private PmsCarrierMapper carrierMapper;
    @Mock
    private PmsLogisticsMapper logisticsMapper;
    @InjectMocks
    private PmsCarrierServiceImpl carrierService;

    @Test
    void deleteCarrierByIds_hasLogistics_rejected()
    {
        when(carrierMapper.countLogisticsByCarrierId(Long.valueOf(9))).thenReturn(2);

        ServiceException ex = assertThrows(ServiceException.class,
            () -> carrierService.deleteCarrierByIds(new Long[] { Long.valueOf(9) }));
        assertTrue(ex.getMessage().contains("无法删除"));
        verify(carrierMapper, never()).deleteCarrierByIds(any());
    }
}
