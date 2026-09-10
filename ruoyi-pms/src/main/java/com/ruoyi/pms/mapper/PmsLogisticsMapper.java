package com.ruoyi.pms.mapper;

import java.util.List;
import com.ruoyi.pms.domain.PmsLogistics;
import com.ruoyi.pms.domain.vo.PmsLogisticsStatsVo;
import com.ruoyi.pms.domain.vo.PmsReportQuery;

public interface PmsLogisticsMapper
{
    public PmsLogistics selectLogisticsById(Long logisticsId);
    public PmsLogistics checkLogisticsNoUnique(String logisticsNo);
    public List<PmsLogistics> selectLogisticsList(PmsLogistics logistics);
    public int insertLogistics(PmsLogistics logistics);
    public int updateLogistics(PmsLogistics logistics);
    public int updateLogisticsStatus(PmsLogistics logistics);
    public PmsLogisticsStatsVo selectMonthlyStats(PmsReportQuery query);
    public List<PmsLogisticsStatsVo> selectCarrierStats(PmsReportQuery query);
    public List<PmsLogisticsStatsVo> selectSupplierStats(PmsReportQuery query);
    public int countByPayStatus(String payStatus);
}
