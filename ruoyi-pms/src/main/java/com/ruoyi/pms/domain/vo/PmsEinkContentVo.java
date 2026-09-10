package com.ruoyi.pms.domain.vo;

import java.math.BigDecimal;

public class PmsEinkContentVo
{
    private String sn;
    private String productName;
    private String spec;
    private BigDecimal salePrice;
    private String intro;
    private Integer stockQty;
    private String supplierName;
    private String fontStyle;
    private String empty;

    public String getSn() { return sn; }
    public void setSn(String sn) { this.sn = sn; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getSpec() { return spec; }
    public void setSpec(String spec) { this.spec = spec; }
    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }
    public String getIntro() { return intro; }
    public void setIntro(String intro) { this.intro = intro; }
    public Integer getStockQty() { return stockQty; }
    public void setStockQty(Integer stockQty) { this.stockQty = stockQty; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public String getFontStyle() { return fontStyle; }
    public void setFontStyle(String fontStyle) { this.fontStyle = fontStyle; }
    public String getEmpty() { return empty; }
    public void setEmpty(String empty) { this.empty = empty; }
}
