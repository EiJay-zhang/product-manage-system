package com.ruoyi.pms.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import jakarta.validation.constraints.NotBlank;

/**
 * 商品分类 pms_category
 */
public class PmsCategory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "分类ID", cellType = Excel.ColumnType.NUMERIC)
    /** 分类ID */
    private Long categoryId;

    @Excel(name = "分类名称")
    /** 分类名称 */
    private String categoryName;

    @Excel(name = "显示顺序")
    /** 显示顺序 */
    private Integer orderNum;

    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    /** 状态 */
    private String status;

    /** 删除标志 */
    private String delFlag;

    public Long getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    @NotBlank(message = "分类名称不能为空")
    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public Integer getOrderNum()
    {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum)
    {
        this.orderNum = orderNum;
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
