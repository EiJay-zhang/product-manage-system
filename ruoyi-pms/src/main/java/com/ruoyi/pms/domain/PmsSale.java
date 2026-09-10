package com.ruoyi.pms.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 销售出库 pms_sale
 */
public class PmsSale extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "销售单ID", cellType = Excel.ColumnType.NUMERIC)
    /** 销售单ID */
    private Long saleId;

    @Excel(name = "销售单号")
    /** 销售单号 */
    private String saleNo;

    /** 商品ID */
    private Long productId;

    @Excel(name = "商品名称")
    /** 商品名称 */
    private String productName;

    @Excel(name = "规格")
    /** 规格 */
    private String spec;

    /** 分类ID */
    private Long categoryId;

    @Excel(name = "数量")
    /** 销售数量 */
    private Integer qty;

    @Excel(name = "售价")
    /** 成交售价 */
    private BigDecimal salePrice;

    @Excel(name = "金额")
    /** 销售金额 */
    private BigDecimal amount;

    @Excel(name = "销售时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    /** 销售时间 */
    private Date saleTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    /** 开始 */
    private Date beginTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    /** 结束 */
    private Date endTime;

    public Long getSaleId()
    {
        return saleId;
    }

    public void setSaleId(Long saleId)
    {
        this.saleId = saleId;
    }

    public String getSaleNo()
    {
        return saleNo;
    }

    public void setSaleNo(String saleNo)
    {
        this.saleNo = saleNo;
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

    public Long getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    @NotNull(message = "销售数量不能为空")
    public Integer getQty()
    {
        return qty;
    }

    public void setQty(Integer qty)
    {
        this.qty = qty;
    }

    public BigDecimal getSalePrice()
    {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice)
    {
        this.salePrice = salePrice;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public Date getSaleTime()
    {
        return saleTime;
    }

    public void setSaleTime(Date saleTime)
    {
        this.saleTime = saleTime;
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

}
