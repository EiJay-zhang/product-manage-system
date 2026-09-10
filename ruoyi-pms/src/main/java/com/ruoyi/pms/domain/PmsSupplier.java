package com.ruoyi.pms.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import jakarta.validation.constraints.NotBlank;

/**
 * 供应商 pms_supplier
 */
public class PmsSupplier extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "厂家ID", cellType = Excel.ColumnType.NUMERIC)
    /** 厂家ID */
    private Long supplierId;

    @Excel(name = "厂家名称")
    /** 厂家名称 */
    private String supplierName;

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

    public Long getSupplierId()
    {
        return supplierId;
    }

    public void setSupplierId(Long supplierId)
    {
        this.supplierId = supplierId;
    }

    @NotBlank(message = "厂家名称不能为空")
    public String getSupplierName()
    {
        return supplierName;
    }

    public void setSupplierName(String supplierName)
    {
        this.supplierName = supplierName;
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
