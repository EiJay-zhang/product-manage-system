package com.ruoyi.pms.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import jakarta.validation.constraints.NotBlank;

/**
 * 物流商 pms_carrier
 */
public class PmsCarrier extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "物流商ID", cellType = Excel.ColumnType.NUMERIC)
    /** 物流商ID */
    private Long carrierId;

    @Excel(name = "物流商名称")
    /** 物流商名称 */
    private String carrierName;

    @Excel(name = "联系人")
    /** 联系人 */
    private String contactName;

    @Excel(name = "联系电话")
    /** 联系电话 */
    private String phone;

    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    /** 状态 */
    private String status;

    /** 删除标志 */
    private String delFlag;

    public Long getCarrierId()
    {
        return carrierId;
    }

    public void setCarrierId(Long carrierId)
    {
        this.carrierId = carrierId;
    }

    @NotBlank(message = "物流商名称不能为空")
    public String getCarrierName()
    {
        return carrierName;
    }

    public void setCarrierName(String carrierName)
    {
        this.carrierName = carrierName;
    }

    public String getContactName()
    {
        return contactName;
    }

    public void setContactName(String contactName)
    {
        this.contactName = contactName;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

}
