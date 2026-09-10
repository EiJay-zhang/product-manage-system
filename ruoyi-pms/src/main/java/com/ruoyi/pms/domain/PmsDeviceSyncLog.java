package com.ruoyi.pms.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 同步日志 pms_device_sync_log
 */
public class PmsDeviceSyncLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "日志ID", cellType = Excel.ColumnType.NUMERIC)
    /** 日志ID */
    private Long logId;

    /** 设备ID */
    private Long deviceId;

    @Excel(name = "设备号")
    /** 设备SN */
    private String sn;

    /** 商品ID */
    private Long productId;

    @Excel(name = "同步内容")
    /** 同步内容 */
    private String syncContent;

    @Excel(name = "同步状态", readConverterExp = "0=失败,1=成功,2=待确认")
    /** 同步状态 */
    private String syncStatus;

    @Excel(name = "指令")
    /** 指令类型 */
    private String commandType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    /** 开始 */
    private Date beginTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    /** 结束 */
    private Date endTime;

    public Long getLogId()
    {
        return logId;
    }

    public void setLogId(Long logId)
    {
        this.logId = logId;
    }

    public Long getDeviceId()
    {
        return deviceId;
    }

    public void setDeviceId(Long deviceId)
    {
        this.deviceId = deviceId;
    }

    public String getSn()
    {
        return sn;
    }

    public void setSn(String sn)
    {
        this.sn = sn;
    }

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    public String getSyncContent()
    {
        return syncContent;
    }

    public void setSyncContent(String syncContent)
    {
        this.syncContent = syncContent;
    }

    public String getSyncStatus()
    {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus)
    {
        this.syncStatus = syncStatus;
    }

    public String getCommandType()
    {
        return commandType;
    }

    public void setCommandType(String commandType)
    {
        this.commandType = commandType;
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
