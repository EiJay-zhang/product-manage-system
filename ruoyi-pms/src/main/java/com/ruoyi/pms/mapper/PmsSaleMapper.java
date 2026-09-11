package com.ruoyi.pms.mapper;

import java.util.List;
import com.ruoyi.pms.domain.PmsSale;
import com.ruoyi.pms.domain.vo.PmsChartPoint;
import com.ruoyi.pms.domain.vo.PmsReportQuery;
import com.ruoyi.pms.domain.vo.PmsTurnoverVo;

public interface PmsSaleMapper
{
    public PmsSale selectSaleById(Long saleId);
    public List<PmsSale> selectSaleList(PmsSale sale);
    public String selectMaxSaleNo(String prefix);
    public int insertSale(PmsSale sale);
    public int updateSaleStatus(PmsSale sale);
    public PmsTurnoverVo selectTurnoverSummary(PmsReportQuery query);
    public List<PmsChartPoint> selectTurnoverTrend(PmsReportQuery query);
    public List<PmsChartPoint> selectTurnoverRank(PmsReportQuery query);
}
