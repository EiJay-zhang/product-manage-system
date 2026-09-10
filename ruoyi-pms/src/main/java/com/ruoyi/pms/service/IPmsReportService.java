package com.ruoyi.pms.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.pms.domain.PmsPurchase;
import com.ruoyi.pms.domain.PmsSale;
import com.ruoyi.pms.domain.PmsStockLog;
import com.ruoyi.pms.domain.vo.PmsChartPoint;
import com.ruoyi.pms.domain.vo.PmsCostReportVo;
import com.ruoyi.pms.domain.vo.PmsDashboardVo;
import com.ruoyi.pms.domain.vo.PmsReportOverviewVo;
import com.ruoyi.pms.domain.vo.PmsReportQuery;
import com.ruoyi.pms.domain.vo.PmsSupplierReportVo;
import com.ruoyi.pms.domain.vo.PmsTurnoverVo;

public interface IPmsReportService
{
    public PmsDashboardVo dashboard();
    public PmsReportOverviewVo overview(PmsReportQuery query);
    public List<PmsPurchase> purchaseReport(PmsReportQuery query);
    public List<PmsStockLog> stockReport(PmsReportQuery query);
    public List<PmsCostReportVo> costReport(PmsReportQuery query);
    public List<PmsSupplierReportVo> supplierReport(PmsReportQuery query);
    public PmsTurnoverVo turnover(PmsReportQuery query);
    public List<PmsChartPoint> turnoverTrend(PmsReportQuery query);
    public List<PmsChartPoint> turnoverRank(PmsReportQuery query);
    public Map<String, Object> turnoverCompare(PmsReportQuery query);
    public List<PmsSale> turnoverDetail(PmsReportQuery query);
    public Map<String, Object> purchaseReportData(PmsReportQuery query);
    public Map<String, Object> stockReportData(PmsReportQuery query);
}
