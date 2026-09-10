package com.ruoyi.pms.controller;

import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.pms.domain.PmsPurchase;
import com.ruoyi.pms.domain.PmsSale;
import com.ruoyi.pms.domain.PmsStockLog;
import com.ruoyi.pms.domain.vo.PmsCostReportVo;
import com.ruoyi.pms.domain.vo.PmsReportOverviewVo;
import com.ruoyi.pms.domain.vo.PmsReportQuery;
import com.ruoyi.pms.domain.vo.PmsSupplierReportVo;
import com.ruoyi.pms.service.IPmsReportService;

@Tag(name = "报表统计")
@RestController
@RequestMapping("/pms/report")
public class PmsReportController extends BaseController
{
    @Autowired
    private IPmsReportService reportService;

    @PreAuthorize("@ss.hasPermi('pms:report:overview')")
    @GetMapping("/overview")
    @Operation(summary = "数据总览")
    public AjaxResult overview(PmsReportQuery query)
    {
        return success(reportService.overview(query));
    }

    @PreAuthorize("@ss.hasPermi('pms:report:purchase')")
    @GetMapping("/purchase")
    @Operation(summary = "进货明细报表")
    public AjaxResult purchase(PmsReportQuery query)
    {
        return success(reportService.purchaseReportData(query));
    }

    @PreAuthorize("@ss.hasPermi('pms:report:stock')")
    @GetMapping("/stock")
    @Operation(summary = "库存台账报表")
    public AjaxResult stock(PmsReportQuery query)
    {
        return success(reportService.stockReportData(query));
    }

    @PreAuthorize("@ss.hasPermi('pms:report:cost')")
    @GetMapping("/cost")
    @Operation(summary = "成本统计报表")
    public AjaxResult cost(PmsReportQuery query)
    {
        return success(reportService.costReport(query));
    }

    @PreAuthorize("@ss.hasPermi('pms:report:supplier')")
    @GetMapping("/supplier")
    @Operation(summary = "供应商统计报表")
    public AjaxResult supplier(PmsReportQuery query)
    {
        return success(reportService.supplierReport(query));
    }

    @PreAuthorize("@ss.hasPermi('pms:report:turnover')")
    @GetMapping("/turnover")
    @Operation(summary = "营业额汇总")
    public AjaxResult turnover(PmsReportQuery query)
    {
        return success(reportService.turnover(query));
    }

    @PreAuthorize("@ss.hasPermi('pms:report:turnover')")
    @GetMapping("/turnover/trend")
    @Operation(summary = "营收趋势")
    public AjaxResult trend(PmsReportQuery query)
    {
        return success(reportService.turnoverTrend(query));
    }

    @PreAuthorize("@ss.hasPermi('pms:report:turnover')")
    @GetMapping("/turnover/rank")
    @Operation(summary = "商品营收排行")
    public AjaxResult rank(PmsReportQuery query)
    {
        return success(reportService.turnoverRank(query));
    }

    @PreAuthorize("@ss.hasPermi('pms:report:turnover')")
    @GetMapping("/turnover/compare")
    @Operation(summary = "营收对比")
    public AjaxResult compare(PmsReportQuery query)
    {
        return success(reportService.turnoverCompare(query));
    }

    @PreAuthorize("@ss.hasPermi('pms:report:turnover')")
    @GetMapping("/turnover/detail")
    @Operation(summary = "营业额明细下钻")
    public AjaxResult detail(PmsReportQuery query)
    {
        return success(reportService.turnoverDetail(query));
    }

    @Log(title = "报表导出", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('pms:report:export')")
    @PostMapping("/export/{reportType}")
    @Operation(summary = "导出报表")
    public void export(HttpServletResponse response, @PathVariable String reportType, PmsReportQuery query)
    {
        switch (reportType)
        {
            case "overview":
                ArrayList<PmsReportOverviewVo> overviews = new ArrayList<PmsReportOverviewVo>();
                overviews.add(reportService.overview(query));
                new ExcelUtil<PmsReportOverviewVo>(PmsReportOverviewVo.class).exportExcel(response, overviews, "数据总览报表");
                break;
            case "purchase":
                List<PmsPurchase> purchases = reportService.purchaseReport(query);
                new ExcelUtil<PmsPurchase>(PmsPurchase.class).exportExcel(response, purchases, "进货明细报表");
                break;
            case "stock":
                List<PmsStockLog> stocks = reportService.stockReport(query);
                new ExcelUtil<PmsStockLog>(PmsStockLog.class).exportExcel(response, stocks, "库存台账报表");
                break;
            case "cost":
                List<PmsCostReportVo> costs = reportService.costReport(query);
                new ExcelUtil<PmsCostReportVo>(PmsCostReportVo.class).exportExcel(response, costs, "成本统计报表");
                break;
            case "supplier":
                List<PmsSupplierReportVo> suppliers = reportService.supplierReport(query);
                new ExcelUtil<PmsSupplierReportVo>(PmsSupplierReportVo.class).exportExcel(response, suppliers, "供应商统计报表");
                break;
            case "turnover":
                List<PmsSale> sales = reportService.turnoverDetail(query);
                new ExcelUtil<PmsSale>(PmsSale.class).exportExcel(response, sales, "营业额明细");
                break;
            default:
                throw new ServiceException("不支持的报表类型");
        }
    }
}
