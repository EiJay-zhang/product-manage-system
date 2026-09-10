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
import com.ruoyi.pms.domain.PmsSupplier;
import com.ruoyi.pms.service.IPmsSupplierService;

@Tag(name = "供应商")
@RestController
@RequestMapping("/pms/supplier")
public class PmsSupplierController extends BaseController
{
    @Autowired
    private IPmsSupplierService supplierService;

    @PreAuthorize("@ss.hasPermi('pms:supplier:list')")
    @GetMapping("/list")
    @Operation(summary = "厂家列表")
    public TableDataInfo list(PmsSupplier supplier)
    {
        startPage();
        return getDataTable(supplierService.selectSupplierList(supplier));
    }

    @PreAuthorize("@ss.hasPermi('pms:supplier:query')")
    @GetMapping("/optionselect")
    @Operation(summary = "厂家下拉")
    public AjaxResult optionselect(PmsSupplier supplier)
    {
        return success(supplierService.selectSupplierList(supplier));
    }

    @Log(title = "供应商", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('pms:supplier:export')")
    @PostMapping("/export")
    @Operation(summary = "导出厂家")
    public void export(HttpServletResponse response, PmsSupplier supplier)
    {
        List<PmsSupplier> list = supplierService.selectSupplierList(supplier);
        new ExcelUtil<PmsSupplier>(PmsSupplier.class).exportExcel(response, list, "供应商数据");
    }

    @PreAuthorize("@ss.hasPermi('pms:supplier:query')")
    @GetMapping("/{supplierId}")
    @Operation(summary = "厂家详情")
    public AjaxResult getInfo(@PathVariable Long supplierId)
    {
        return success(supplierService.selectSupplierById(supplierId));
    }

    @PreAuthorize("@ss.hasPermi('pms:supplier:query')")
    @GetMapping("/{supplierId}/products")
    @Operation(summary = "厂家供货商品")
    public AjaxResult products(@PathVariable Long supplierId)
    {
        return success(supplierService.selectProducts(supplierId));
    }

    @PreAuthorize("@ss.hasPermi('pms:supplier:query')")
    @GetMapping("/{supplierId}/purchases")
    @Operation(summary = "厂家进货记录")
    public AjaxResult purchases(@PathVariable Long supplierId)
    {
        return success(supplierService.selectPurchases(supplierId));
    }

    @PreAuthorize("@ss.hasPermi('pms:supplier:add')")
    @Log(title = "供应商", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "新增厂家")
    public AjaxResult add(@Validated @RequestBody PmsSupplier supplier)
    {
        supplier.setCreateBy(getUsername());
        return toAjax(supplierService.insertSupplier(supplier));
    }

    @PreAuthorize("@ss.hasPermi('pms:supplier:edit')")
    @Log(title = "供应商", businessType = BusinessType.UPDATE)
    @PutMapping
    @Operation(summary = "编辑厂家")
    public AjaxResult edit(@Validated @RequestBody PmsSupplier supplier)
    {
        supplier.setUpdateBy(getUsername());
        return toAjax(supplierService.updateSupplier(supplier));
    }

    @PreAuthorize("@ss.hasPermi('pms:supplier:remove')")
    @Log(title = "供应商", businessType = BusinessType.DELETE)
    @DeleteMapping("/{supplierIds}")
    @Operation(summary = "删除厂家")
    public AjaxResult remove(@PathVariable Long[] supplierIds)
    {
        return toAjax(supplierService.deleteSupplierByIds(supplierIds));
    }
}
