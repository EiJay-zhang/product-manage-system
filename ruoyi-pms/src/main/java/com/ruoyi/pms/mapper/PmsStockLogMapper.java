package com.ruoyi.pms.mapper;

import java.util.List;
import com.ruoyi.pms.domain.PmsStockLog;

public interface PmsStockLogMapper
{
    public List<PmsStockLog> selectStockLogList(PmsStockLog log);
    public int insertStockLog(PmsStockLog log);
}
