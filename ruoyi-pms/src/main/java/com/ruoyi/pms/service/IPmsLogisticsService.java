package com.ruoyi.pms.service;

import java.util.List;
import com.ruoyi.pms.domain.PmsLogistics;
import com.ruoyi.pms.domain.vo.PmsLogisticsStatsVo;
import com.ruoyi.pms.domain.vo.PmsReportQuery;

public interface IPmsLogisticsService
{
    public PmsLogistics selectLogisticsById(Long logisticsId);
    public List<PmsLogistics> selectLogisticsList(PmsLogistics logistics);
    public int insertLogistics(PmsLogistics logistics);
    public int updateLogistics(PmsLogistics logistics);
    public int updateStatus(PmsLogistics logistics);
    public int mark(PmsLogistics logistics);
    public int batchReconcile(PmsLogistics logistics);
    public PmsLogisticsStatsVo monthly(PmsReportQuery query);
    public List<PmsLogisticsStatsVo> carrier(PmsReportQuery query);
    public List<PmsLogisticsStatsVo> supplier(PmsReportQuery query);
}
