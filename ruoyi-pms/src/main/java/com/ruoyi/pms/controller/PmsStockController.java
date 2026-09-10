package com.ruoyi.pms.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.ruoyi.pms.domain.PmsStockLog;
import com.ruoyi.pms.service.IPmsStockLogService;

@Tag(name = "库存调整")
@RestController
@RequestMapping("/pms/stock")
public class PmsStockController extends BaseController
{
    @Autowired
    private IPmsStockLogService stockLogService;

    @PreAuthorize("@ss.hasPermi('pms:stock:adjust')")
    @Log(title = "库存调整", businessType = BusinessType.UPDATE)
    @PutMapping("/adjust")
    @Operation(summary = "库存调整")
    public AjaxResult adjust(@RequestBody PmsStockLog log)
    {
        log.setCreateBy(getUsername());
        return toAjax(stockLogService.adjust(log));
    }

    @PreAuthorize("@ss.hasPermi('pms:stock:log')")
    @GetMapping("/log/list")
    @Operation(summary = "库存变动流水")
    public TableDataInfo logList(PmsStockLog log)
    {
        startPage();
        return getDataTable(stockLogService.selectStockLogList(log));
    }

    @Log(title = "库存流水", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('pms:stock:export')")
    @PostMapping("/log/export")
    @Operation(summary = "导出库存流水")
    public void export(HttpServletResponse response, PmsStockLog log)
    {
        List<PmsStockLog> list = stockLogService.selectStockLogList(log);
        new ExcelUtil<PmsStockLog>(PmsStockLog.class).exportExcel(response, list, "库存流水");
    }
}
