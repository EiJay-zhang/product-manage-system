package com.ruoyi.pms.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 物流对账 pms_logistics
 */
public class PmsLogistics extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "物流单ID", cellType = Excel.ColumnType.NUMERIC)
    /** 物流单ID */
    private Long logisticsId;

    @Excel(name = "物流单号")
    /** 物流单号 */
    private String logisticsNo;

    /** 进货批次 */
    private Long purchaseId;

    @Excel(name = "进货单号")
    /** 进货单号 */
    private String purchaseNo;

    /** 商品ID */
    private Long productId;

    @Excel(name = "对应商品")
    /** 商品 */
    private String productName;

    /** 厂家ID */
    private Long supplierId;

    @Excel(name = "供货厂家")
    /** 厂家 */
    private String supplierName;

    /** 物流商ID */
    private Long carrierId;

    @Excel(name = "物流服务商")
    /** 物流服务商 */
    private String carrier;

    @Excel(name = "发货时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    /** 发货时间 */
    private Date shipTime;

    @Excel(name = "到货时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    /** 到货时间 */
    private Date arriveTime;

    @Excel(name = "重量/体积")
    /** 重量/体积 */
    private String weightVolume;

    @Excel(name = "运费")
    /** 运费 */
    private BigDecimal freight;

    @Excel(name = "保价费")
    /** 保价费 */
    private BigDecimal insuranceFee;

    @Excel(name = "其他杂费")
    /** 其他杂费 */
    private BigDecimal otherFee;

    @Excel(name = "总物流费用")
    /** 总物流费用 */
    private BigDecimal totalFee;

    @Excel(name = "付款状态", readConverterExp = "0=未对账,1=已对账,2=已结清")
    /** 付款状态 */
    private String payStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    /** 对账时间 */
    private Date reconcileTime;

    @Excel(name = "对账人")
    /** 对账人 */
    private String reconcileBy;

    @Excel(name = "异常", readConverterExp = "0=否,1=是")
    /** 异常标记 */
    private String abnormalFlag;

    @Excel(name = "异常备注")
    /** 异常备注 */
    private String abnormalRemark;

    /** 批量ID */
    private Long[] ids;

    @JsonFormat(pattern = "yyyy-MM-dd")
    /** 开始 */
    private Date beginTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    /** 结束 */
    private Date endTime;

    public Long getLogisticsId()
    {
        return logisticsId;
    }

    public void setLogisticsId(Long logisticsId)
    {
        this.logisticsId = logisticsId;
    }

    @NotBlank(message = "物流单号不能为空")
    public String getLogisticsNo()
    {
        return logisticsNo;
    }

    public void setLogisticsNo(String logisticsNo)
    {
        this.logisticsNo = logisticsNo;
    }

    @NotNull(message = "请关联进货批次")
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

    public Long getCarrierId()
    {
        return carrierId;
    }

    public void setCarrierId(Long carrierId)
    {
        this.carrierId = carrierId;
    }

    public String getCarrier()
    {
        return carrier;
    }

    public void setCarrier(String carrier)
    {
        this.carrier = carrier;
    }

    public Date getShipTime()
    {
        return shipTime;
    }

    public void setShipTime(Date shipTime)
    {
        this.shipTime = shipTime;
    }

    public Date getArriveTime()
    {
        return arriveTime;
    }

    public void setArriveTime(Date arriveTime)
    {
        this.arriveTime = arriveTime;
    }

    public String getWeightVolume()
    {
        return weightVolume;
    }

    public void setWeightVolume(String weightVolume)
    {
        this.weightVolume = weightVolume;
    }

    public BigDecimal getFreight()
    {
        return freight;
    }

    public void setFreight(BigDecimal freight)
    {
        this.freight = freight;
    }

    public BigDecimal getInsuranceFee()
    {
        return insuranceFee;
    }

    public void setInsuranceFee(BigDecimal insuranceFee)
    {
        this.insuranceFee = insuranceFee;
    }

    public BigDecimal getOtherFee()
    {
        return otherFee;
    }

    public void setOtherFee(BigDecimal otherFee)
    {
        this.otherFee = otherFee;
    }

    public BigDecimal getTotalFee()
    {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee)
    {
        this.totalFee = totalFee;
    }

    public String getPayStatus()
    {
        return payStatus;
    }

    public void setPayStatus(String payStatus)
    {
        this.payStatus = payStatus;
    }

    public Date getReconcileTime()
    {
        return reconcileTime;
    }

    public void setReconcileTime(Date reconcileTime)
    {
        this.reconcileTime = reconcileTime;
    }

    public String getReconcileBy()
    {
        return reconcileBy;
    }

    public void setReconcileBy(String reconcileBy)
    {
        this.reconcileBy = reconcileBy;
    }

    public String getAbnormalFlag()
    {
        return abnormalFlag;
    }

    public void setAbnormalFlag(String abnormalFlag)
    {
        this.abnormalFlag = abnormalFlag;
    }

    public String getAbnormalRemark()
    {
        return abnormalRemark;
    }

    public void setAbnormalRemark(String abnormalRemark)
    {
        this.abnormalRemark = abnormalRemark;
    }

    public Long[] getIds()
    {
        return ids;
    }

    public void setIds(Long[] ids)
    {
        this.ids = ids;
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
