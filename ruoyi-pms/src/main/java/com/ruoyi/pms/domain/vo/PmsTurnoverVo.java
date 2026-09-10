package com.ruoyi.pms.domain.vo;

import java.math.BigDecimal;

public class PmsTurnoverVo
{
    private BigDecimal turnover;
    private Integer qty;
    private Integer productCount;
    private BigDecimal avgDaily;
    private String label;

    public BigDecimal getTurnover() { return turnover; }
    public void setTurnover(BigDecimal turnover) { this.turnover = turnover; }
    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }
    public Integer getProductCount() { return productCount; }
    public void setProductCount(Integer productCount) { this.productCount = productCount; }
    public BigDecimal getAvgDaily() { return avgDaily; }
    public void setAvgDaily(BigDecimal avgDaily) { this.avgDaily = avgDaily; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
}
