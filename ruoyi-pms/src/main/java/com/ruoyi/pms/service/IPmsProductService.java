package com.ruoyi.pms.service;

import java.util.List;
import com.ruoyi.pms.domain.PmsProduct;

public interface IPmsProductService
{
    public PmsProduct selectProductById(Long productId);
    public List<PmsProduct> selectProductList(PmsProduct product);
    public int insertProduct(PmsProduct product);
    public int updateProduct(PmsProduct product);
    public int deleteProductByIds(Long[] productIds);
    public String importProduct(List<PmsProduct> products, Boolean updateSupport, String operName);
}
