package com.ruoyi.pms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.pms.domain.PmsCategory;
import com.ruoyi.pms.service.IPmsCategoryService;

@Tag(name = "商品分类")
@RestController
@RequestMapping("/pms/category")
public class PmsCategoryController extends BaseController
{
    @Autowired
    private IPmsCategoryService categoryService;

    @PreAuthorize("@ss.hasPermi('pms:category:list')")
    @GetMapping("/list")
    @Operation(summary = "分类列表")
    public TableDataInfo list(PmsCategory category)
    {
        startPage();
        return getDataTable(categoryService.selectCategoryList(category));
    }

    @PreAuthorize("@ss.hasPermi('pms:category:query')")
    @GetMapping("/optionselect")
    @Operation(summary = "分类下拉")
    public AjaxResult optionselect(PmsCategory category)
    {
        return success(categoryService.selectCategoryList(category));
    }

    @PreAuthorize("@ss.hasPermi('pms:category:query')")
    @GetMapping("/{categoryId}")
    @Operation(summary = "分类详情")
    public AjaxResult getInfo(@PathVariable Long categoryId)
    {
        return success(categoryService.selectCategoryById(categoryId));
    }

    @PreAuthorize("@ss.hasPermi('pms:category:add')")
    @Log(title = "商品分类", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "新增分类")
    public AjaxResult add(@Validated @RequestBody PmsCategory category)
    {
        category.setCreateBy(getUsername());
        return toAjax(categoryService.insertCategory(category));
    }

    @PreAuthorize("@ss.hasPermi('pms:category:edit')")
    @Log(title = "商品分类", businessType = BusinessType.UPDATE)
    @PutMapping
    @Operation(summary = "编辑分类")
    public AjaxResult edit(@Validated @RequestBody PmsCategory category)
    {
        category.setUpdateBy(getUsername());
        return toAjax(categoryService.updateCategory(category));
    }

    @PreAuthorize("@ss.hasPermi('pms:category:remove')")
    @Log(title = "商品分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    @Operation(summary = "删除分类")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(categoryService.deleteCategoryByIds(ids));
    }
}
