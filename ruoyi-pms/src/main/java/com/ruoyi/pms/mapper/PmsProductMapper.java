package com.ruoyi.pms.mapper;

import java.util.List;
import com.ruoyi.pms.domain.PmsProduct;

public interface PmsProductMapper
{
    public PmsProduct selectProductById(Long productId);
    public List<PmsProduct> selectProductList(PmsProduct product);
    public String selectMaxProductCode(String prefix);
    public int insertProduct(PmsProduct product);
    public int updateProduct(PmsProduct product);
    public int updateProductStock(PmsProduct product);
    public int deleteProductByIds(Long[] productIds);
}
