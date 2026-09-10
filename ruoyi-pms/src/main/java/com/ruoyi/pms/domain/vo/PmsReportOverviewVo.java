package com.ruoyi.pms.domain.vo;

import java.math.BigDecimal;
import com.ruoyi.common.annotation.Excel;

public class PmsReportOverviewVo
{
    @Excel(name = "商品总数")
    private Integer productCount;
    @Excel(name = "累计进货量")
    private Integer purchaseQty;
    @Excel(name = "当前总库存")
    private Integer stockQty;
    @Excel(name = "累计进货总成本")
    private BigDecimal purchaseCost;
    @Excel(name = "库存预警商品数")
    private Integer warnCount;
    @Excel(name = "无库存商品数")
    private Integer emptyCount;

    public Integer getProductCount() { return productCount; }
    public void setProductCount(Integer productCount) { this.productCount = productCount; }
    public Integer getPurchaseQty() { return purchaseQty; }
    public void setPurchaseQty(Integer purchaseQty) { this.purchaseQty = purchaseQty; }
    public Integer getStockQty() { return stockQty; }
    public void setStockQty(Integer stockQty) { this.stockQty = stockQty; }
    public BigDecimal getPurchaseCost() { return purchaseCost; }
    public void setPurchaseCost(BigDecimal purchaseCost) { this.purchaseCost = purchaseCost; }
    public Integer getWarnCount() { return warnCount; }
    public void setWarnCount(Integer warnCount) { this.warnCount = warnCount; }
    public Integer getEmptyCount() { return emptyCount; }
    public void setEmptyCount(Integer emptyCount) { this.emptyCount = emptyCount; }
}
