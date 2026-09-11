package com.ruoyi.pms.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 进货台账 pms_purchase
 */
public class PmsPurchase extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "进货单ID", cellType = Excel.ColumnType.NUMERIC)
    /** 进货单ID */
    private Long purchaseId;

    @Excel(name = "进货单号")
    /** 进货单号 */
    private String purchaseNo;

    /** 商品ID */
    private Long productId;

    @Excel(name = "商品名称")
    /** 商品名称 */
    private String productName;

    @Excel(name = "规格")
    /** 规格 */
    private String spec;

    /** 厂家ID */
    private Long supplierId;

    @Excel(name = "供货厂家")
    /** 厂家 */
    private String supplierName;

    @Excel(name = "进货数量")
    /** 进货数量 */
    private Integer qty;

    @Excel(name = "单品进价")
    /** 本次进价 */
    private BigDecimal purchasePrice;

    @Excel(name = "本次进货金额")
    /** 本次金额 */
    private BigDecimal amount;

    @Excel(name = "进货时间", dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    /** 进货日期 */
    private Date purchaseTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    /** 开始时间 */
    private Date beginTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    /** 结束时间 */
    private Date endTime;

    @Excel(name = "状态", readConverterExp = "0=正常,1=作废")
    /** 状态（0正常 1作废） */
    private String status;

    public Long getPurchaseId()
    {
        return purchaseId;
    }

    public void setPurchaseId(Long purchaseId)
    {
        this.purchaseId = purchaseId;
    }

    public String getPurchaseNo()
    {
        return purchaseNo;
    }

    public void setPurchaseNo(String purchaseNo)
    {
        this.purchaseNo = purchaseNo;
    }

    @NotNull(message = "商品不能为空")
    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getSpec()
    {
        return spec;
    }

    public void setSpec(String spec)
    {
        this.spec = spec;
    }

    public Long getSupplierId()
    {
        return supplierId;
    }

    public void setSupplierId(Long supplierId)
    {
        this.supplierId = supplierId;
    }

    public String getSupplierName()
    {
        return supplierName;
    }

    public void setSupplierName(String supplierName)
    {
        this.supplierName = supplierName;
    }

    @NotNull(message = "进货数量不能为空")
    public Integer getQty()
    {
        return qty;
    }

    public void setQty(Integer qty)
    {
        this.qty = qty;
    }

    @NotNull(message = "进价不能为空")
    public BigDecimal getPurchasePrice()
    {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice)
    {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    @NotNull(message = "进货时间不能为空")
    public Date getPurchaseTime()
    {
        return purchaseTime;
    }

    public void setPurchaseTime(Date purchaseTime)
    {
        this.purchaseTime = purchaseTime;
    }

    public Date getBeginTime()
    {
        return beginTime;
    }

    public void setBeginTime(Date beginTime)
    {
        this.beginTime = beginTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

}
