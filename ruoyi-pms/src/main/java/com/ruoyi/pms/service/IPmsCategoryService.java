package com.ruoyi.pms.service;

import java.util.List;
import com.ruoyi.pms.domain.PmsCategory;

public interface IPmsCategoryService
{
    public PmsCategory selectCategoryById(Long categoryId);
    public List<PmsCategory> selectCategoryList(PmsCategory category);
    public int insertCategory(PmsCategory category);
    public int updateCategory(PmsCategory category);
    public int deleteCategoryByIds(Long[] categoryIds);
}
