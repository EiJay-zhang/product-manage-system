package com.ruoyi.pms.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ruoyi.pms.domain.PmsSetting;
import com.ruoyi.pms.domain.vo.PmsDashboardVo;
import com.ruoyi.pms.domain.vo.PmsReportQuery;
import com.ruoyi.pms.mapper.PmsPurchaseMapper;
import com.ruoyi.pms.mapper.PmsReportMapper;
import com.ruoyi.pms.mapper.PmsSaleMapper;
import com.ruoyi.pms.mapper.PmsSettingMapper;
import com.ruoyi.pms.mapper.PmsStockLogMapper;

@ExtendWith(MockitoExtension.class)
class PmsReportServiceImplTest
{
    @Mock
    private PmsReportMapper reportMapper;
    @Mock
    private PmsPurchaseMapper purchaseMapper;
    @Mock
    private PmsStockLogMapper stockLogMapper;
    @Mock
    private PmsSaleMapper saleMapper;
    @Mock
    private PmsSettingMapper settingMapper;
    @InjectMocks
    private PmsReportServiceImpl reportService;

    @Test
    void dashboard_passesSettingWarnThreshold()
    {
        PmsSetting setting = new PmsSetting();
        setting.setStockWarnThreshold(Integer.valueOf(8));
        when(settingMapper.selectSetting()).thenReturn(setting);

        PmsDashboardVo vo = new PmsDashboardVo();
        vo.setWarnCount(Integer.valueOf(3));
        when(reportMapper.selectDashboard(any())).thenReturn(vo);

        PmsDashboardVo result = reportService.dashboard();
        assertEquals(Integer.valueOf(3), result.getWarnCount());

        ArgumentCaptor<PmsReportQuery> captor = ArgumentCaptor.forClass(PmsReportQuery.class);
        org.mockito.Mockito.verify(reportMapper).selectDashboard(captor.capture());
        assertEquals(Integer.valueOf(8), captor.getValue().getWarnThreshold());
    }
}
