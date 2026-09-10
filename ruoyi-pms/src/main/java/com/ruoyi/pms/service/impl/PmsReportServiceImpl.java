package com.ruoyi.pms.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.pms.domain.PmsPurchase;
import com.ruoyi.pms.domain.PmsSale;
import com.ruoyi.pms.domain.PmsSetting;
import com.ruoyi.pms.domain.PmsStockLog;
import com.ruoyi.pms.domain.vo.PmsChartPoint;
import com.ruoyi.pms.domain.vo.PmsCostReportVo;
import com.ruoyi.pms.domain.vo.PmsDashboardVo;
import com.ruoyi.pms.domain.vo.PmsReportOverviewVo;
import com.ruoyi.pms.domain.vo.PmsReportQuery;
import com.ruoyi.pms.domain.vo.PmsSupplierReportVo;
import com.ruoyi.pms.domain.vo.PmsTurnoverVo;
import com.ruoyi.pms.mapper.PmsPurchaseMapper;
import com.ruoyi.pms.mapper.PmsReportMapper;
import com.ruoyi.pms.mapper.PmsSaleMapper;
import com.ruoyi.pms.mapper.PmsSettingMapper;
import com.ruoyi.pms.mapper.PmsStockLogMapper;
import com.ruoyi.pms.service.IPmsReportService;
import com.ruoyi.pms.util.PmsDateRange;

@Service
public class PmsReportServiceImpl implements IPmsReportService
{
    @Autowired
    private PmsReportMapper reportMapper;
    @Autowired
    private PmsPurchaseMapper purchaseMapper;
    @Autowired
    private PmsStockLogMapper stockLogMapper;
    @Autowired
    private PmsSaleMapper saleMapper;
    @Autowired
    private PmsSettingMapper settingMapper;

    private void prepare(PmsReportQuery query)
    {
        if (query == null)
        {
            query = new PmsReportQuery();
        }
        if (StringUtils.isEmpty(query.getRange()))
        {
            query.setRange("month");
        }
        PmsDateRange range = PmsDateRange.of(query.getRange(), query.getBeginTime(), query.getEndTime());
        query.setBeginTime(range.begin);
        query.setEndTime(range.end);
        if (query.getWarnThreshold() == null)
        {
            PmsSetting setting = settingMapper.selectSetting();
            if (setting != null)
            {
                query.setWarnThreshold(setting.getStockWarnThreshold());
            }
        }
    }

    @Override
    public PmsDashboardVo dashboard()
    {
        PmsReportQuery query = new PmsReportQuery();
        query.setRange("today");
        prepare(query);
        PmsDashboardVo vo = reportMapper.selectDashboard(query);
        if (vo == null)
        {
            vo = new PmsDashboardVo();
        }
        PmsSetting setting = settingMapper.selectSetting();
        if (setting != null && vo.getTodayTurnover() != null)
        {
            if (setting.getTurnoverWarnMin() != null && vo.getTodayTurnover().compareTo(setting.getTurnoverWarnMin()) < 0)
            {
                vo.setTurnoverWarn("低于营收目标");
            }
            else if (setting.getTurnoverWarnMax() != null && vo.getTodayTurnover().compareTo(setting.getTurnoverWarnMax()) > 0)
            {
                vo.setTurnoverWarn("高于营收上限");
            }
        }
        return vo;
    }

    @Override
    public PmsReportOverviewVo overview(PmsReportQuery query)
    {
        prepare(query);
        return reportMapper.selectOverview(query);
    }

    @Override
    public List<PmsPurchase> purchaseReport(PmsReportQuery query)
    {
        prepare(query);
        PmsPurchase q = new PmsPurchase();
        q.setProductName(query.getProductName());
        q.setSupplierId(query.getSupplierId());
        q.setBeginTime(query.getBeginTime());
        q.setEndTime(query.getEndTime());
        return purchaseMapper.selectPurchaseList(q);
    }

    @Override
    public List<PmsStockLog> stockReport(PmsReportQuery query)
    {
        prepare(query);
        PmsStockLog q = new PmsStockLog();
        q.setProductName(query.getProductName());
        q.setBeginTime(query.getBeginTime());
        q.setEndTime(query.getEndTime());
        return stockLogMapper.selectStockLogList(q);
    }

    @Override
    public List<PmsCostReportVo> costReport(PmsReportQuery query)
    {
        prepare(query);
        return reportMapper.selectCostReport(query);
    }

    @Override
    public List<PmsSupplierReportVo> supplierReport(PmsReportQuery query)
    {
        prepare(query);
        return reportMapper.selectSupplierReport(query);
    }

