package com.ruoyi.pms.domain.vo;

import java.math.BigDecimal;

public class PmsLogisticsStatsVo
{
    private String name;
    private Integer billCount;
    private BigDecimal freight;
    private BigDecimal otherFee;
    private BigDecimal totalFee;
    private BigDecimal settledFee;
    private BigDecimal unsettledFee;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getBillCount() { return billCount; }
    public void setBillCount(Integer billCount) { this.billCount = billCount; }
    public BigDecimal getFreight() { return freight; }
    public void setFreight(BigDecimal freight) { this.freight = freight; }
    public BigDecimal getOtherFee() { return otherFee; }
    public void setOtherFee(BigDecimal otherFee) { this.otherFee = otherFee; }
    public BigDecimal getTotalFee() { return totalFee; }
    public void setTotalFee(BigDecimal totalFee) { this.totalFee = totalFee; }
    public BigDecimal getSettledFee() { return settledFee; }
    public void setSettledFee(BigDecimal settledFee) { this.settledFee = settledFee; }
    public BigDecimal getUnsettledFee() { return unsettledFee; }
    public void setUnsettledFee(BigDecimal unsettledFee) { this.unsettledFee = unsettledFee; }
}
