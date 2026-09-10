package com.ruoyi.pms.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 墨水屏设备 pms_device
 */
public class PmsDevice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "设备ID", cellType = Excel.ColumnType.NUMERIC)
    /** 设备ID */
    private Long deviceId;

    @Excel(name = "SN")
    /** 设备SN */
    private String sn;

    @Excel(name = "设备编号")
    /** 设备编号 */
    private String deviceCode;

    /** 绑定商品ID */
    private Long productId;

    @Excel(name = "绑定商品")
    /** 绑定商品 */
    private String productName;

    @Excel(name = "货架")
    /** 货架编号 */
    private String shelfNo;

    @Excel(name = "在线状态", readConverterExp = "0=离线,1=在线")
    /** 在线状态 */
    private String onlineStatus;

    @Excel(name = "最后同步时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    /** 最后同步时间 */
    private Date lastSyncTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    /** 最后心跳 */
    private Date lastHeartbeat;

    /** 电量 */
    private Integer battery;

    /** 固件版本 */
    private String firmware;

    @Excel(name = "异常", readConverterExp = "0=否,1=是")
    /** 异常标记 */
    private String abnormalFlag;

    /** 异常备注 */
    private String abnormalRemark;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    /** 绑定时间 */
    private Date bindTime;

    /** 批量设备ID */
    private Long[] deviceIds;

    /** 批量商品ID */
    private Long[] productIds;

    /** 心跳超时秒 */
    private Integer heartbeatTimeoutSec;

    public Long getDeviceId()
    {
        return deviceId;
    }

    public void setDeviceId(Long deviceId)
    {
        this.deviceId = deviceId;
    }

    @NotBlank(message = "设备SN不能为空")
    public String getSn()
    {
        return sn;
    }

    public void setSn(String sn)
    {
        this.sn = sn;
    }

    public String getDeviceCode()
    {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode)
    {
        this.deviceCode = deviceCode;
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

    public String getShelfNo()
    {
        return shelfNo;
    }

    public void setShelfNo(String shelfNo)
    {
        this.shelfNo = shelfNo;
    }

    public String getOnlineStatus()
    {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus)
    {
        this.onlineStatus = onlineStatus;
    }

    public Date getLastSyncTime()
    {
        return lastSyncTime;
    }

    public void setLastSyncTime(Date lastSyncTime)
    {
        this.lastSyncTime = lastSyncTime;
    }

    public Date getLastHeartbeat()
    {
        return lastHeartbeat;
    }

    public void setLastHeartbeat(Date lastHeartbeat)
    {
        this.lastHeartbeat = lastHeartbeat;
    }

    public Integer getBattery()
    {
        return battery;
    }

    public void setBattery(Integer battery)
    {
        this.battery = battery;
    }

    public String getFirmware()
    {
        return firmware;
    }

    public void setFirmware(String firmware)
    {
        this.firmware = firmware;
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

    public Date getBindTime()
    {
        return bindTime;
    }

    public void setBindTime(Date bindTime)
    {
        this.bindTime = bindTime;
    }

    public Long[] getDeviceIds()
    {
        return deviceIds;
    }

    public void setDeviceIds(Long[] deviceIds)
    {
        this.deviceIds = deviceIds;
    }

    public Long[] getProductIds()
    {
        return productIds;
    }

    public void setProductIds(Long[] productIds)
    {
        this.productIds = productIds;
    }

    public Integer getHeartbeatTimeoutSec()
    {
        return heartbeatTimeoutSec;
    }

    public void setHeartbeatTimeoutSec(Integer heartbeatTimeoutSec)
    {
        this.heartbeatTimeoutSec = heartbeatTimeoutSec;
    }

}