    @Override
    public PmsTurnoverVo turnover(PmsReportQuery query)
    {
        prepare(query);
        if (StringUtils.isEmpty(query.getDimension()))
        {
            query.setDimension("day");
        }
        PmsTurnoverVo vo = saleMapper.selectTurnoverSummary(query);
        if (vo == null)
        {
            vo = new PmsTurnoverVo();
            vo.setTurnover(BigDecimal.ZERO);
            vo.setQty(0);
            vo.setProductCount(0);
        }
        if (query.getBeginTime() != null && query.getEndTime() != null)
        {
            long days = ChronoUnit.DAYS.between(
                query.getBeginTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate(),
                query.getEndTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()) + 1;
            if (days < 1)
            {
                days = 1;
            }
            BigDecimal turnover = vo.getTurnover() == null ? BigDecimal.ZERO : vo.getTurnover();
            vo.setAvgDaily(turnover.divide(new BigDecimal(days), 2, RoundingMode.HALF_UP));
        }
        return vo;
    }

    @Override
    public List<PmsChartPoint> turnoverTrend(PmsReportQuery query)
    {
        prepare(query);
        if (StringUtils.isEmpty(query.getDimension()))
        {
            query.setDimension("day");
        }
        return saleMapper.selectTurnoverTrend(query);
    }

    @Override
    public List<PmsChartPoint> turnoverRank(PmsReportQuery query)
    {
        prepare(query);
        return saleMapper.selectTurnoverRank(query);
    }

    @Override
    public Map<String, Object> turnoverCompare(PmsReportQuery query)
    {
        String range = StringUtils.isEmpty(query.getRange()) ? "week" : query.getRange();
        PmsReportQuery current = new PmsReportQuery();
        current.setRange(range);
        PmsTurnoverVo now = turnover(current);

        PmsReportQuery lastQ = new PmsReportQuery();
        if ("week".equals(range))
        {
            lastQ.setRange("lastWeek");
        }
        else
        {
            lastQ.setRange("lastMonth");
        }
        PmsTurnoverVo last = turnover(lastQ);
        BigDecimal nowVal = now.getTurnover() == null ? BigDecimal.ZERO : now.getTurnover();
        BigDecimal lastVal = last.getTurnover() == null ? BigDecimal.ZERO : last.getTurnover();
        BigDecimal diff = nowVal.subtract(lastVal);
        BigDecimal rate = BigDecimal.ZERO;
        if (lastVal.compareTo(BigDecimal.ZERO) != 0)
        {
            rate = diff.multiply(new BigDecimal("100")).divide(lastVal, 2, RoundingMode.HALF_UP);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("current", now);
        map.put("previous", last);
        map.put("diff", diff);
        map.put("growthRate", rate);
        return map;
    }

    @Override
    public List<PmsSale> turnoverDetail(PmsReportQuery query)
    {
        prepare(query);
        PmsSale sale = new PmsSale();
        sale.setProductName(query.getProductName());
        sale.setCategoryId(query.getCategoryId());
        sale.setBeginTime(query.getBeginTime());
        sale.setEndTime(query.getEndTime());
        return saleMapper.selectSaleList(sale);
    }

    @Override
    public Map<String, Object> purchaseReportData(PmsReportQuery query)
    {
        List<PmsPurchase> list = purchaseReport(query);
        BigDecimal amount = BigDecimal.ZERO;
        int qty = 0;
        Map<String, BigDecimal> dayMap = new LinkedHashMap<String, BigDecimal>();
        for (PmsPurchase item : list)
        {
            if (item.getAmount() != null)
            {
                amount = amount.add(item.getAmount());
            }
            if (item.getQty() != null)
            {
                qty += item.getQty();
            }
            if (item.getPurchaseTime() != null)
            {
                String day = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, item.getPurchaseTime());
                BigDecimal cur = dayMap.get(day);
                BigDecimal add = item.getAmount() == null ? BigDecimal.ZERO : item.getAmount();
                dayMap.put(day, cur == null ? add : cur.add(add));
            }
        }
        List<PmsChartPoint> chart = new ArrayList<PmsChartPoint>();
        for (Map.Entry<String, BigDecimal> e : dayMap.entrySet())
        {
            PmsChartPoint p = new PmsChartPoint();
            p.setLabel(e.getKey());
            p.setValue(e.getValue());
            chart.add(p);
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("rows", list);
        data.put("qty", qty);
        data.put("amount", amount);
        data.put("chart", chart);
        return data;
    }

    @Override
    public Map<String, Object> stockReportData(PmsReportQuery query)
    {
        List<PmsStockLog> list = stockReport(query);
        int inQty = 0;
        int adjustQty = 0;
        for (PmsStockLog item : list)
        {
            int change = item.getChangeQty() == null ? 0 : item.getChangeQty();
            if ("IN".equals(item.getChangeType()) || "INIT".equals(item.getChangeType()))
            {
                inQty += change;
            }
            else if (!"SALE".equals(item.getChangeType()))
            {
                adjustQty += change;
            }
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("rows", list);
        data.put("inQty", inQty);
        data.put("adjustQty", adjustQty);
        return data;
    }
}
