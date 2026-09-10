package com.ruoyi.pms.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品信息 pms_product
 */
public class PmsProduct extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "商品ID", cellType = Excel.ColumnType.NUMERIC)
    /** 商品ID */
    private Long productId;

    @Excel(name = "商品编号")
    /** 商品编号 */
    private String productCode;

    @Excel(name = "商品名称")
    /** 商品名称 */
    private String productName;

    @Excel(name = "规格")
    /** 规格 */
    private String spec;

    /** 分类ID */
    private Long categoryId;

    @Excel(name = "分类")
    /** 分类名称 */
    private String categoryName;

    /** 厂家ID */
    private Long supplierId;

    @Excel(name = "进货厂家")
    /** 厂家名称 */
    private String supplierName;

    @Excel(name = "进价")
    /** 当前进价 */
    private BigDecimal purchasePrice;

    @Excel(name = "售价")
    /** 售价 */
    private BigDecimal salePrice;

    @Excel(name = "库存")
    /** 库存数量 */
    private Integer stockQty;

    @Excel(name = "进货时间", dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    /** 最近进货日期 */
    private Date purchaseTime;

    @Excel(name = "简介")
    /** 简介 */
    private String intro;

    /** 删除标志 */
    private String delFlag;

    /** 库存状态 normal/warning/empty */
    private String stockStatus;

    /** 绑定设备数 */
    private Integer deviceCount;

    /** 库存下限筛选 */
    private Integer stockMin;

    /** 库存上限筛选 */
    private Integer stockMax;

    /** 售价下限筛选 */
    private BigDecimal salePriceMin;

    /** 售价上限筛选 */
    private BigDecimal salePriceMax;

    /** 预警阈值（查询用） */
    private Integer warnThreshold;

    @JsonFormat(pattern = "yyyy-MM-dd")
    /** 进货开始 */
    private Date beginPurchaseTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    /** 进货结束 */
    private Date endPurchaseTime;

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    public String getProductCode()
    {
        return productCode;
    }

    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }

    @NotBlank(message = "商品名称不能为空")
    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    @NotBlank(message = "商品规格不能为空")
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

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    @NotNull(message = "进货厂家不能为空")
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

    @NotNull(message = "进价不能为空")
    public BigDecimal getPurchasePrice()
    {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice)
    {
        this.purchasePrice = purchasePrice;
    }

    @NotNull(message = "售价不能为空")
    public BigDecimal getSalePrice()
    {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice)
    {
        this.salePrice = salePrice;
    }

    @NotNull(message = "库存数量不能为空")
    public Integer getStockQty()
    {
        return stockQty;
    }

    public void setStockQty(Integer stockQty)
    {
        this.stockQty = stockQty;
    }

    public Date getPurchaseTime()
    {
        return purchaseTime;
    }

    public void setPurchaseTime(Date purchaseTime)
    {
        this.purchaseTime = purchaseTime;
    }

    public String getIntro()
    {
        return intro;
    }

    public void setIntro(String intro)
    {
        this.intro = intro;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getStockStatus()
    {
        return stockStatus;
    }

    public void setStockStatus(String stockStatus)
    {
        this.stockStatus = stockStatus;
    }

    public Integer getDeviceCount()
    {
        return deviceCount;
    }

    public void setDeviceCount(Integer deviceCount)
    {
        this.deviceCount = deviceCount;
    }

    public Integer getStockMin()
    {
        return stockMin;
    }

    public void setStockMin(Integer stockMin)
    {
        this.stockMin = stockMin;
    }

    public Integer getStockMax()
    {
        return stockMax;
    }

    public void setStockMax(Integer stockMax)
    {
        this.stockMax = stockMax;
    }

    public BigDecimal getSalePriceMin()
    {
        return salePriceMin;
    }

    public void setSalePriceMin(BigDecimal salePriceMin)
    {
        this.salePriceMin = salePriceMin;
    }

    public BigDecimal getSalePriceMax()
    {
        return salePriceMax;
    }

    public void setSalePriceMax(BigDecimal salePriceMax)
    {
        this.salePriceMax = salePriceMax;
    }

    public Integer getWarnThreshold()
    {
        return warnThreshold;
    }

    public void setWarnThreshold(Integer warnThreshold)
    {
        this.warnThreshold = warnThreshold;
    }

    public Date getBeginPurchaseTime()
    {
        return beginPurchaseTime;
    }

    public void setBeginPurchaseTime(Date beginPurchaseTime)
    {
        this.beginPurchaseTime = beginPurchaseTime;
    }

    public Date getEndPurchaseTime()
    {
        return endPurchaseTime;
    }

    public void setEndPurchaseTime(Date endPurchaseTime)
    {
        this.endPurchaseTime = endPurchaseTime;
    }

}
