package com.ruoyi.pms.domain.vo;

import java.math.BigDecimal;
import com.ruoyi.common.annotation.Excel;

public class PmsCostReportVo
{
    @Excel(name = "商品名称")
    private String productName;
    @Excel(name = "规格")
    private String spec;
    @Excel(name = "当前库存")
    private Integer stockQty;
    @Excel(name = "当前进价")
    private BigDecimal purchasePrice;
    @Excel(name = "库存成本")
    private BigDecimal stockCost;
    @Excel(name = "期间进货总成本")
    private BigDecimal purchaseCost;
    @Excel(name = "期间进货量")
    private Integer purchaseQty;
    @Excel(name = "平均进价")
    private BigDecimal avgPrice;

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getSpec() { return spec; }
    public void setSpec(String spec) { this.spec = spec; }
    public Integer getStockQty() { return stockQty; }
    public void setStockQty(Integer stockQty) { this.stockQty = stockQty; }
    public BigDecimal getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; }
    public BigDecimal getStockCost() { return stockCost; }
    public void setStockCost(BigDecimal stockCost) { this.stockCost = stockCost; }
    public BigDecimal getPurchaseCost() { return purchaseCost; }
    public void setPurchaseCost(BigDecimal purchaseCost) { this.purchaseCost = purchaseCost; }
    public Integer getPurchaseQty() { return purchaseQty; }
    public void setPurchaseQty(Integer purchaseQty) { this.purchaseQty = purchaseQty; }
    public BigDecimal getAvgPrice() { return avgPrice; }
    public void setAvgPrice(BigDecimal avgPrice) { this.avgPrice = avgPrice; }
}
