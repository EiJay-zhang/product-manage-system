package com.ruoyi.pms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.pms.domain.PmsSetting;
import com.ruoyi.pms.service.IPmsSettingService;

@Tag(name = "业务设置")
@RestController
@RequestMapping("/pms/setting")
public class PmsSettingController extends BaseController
{
    @Autowired
    private IPmsSettingService settingService;

    @PreAuthorize("@ss.hasPermi('pms:setting:query')")
    @GetMapping
    @Operation(summary = "查询业务配置")
    public AjaxResult get()
    {
        return success(settingService.getSetting());
    }

    @PreAuthorize("@ss.hasPermi('pms:setting:edit')")
    @Log(title = "业务设置", businessType = BusinessType.UPDATE)
    @PutMapping
    @Operation(summary = "保存业务配置")
    public AjaxResult save(@RequestBody PmsSetting setting)
    {
        setting.setUpdateBy(getUsername());
        return toAjax(settingService.saveSetting(setting));
    }
}
