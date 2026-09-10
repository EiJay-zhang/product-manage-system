package com.ruoyi.pms.mapper;

import java.util.List;
import com.ruoyi.pms.domain.vo.PmsCostReportVo;
import com.ruoyi.pms.domain.vo.PmsDashboardVo;
import com.ruoyi.pms.domain.vo.PmsReportOverviewVo;
import com.ruoyi.pms.domain.vo.PmsReportQuery;
import com.ruoyi.pms.domain.vo.PmsSupplierReportVo;

public interface PmsReportMapper
{
    public PmsReportOverviewVo selectOverview(PmsReportQuery query);
    public List<PmsCostReportVo> selectCostReport(PmsReportQuery query);
    public List<PmsSupplierReportVo> selectSupplierReport(PmsReportQuery query);
    public PmsDashboardVo selectDashboard(PmsReportQuery query);
}
