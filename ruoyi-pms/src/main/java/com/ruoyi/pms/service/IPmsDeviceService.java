package com.ruoyi.pms.service;

import java.util.List;
import com.ruoyi.pms.domain.PmsDevice;
import com.ruoyi.pms.domain.PmsDeviceSyncLog;
import com.ruoyi.pms.domain.PmsSetting;
import com.ruoyi.pms.domain.vo.PmsEinkContentVo;

public interface IPmsDeviceService
{
    public PmsDevice selectDeviceById(Long deviceId);
    public List<PmsDevice> selectDeviceList(PmsDevice device);
    public int bind(PmsDevice device);
    public int unbind(Long deviceId, String operator);
    public int refresh(Long deviceId, String operator);
    public int restart(Long deviceId, String operator);
    public int mark(PmsDevice device);
    public int batchRefresh(PmsDevice device, String operator);
    public void refreshByProductId(Long productId, String command);
    public List<PmsDeviceSyncLog> selectSyncLogList(PmsDeviceSyncLog log);
    public PmsSetting getTemplate();
    public int saveTemplate(PmsSetting setting);
    public PmsDevice heartbeat(PmsDevice device);
    public PmsEinkContentVo content(String sn);
    public int ack(PmsDeviceSyncLog log);
}
