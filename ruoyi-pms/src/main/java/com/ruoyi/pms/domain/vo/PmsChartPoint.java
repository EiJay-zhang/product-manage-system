package com.ruoyi.pms.domain.vo;

import java.math.BigDecimal;

public class PmsChartPoint
{
    private String label;
    private BigDecimal value;
    private Integer qty;

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }
    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }
}
