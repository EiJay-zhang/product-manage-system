package com.ruoyi.pms.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.pms.domain.PmsProduct;

public interface PmsProductMapper
{
    public PmsProduct selectProductById(Long productId);
    public PmsProduct selectProductByCode(String productCode);
    public PmsProduct selectProductByNameAndSpec(@Param("productName") String productName, @Param("spec") String spec);
    public List<PmsProduct> selectProductList(PmsProduct product);
    public String selectMaxProductCode(String prefix);
    public int insertProduct(PmsProduct product);
    public int updateProduct(PmsProduct product);
    public int updateProductStock(PmsProduct product);
    public int deleteProductByIds(Long[] productIds);
}
