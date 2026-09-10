package com.ruoyi.pms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.pms.service.IPmsReportService;

@Tag(name = "经营看板")
@RestController
@RequestMapping("/pms/dashboard")
public class PmsDashboardController extends BaseController
{
    @Autowired
    private IPmsReportService reportService;

    @PreAuthorize("@ss.hasPermi('pms:dashboard:query')")
    @GetMapping
    @Operation(summary = "首页汇总")
    public AjaxResult get()
    {
        return success(reportService.dashboard());
    }
}
