package com.ruoyi.pms.service;

import java.util.List;
import com.ruoyi.pms.domain.PmsStockLog;

public interface IPmsStockLogService
{
    public List<PmsStockLog> selectStockLogList(PmsStockLog log);
    public int adjust(PmsStockLog log);
}
