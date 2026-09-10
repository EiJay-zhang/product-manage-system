package com.ruoyi.pms.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.pms.domain.PmsLogistics;
import com.ruoyi.pms.domain.vo.PmsReportQuery;
import com.ruoyi.pms.service.IPmsLogisticsService;

@Tag(name = "物流对账")
@RestController
@RequestMapping("/pms/logistics")
public class PmsLogisticsController extends BaseController
{
    @Autowired
    private IPmsLogisticsService logisticsService;

    @PreAuthorize("@ss.hasPermi('pms:logistics:list')")
    @GetMapping("/list")
    @Operation(summary = "物流账单列表")
    public TableDataInfo list(PmsLogistics logistics)
    {
        startPage();
        return getDataTable(logisticsService.selectLogisticsList(logistics));
    }

    @Log(title = "物流对账", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('pms:logistics:export')")
    @PostMapping("/export")
    @Operation(summary = "导出物流对账")
    public void export(HttpServletResponse response, PmsLogistics logistics)
    {
        List<PmsLogistics> list = logisticsService.selectLogisticsList(logistics);
        new ExcelUtil<PmsLogistics>(PmsLogistics.class).exportExcel(response, list, "物流对账");
    }

    @PreAuthorize("@ss.hasPermi('pms:logistics:query')")
    @GetMapping("/stats/monthly")
    @Operation(summary = "月度物流费用")
    public AjaxResult monthly(PmsReportQuery query)
    {
        return success(logisticsService.monthly(query));
    }

    @PreAuthorize("@ss.hasPermi('pms:logistics:query')")
    @GetMapping("/stats/carrier")
    @Operation(summary = "服务商对账统计")
    public AjaxResult carrier(PmsReportQuery query)
    {
        return success(logisticsService.carrier(query));
    }

    @PreAuthorize("@ss.hasPermi('pms:logistics:query')")
    @GetMapping("/stats/supplier")
    @Operation(summary = "厂家物流成本")
    public AjaxResult supplier(PmsReportQuery query)
    {
        return success(logisticsService.supplier(query));
    }

    @PreAuthorize("@ss.hasPermi('pms:logistics:query')")
    @GetMapping("/{logisticsId}")
    @Operation(summary = "物流账单详情")
    public AjaxResult getInfo(@PathVariable Long logisticsId)
    {
        return success(logisticsService.selectLogisticsById(logisticsId));
    }

    @PreAuthorize("@ss.hasPermi('pms:logistics:add')")
    @Log(title = "物流对账", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "录入物流单")
    public AjaxResult add(@Validated @RequestBody PmsLogistics logistics)
    {
        logistics.setCreateBy(getUsername());
        return toAjax(logisticsService.insertLogistics(logistics));
    }

    @PreAuthorize("@ss.hasPermi('pms:logistics:edit')")
    @Log(title = "物流对账", businessType = BusinessType.UPDATE)
    @PutMapping
    @Operation(summary = "编辑物流单")
    public AjaxResult edit(@RequestBody PmsLogistics logistics)
    {
        logistics.setUpdateBy(getUsername());
        return toAjax(logisticsService.updateLogistics(logistics));
    }

    @PreAuthorize("@ss.hasPermi('pms:logistics:edit')")
    @Log(title = "物流对账", businessType = BusinessType.UPDATE)
    @PutMapping("/status")
    @Operation(summary = "批量改付款状态")
    public AjaxResult status(@RequestBody PmsLogistics logistics)
    {
        logistics.setUpdateBy(getUsername());
        return toAjax(logisticsService.updateStatus(logistics));
    }

    @PreAuthorize("@ss.hasPermi('pms:logistics:edit')")
    @Log(title = "物流对账", businessType = BusinessType.UPDATE)
    @PutMapping("/mark")
    @Operation(summary = "标记异常账单")
    public AjaxResult mark(@RequestBody PmsLogistics logistics)
    {
        logistics.setUpdateBy(getUsername());
        return toAjax(logisticsService.mark(logistics));
    }

    @PreAuthorize("@ss.hasPermi('pms:logistics:edit')")
    @Log(title = "物流对账", businessType = BusinessType.UPDATE)
    @PostMapping("/batchReconcile")
    @Operation(summary = "批量对账")
    public AjaxResult batchReconcile(@RequestBody PmsLogistics logistics)
    {
        logistics.setUpdateBy(getUsername());
        return toAjax(logisticsService.batchReconcile(logistics));
    }
}
