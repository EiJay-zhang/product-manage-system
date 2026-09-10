package com.ruoyi.pms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.pms.domain.PmsSetting;
import com.ruoyi.pms.mapper.PmsSettingMapper;
import com.ruoyi.pms.service.IPmsSettingService;

@Service
public class PmsSettingServiceImpl implements IPmsSettingService
{
    @Autowired
    private PmsSettingMapper settingMapper;

    @Override
    public PmsSetting getSetting()
    {
        return settingMapper.selectSetting();
    }

    @Override
    public int saveSetting(PmsSetting setting)
    {
        return settingMapper.updateSetting(setting);
    }
}
