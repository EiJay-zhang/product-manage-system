package com.ruoyi.pms.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.math.BigDecimal;

/**
 * 业务配置 pms_setting
 */
public class PmsSetting extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 配置ID */
    private Long settingId;

    /** 库存预警阈值 */
    private Integer stockWarnThreshold;

    /** 营收下限 */
    private BigDecimal turnoverWarnMin;

    /** 营收上限 */
    private BigDecimal turnoverWarnMax;

    /** 同步频率秒 */
    private Integer einkSyncIntervalSec;

    /** 字体样式 */
    private String einkFontStyle;

    /** 展示名称 */
    private String showName;

    /** 展示规格 */
    private String showSpec;

    /** 展示售价 */
    private String showSalePrice;

    /** 展示简介 */
    private String showIntro;

    /** 展示库存 */
    private String showStock;

    /** 展示厂家 */
    private String showSupplier;

    public Long getSettingId()
    {
        return settingId;
    }

    public void setSettingId(Long settingId)
    {
        this.settingId = settingId;
    }

    public Integer getStockWarnThreshold()
    {
        return stockWarnThreshold;
    }

    public void setStockWarnThreshold(Integer stockWarnThreshold)
    {
        this.stockWarnThreshold = stockWarnThreshold;
    }

    public BigDecimal getTurnoverWarnMin()
    {
        return turnoverWarnMin;
    }

    public void setTurnoverWarnMin(BigDecimal turnoverWarnMin)
    {
        this.turnoverWarnMin = turnoverWarnMin;
    }

    public BigDecimal getTurnoverWarnMax()
    {
        return turnoverWarnMax;
    }

    public void setTurnoverWarnMax(BigDecimal turnoverWarnMax)
    {
        this.turnoverWarnMax = turnoverWarnMax;
    }

    public Integer getEinkSyncIntervalSec()
    {
        return einkSyncIntervalSec;
    }

    public void setEinkSyncIntervalSec(Integer einkSyncIntervalSec)
    {
        this.einkSyncIntervalSec = einkSyncIntervalSec;
    }

    public String getEinkFontStyle()
    {
        return einkFontStyle;
    }

    public void setEinkFontStyle(String einkFontStyle)
    {
        this.einkFontStyle = einkFontStyle;
    }

    public String getShowName()
    {
        return showName;
    }

    public void setShowName(String showName)
    {
        this.showName = showName;
    }

    public String getShowSpec()
    {
        return showSpec;
    }

    public void setShowSpec(String showSpec)
    {
        this.showSpec = showSpec;
    }

    public String getShowSalePrice()
    {
        return showSalePrice;
    }

    public void setShowSalePrice(String showSalePrice)
    {
        this.showSalePrice = showSalePrice;
    }

    public String getShowIntro()
    {
        return showIntro;
    }

    public void setShowIntro(String showIntro)
    {
        this.showIntro = showIntro;
    }

    public String getShowStock()
    {
        return showStock;
    }

    public void setShowStock(String showStock)
    {
        this.showStock = showStock;
    }

    public String getShowSupplier()
    {
        return showSupplier;
    }

    public void setShowSupplier(String showSupplier)
    {
        this.showSupplier = showSupplier;
    }

}
