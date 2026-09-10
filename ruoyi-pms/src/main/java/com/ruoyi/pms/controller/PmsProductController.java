package com.ruoyi.pms.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
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
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.pms.domain.PmsProduct;
import com.ruoyi.pms.service.IPmsProductService;

@Tag(name = "商品信息")
@RestController
@RequestMapping("/pms/product")
public class PmsProductController extends BaseController
{
    @Autowired
    private IPmsProductService productService;

    @PreAuthorize("@ss.hasPermi('pms:product:list')")
    @GetMapping("/list")
    @Operation(summary = "商品分页列表")
    public TableDataInfo list(PmsProduct product)
    {
        startPage();
        return getDataTable(productService.selectProductList(product));
    }

    @PreAuthorize("@ss.hasPermi('pms:product:query')")
    @GetMapping("/optionselect")
    @Operation(summary = "商品下拉")
    public AjaxResult optionselect(PmsProduct product)
    {
        return success(productService.selectProductList(product));
    }

    @Log(title = "商品信息", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('pms:product:export')")
    @PostMapping("/export")
    @Operation(summary = "导出商品")
    public void export(HttpServletResponse response, PmsProduct product)
    {
        List<PmsProduct> list = productService.selectProductList(product);
        new ExcelUtil<PmsProduct>(PmsProduct.class).exportExcel(response, list, "商品数据");
    }

    @PreAuthorize("@ss.hasPermi('pms:product:query')")
    @GetMapping("/{productId}")
    @Operation(summary = "商品详情")
    public AjaxResult getInfo(@PathVariable Long productId)
    {
        return success(productService.selectProductById(productId));
    }

    @PreAuthorize("@ss.hasPermi('pms:product:add')")
    @Log(title = "商品信息", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "新增商品")
    public AjaxResult add(@Validated @RequestBody PmsProduct product)
    {
        product.setCreateBy(getUsername());
        return toAjax(productService.insertProduct(product));
    }

    @PreAuthorize("@ss.hasPermi('pms:product:edit')")
    @Log(title = "商品信息", businessType = BusinessType.UPDATE)
    @PutMapping
    @Operation(summary = "编辑商品")
    public AjaxResult edit(@Validated @RequestBody PmsProduct product)
    {
        product.setUpdateBy(getUsername());
        return toAjax(productService.updateProduct(product));
    }

    @PreAuthorize("@ss.hasPermi('pms:product:remove')")
    @Log(title = "商品信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{productIds}")
    @Operation(summary = "删除商品")
    public AjaxResult remove(@PathVariable Long[] productIds)
    {
        return toAjax(productService.deleteProductByIds(productIds));
    }
}
