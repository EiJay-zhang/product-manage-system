package com.ruoyi.pms.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.pms.domain.PmsCategory;
import com.ruoyi.pms.mapper.PmsCategoryMapper;
import com.ruoyi.pms.service.IPmsCategoryService;

@Service
public class PmsCategoryServiceImpl implements IPmsCategoryService
{
    @Autowired
    private PmsCategoryMapper categoryMapper;

    @Override
    public PmsCategory selectCategoryById(Long categoryId)
    {
        return categoryMapper.selectCategoryById(categoryId);
    }

    @Override
    public List<PmsCategory> selectCategoryList(PmsCategory category)
    {
        return categoryMapper.selectCategoryList(category);
    }

    private void checkNameUnique(PmsCategory category)
    {
        PmsCategory exist = categoryMapper.checkCategoryNameUnique(category.getCategoryName());
        if (exist != null && !exist.getCategoryId().equals(category.getCategoryId()))
        {
            throw new ServiceException("分类名称已存在");
        }
    }

    @Override
    public int insertCategory(PmsCategory category)
    {
        if (category.getOrderNum() == null)
        {
            category.setOrderNum(0);
        }
        if (StringUtils.isEmpty(category.getStatus()))
        {
            category.setStatus("0");
        }
        checkNameUnique(category);
        return categoryMapper.insertCategory(category);
    }

    @Override
    public int updateCategory(PmsCategory category)
    {
        checkNameUnique(category);
        return categoryMapper.updateCategory(category);
    }

    @Override
    public int deleteCategoryByIds(Long[] categoryIds)
    {
        for (Long id : categoryIds)
        {
            if (categoryMapper.countProductByCategoryId(id) > 0)
            {
                throw new ServiceException("分类下存在商品，无法删除");
            }
        }
        return categoryMapper.deleteCategoryByIds(categoryIds);
    }
}
