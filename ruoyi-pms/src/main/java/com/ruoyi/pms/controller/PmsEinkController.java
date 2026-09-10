package com.ruoyi.pms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.pms.domain.PmsDevice;
import com.ruoyi.pms.domain.PmsDeviceSyncLog;
import com.ruoyi.pms.service.IPmsDeviceService;

@Anonymous
@Tag(name = "墨水屏设备端")
@RestController
@RequestMapping("/pms/eink")
public class PmsEinkController extends BaseController
{
    @Autowired
    private IPmsDeviceService deviceService;

    @PostMapping("/heartbeat")
    @Operation(summary = "设备心跳")
    public AjaxResult heartbeat(@RequestBody PmsDevice device)
    {
        return success(deviceService.heartbeat(device));
    }

    @GetMapping("/content/{sn}")
    @Operation(summary = "拉取展示内容")
    public AjaxResult content(@PathVariable String sn)
    {
        return success(deviceService.content(sn));
    }

    @PostMapping("/ack")
    @Operation(summary = "同步回执")
    public AjaxResult ack(@RequestBody PmsDeviceSyncLog log)
    {
        return toAjax(deviceService.ack(log));
    }
}
