package com.ruoyi.pms.mapper;

import java.util.List;
import com.ruoyi.pms.domain.PmsCategory;

public interface PmsCategoryMapper
{
    public PmsCategory selectCategoryById(Long categoryId);
    public List<PmsCategory> selectCategoryList(PmsCategory category);
    public PmsCategory checkCategoryNameUnique(String categoryName);
    public int insertCategory(PmsCategory category);
    public int updateCategory(PmsCategory category);
    public int deleteCategoryByIds(Long[] categoryIds);
    public int countProductByCategoryId(Long categoryId);
}
