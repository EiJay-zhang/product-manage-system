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
import com.ruoyi.pms.domain.PmsDevice;
import com.ruoyi.pms.domain.PmsDeviceSyncLog;
import com.ruoyi.pms.domain.PmsSetting;
import com.ruoyi.pms.service.IPmsDeviceService;

@Tag(name = "墨水屏管理")
@RestController
@RequestMapping("/pms/device")
public class PmsDeviceController extends BaseController
{
    @Autowired
    private IPmsDeviceService deviceService;

    @PreAuthorize("@ss.hasPermi('pms:device:list')")
    @GetMapping("/list")
    @Operation(summary = "设备列表")
    public TableDataInfo list(PmsDevice device)
    {
        startPage();
        return getDataTable(deviceService.selectDeviceList(device));
    }

    @PreAuthorize("@ss.hasPermi('pms:device:query')")
    @GetMapping("/template")
    @Operation(summary = "展示模板")
    public AjaxResult template()
    {
        return success(deviceService.getTemplate());
    }

    @PreAuthorize("@ss.hasPermi('pms:device:edit')")
    @Log(title = "墨水屏模板", businessType = BusinessType.UPDATE)
    @PutMapping("/template")
    @Operation(summary = "保存展示模板")
    public AjaxResult saveTemplate(@RequestBody PmsSetting setting)
    {
        setting.setUpdateBy(getUsername());
        return toAjax(deviceService.saveTemplate(setting));
    }

    @PreAuthorize("@ss.hasPermi('pms:device:log')")
    @GetMapping("/syncLog/list")
    @Operation(summary = "同步日志")
    public TableDataInfo syncLog(PmsDeviceSyncLog log)
    {
        startPage();
        return getDataTable(deviceService.selectSyncLogList(log));
    }

    @Log(title = "同步日志", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('pms:device:export')")
    @PostMapping("/syncLog/export")
    @Operation(summary = "导出同步日志")
    public void exportLog(HttpServletResponse response, PmsDeviceSyncLog log)
    {
        List<PmsDeviceSyncLog> list = deviceService.selectSyncLogList(log);
        new ExcelUtil<PmsDeviceSyncLog>(PmsDeviceSyncLog.class).exportExcel(response, list, "同步日志");
    }

    @PreAuthorize("@ss.hasPermi('pms:device:query')")
    @GetMapping("/{deviceId}")
    @Operation(summary = "设备详情")
    public AjaxResult getInfo(@PathVariable Long deviceId)
    {
        return success(deviceService.selectDeviceById(deviceId));
    }

    @PreAuthorize("@ss.hasPermi('pms:device:bind')")
    @Log(title = "墨水屏绑定", businessType = BusinessType.INSERT)
    @PostMapping("/bind")
    @Operation(summary = "绑定设备")
    public AjaxResult bind(@Validated @RequestBody PmsDevice device)
    {
        device.setCreateBy(getUsername());
        return toAjax(deviceService.bind(device));
    }

    @PreAuthorize("@ss.hasPermi('pms:device:unbind')")
    @Log(title = "墨水屏解绑", businessType = BusinessType.UPDATE)
    @PutMapping("/unbind/{deviceId}")
    @Operation(summary = "解绑设备")
    public AjaxResult unbind(@PathVariable Long deviceId)
    {
        return toAjax(deviceService.unbind(deviceId, getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('pms:device:operate')")
    @Log(title = "墨水屏刷新", businessType = BusinessType.UPDATE)
    @PutMapping("/refresh/{deviceId}")
    @Operation(summary = "远程刷新")
    public AjaxResult refresh(@PathVariable Long deviceId)
    {
        return toAjax(deviceService.refresh(deviceId, getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('pms:device:operate')")
    @Log(title = "墨水屏重启", businessType = BusinessType.UPDATE)
    @PutMapping("/restart/{deviceId}")
    @Operation(summary = "远程重启")
    public AjaxResult restart(@PathVariable Long deviceId)
    {
        return toAjax(deviceService.restart(deviceId, getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('pms:device:operate')")
    @Log(title = "墨水屏标记", businessType = BusinessType.UPDATE)
    @PutMapping("/mark/{deviceId}")
    @Operation(summary = "标记异常")
    public AjaxResult mark(@PathVariable Long deviceId, @RequestBody PmsDevice device)
    {
        device.setDeviceId(deviceId);
        device.setUpdateBy(getUsername());
        return toAjax(deviceService.mark(device));
    }

    @PreAuthorize("@ss.hasPermi('pms:device:operate')")
    @Log(title = "墨水屏批量刷新", businessType = BusinessType.UPDATE)
    @PostMapping("/batchRefresh")
    @Operation(summary = "批量刷新")
    public AjaxResult batchRefresh(@RequestBody PmsDevice device)
    {
        return success(deviceService.batchRefresh(device, getUsername()));
    }
}
