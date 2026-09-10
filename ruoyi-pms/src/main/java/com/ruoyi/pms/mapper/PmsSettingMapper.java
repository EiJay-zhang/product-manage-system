package com.ruoyi.pms.mapper;

import com.ruoyi.pms.domain.PmsSetting;

public interface PmsSettingMapper
{
    public PmsSetting selectSetting();
    public int updateSetting(PmsSetting setting);
}
