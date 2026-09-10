package com.ruoyi.pms.domain.vo;

import java.math.BigDecimal;

public class PmsDashboardVo
{
    private Integer productCount;
    private Integer warnCount;
    private Integer emptyCount;
    private Integer offlineDeviceCount;
    private Integer uncheckedCount;
    private Integer unpaidCount;
    private BigDecimal todayTurnover;
    private String turnoverWarn;

    public Integer getProductCount() { return productCount; }
    public void setProductCount(Integer productCount) { this.productCount = productCount; }
    public Integer getWarnCount() { return warnCount; }
    public void setWarnCount(Integer warnCount) { this.warnCount = warnCount; }
    public Integer getEmptyCount() { return emptyCount; }
    public void setEmptyCount(Integer emptyCount) { this.emptyCount = emptyCount; }
    public Integer getOfflineDeviceCount() { return offlineDeviceCount; }
    public void setOfflineDeviceCount(Integer offlineDeviceCount) { this.offlineDeviceCount = offlineDeviceCount; }
    public Integer getUncheckedCount() { return uncheckedCount; }
    public void setUncheckedCount(Integer uncheckedCount) { this.uncheckedCount = uncheckedCount; }
    public Integer getUnpaidCount() { return unpaidCount; }
    public void setUnpaidCount(Integer unpaidCount) { this.unpaidCount = unpaidCount; }
    public BigDecimal getTodayTurnover() { return todayTurnover; }
    public void setTodayTurnover(BigDecimal todayTurnover) { this.todayTurnover = todayTurnover; }
    public String getTurnoverWarn() { return turnoverWarn; }
    public void setTurnoverWarn(String turnoverWarn) { this.turnoverWarn = turnoverWarn; }
}
