package com.ruoyi.pms.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
import com.ruoyi.pms.domain.PmsSale;
import com.ruoyi.pms.service.IPmsSaleService;

@Tag(name = "销售出库")
@RestController
@RequestMapping("/pms/sale")
public class PmsSaleController extends BaseController
{
    @Autowired
    private IPmsSaleService saleService;

    @PreAuthorize("@ss.hasPermi('pms:sale:list')")
    @GetMapping("/list")
    @Operation(summary = "销售出库列表")
    public TableDataInfo list(PmsSale sale)
    {
        startPage();
        return getDataTable(saleService.selectSaleList(sale));
    }

    @Log(title = "销售出库", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('pms:sale:export')")
    @PostMapping("/export")
    @Operation(summary = "导出销售记录")
    public void export(HttpServletResponse response, PmsSale sale)
    {
        List<PmsSale> list = saleService.selectSaleList(sale);
        new ExcelUtil<PmsSale>(PmsSale.class).exportExcel(response, list, "销售出库");
    }

    @PreAuthorize("@ss.hasPermi('pms:sale:query')")
    @GetMapping("/{saleId}")
    @Operation(summary = "销售单详情")
    public AjaxResult getInfo(@PathVariable Long saleId)
    {
        return success(saleService.selectSaleById(saleId));
    }

    @PreAuthorize("@ss.hasPermi('pms:sale:add')")
    @Log(title = "销售出库", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "销售出库")
    public AjaxResult add(@Validated @RequestBody PmsSale sale)
    {
        sale.setCreateBy(getUsername());
        return toAjax(saleService.insertSale(sale));
    }

    @PreAuthorize("@ss.hasPermi('pms:sale:void')")
    @Log(title = "销售退货", businessType = BusinessType.UPDATE)
    @PutMapping("/{saleId}/void")
    @Operation(summary = "销售退货")
    public AjaxResult voidBill(@PathVariable Long saleId)
    {
        return toAjax(saleService.voidSale(saleId, getUsername()));
    }
}
