package com.ruoyi.pms.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import com.ruoyi.common.utils.StringUtils;

public class PmsDateRange
{
    public Date begin;
    public Date end;

    public static PmsDateRange of(String range, Date beginTime, Date endTime)
    {
        PmsDateRange r = new PmsDateRange();
        ZoneId z = ZoneId.systemDefault();
        LocalDate today = LocalDate.now();
        if (StringUtils.isEmpty(range) || "custom".equals(range))
        {
            r.begin = beginTime;
            r.end = endTime;
            return r;
        }
        LocalDate start;
        LocalDate end;
        switch (range)
        {
            case "today":
                start = today; end = today; break;
            case "yesterday":
                start = today.minusDays(1); end = start; break;
            case "week":
                start = today.with(DayOfWeek.MONDAY); end = today; break;
            case "lastWeek":
                start = today.with(DayOfWeek.MONDAY).minusWeeks(1);
                end = start.plusDays(6); break;
            case "month":
                start = today.with(TemporalAdjusters.firstDayOfMonth()); end = today; break;
            case "lastMonth":
                start = today.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
                end = today.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()); break;
            case "year":
                start = today.with(TemporalAdjusters.firstDayOfYear()); end = today; break;
            default:
                start = today; end = today;
        }
        r.begin = Date.from(start.atStartOfDay(z).toInstant());
        r.end = Date.from(LocalDateTime.of(end, LocalTime.MAX).atZone(z).toInstant());
        return r;
    }

    public static Date toDate(LocalDate d)
    {
        return Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
