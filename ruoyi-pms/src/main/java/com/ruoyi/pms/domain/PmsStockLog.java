package com.ruoyi.pms.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 库存流水 pms_stock_log
 */
public class PmsStockLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "流水ID", cellType = Excel.ColumnType.NUMERIC)
    /** 流水ID */
    private Long logId;

    /** 商品ID */
    private Long productId;

    @Excel(name = "商品名称")
    /** 商品名称 */
    private String productName;

    @Excel(name = "操作类型", readConverterExp = "IN=入库,CHECK=盘点,LOSS=损耗,TRANSFER=调拨,SALE=销售,INIT=建档,VOID=作废,RETURN=退货")
    /** 变动类型 */
    private String changeType;

    @Excel(name = "变动前")
    /** 变动前 */
    private Integer beforeQty;

    @Excel(name = "变动数量")
    /** 变动数量 */
    private Integer changeQty;

    @Excel(name = "结余库存")
    /** 结余库存 */
    private Integer afterQty;

    /** 业务类型 */
    private String bizType;

    /** 业务单据ID */
    private Long bizId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    /** 开始时间 */
    private Date beginTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    /** 结束时间 */
    private Date endTime;

    /** 调整后库存（请求字段） */
    private Integer afterQtyTarget;

    public Long getLogId()
    {
        return logId;
    }

    public void setLogId(Long logId)
    {
        this.logId = logId;
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

    public String getChangeType()
    {
        return changeType;
    }

    public void setChangeType(String changeType)
    {
        this.changeType = changeType;
    }

    public Integer getBeforeQty()
    {
        return beforeQty;
    }

    public void setBeforeQty(Integer beforeQty)
    {
        this.beforeQty = beforeQty;
    }

    public Integer getChangeQty()
    {
        return changeQty;
    }

    public void setChangeQty(Integer changeQty)
    {
        this.changeQty = changeQty;
    }

    public Integer getAfterQty()
    {
        return afterQty;
    }

    public void setAfterQty(Integer afterQty)
    {
        this.afterQty = afterQty;
    }

    public String getBizType()
    {
        return bizType;
    }

    public void setBizType(String bizType)
    {
        this.bizType = bizType;
    }

    public Long getBizId()
    {
        return bizId;
    }

    public void setBizId(Long bizId)
    {
        this.bizId = bizId;
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

    public Integer getAfterQtyTarget()
    {
        return afterQtyTarget;
    }

    public void setAfterQtyTarget(Integer afterQtyTarget)
    {
        this.afterQtyTarget = afterQtyTarget;
    }

}
