package com.ruoyi.pms.mapper;

import java.util.List;
import com.ruoyi.pms.domain.PmsDeviceSyncLog;

public interface PmsDeviceSyncLogMapper
{
    public List<PmsDeviceSyncLog> selectSyncLogList(PmsDeviceSyncLog log);
    public int insertSyncLog(PmsDeviceSyncLog log);
    public int updateSyncLogStatus(PmsDeviceSyncLog log);
}
