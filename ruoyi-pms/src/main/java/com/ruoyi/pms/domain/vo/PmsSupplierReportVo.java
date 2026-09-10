package com.ruoyi.pms.domain.vo;

import java.math.BigDecimal;
import com.ruoyi.common.annotation.Excel;

public class PmsSupplierReportVo
{
    @Excel(name = "厂家")
    private String supplierName;
    @Excel(name = "供货次数")
    private Integer purchaseTimes;
    @Excel(name = "供货总量")
    private Integer totalQty;
    @Excel(name = "供货总金额")
    private BigDecimal totalAmount;
    @Excel(name = "合作商品数")
    private Integer productCount;

    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public Integer getPurchaseTimes() { return purchaseTimes; }
    public void setPurchaseTimes(Integer purchaseTimes) { this.purchaseTimes = purchaseTimes; }
    public Integer getTotalQty() { return totalQty; }
    public void setTotalQty(Integer totalQty) { this.totalQty = totalQty; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public Integer getProductCount() { return productCount; }
    public void setProductCount(Integer productCount) { this.productCount = productCount; }
}
