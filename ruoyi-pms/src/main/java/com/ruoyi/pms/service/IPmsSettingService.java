package com.ruoyi.pms.service;

import com.ruoyi.pms.domain.PmsSetting;

public interface IPmsSettingService
{
    public PmsSetting getSetting();
    public int saveSetting(PmsSetting setting);
}
