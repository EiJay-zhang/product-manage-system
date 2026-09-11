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
import com.ruoyi.pms.domain.PmsPurchase;
import com.ruoyi.pms.service.IPmsPurchaseService;

@Tag(name = "进货入库")
@RestController
@RequestMapping("/pms/purchase")
public class PmsPurchaseController extends BaseController
{
    @Autowired
    private IPmsPurchaseService purchaseService;

    @PreAuthorize("@ss.hasPermi('pms:purchase:list')")
    @GetMapping("/list")
    @Operation(summary = "进货台账列表")
    public TableDataInfo list(PmsPurchase purchase)
    {
        startPage();
        return getDataTable(purchaseService.selectPurchaseList(purchase));
    }

    @Log(title = "进货台账", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('pms:purchase:export')")
    @PostMapping("/export")
    @Operation(summary = "导出进货台账")
    public void export(HttpServletResponse response, PmsPurchase purchase)
    {
        List<PmsPurchase> list = purchaseService.selectPurchaseList(purchase);
        new ExcelUtil<PmsPurchase>(PmsPurchase.class).exportExcel(response, list, "进货台账");
    }

    @PreAuthorize("@ss.hasPermi('pms:purchase:query')")
    @GetMapping("/{purchaseId}")
    @Operation(summary = "进货单详情")
    public AjaxResult getInfo(@PathVariable Long purchaseId)
    {
        return success(purchaseService.selectPurchaseById(purchaseId));
    }

    @PreAuthorize("@ss.hasPermi('pms:purchase:add')")
    @Log(title = "进货入库", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "进货入库")
    public AjaxResult add(@Validated @RequestBody PmsPurchase purchase)
    {
        purchase.setCreateBy(getUsername());
        return toAjax(purchaseService.insertPurchase(purchase));
    }

    @PreAuthorize("@ss.hasPermi('pms:purchase:void')")
    @Log(title = "进货作废", businessType = BusinessType.UPDATE)
    @PutMapping("/{purchaseId}/void")
    @Operation(summary = "进货作废")
    public AjaxResult voidBill(@PathVariable Long purchaseId)
    {
        return toAjax(purchaseService.voidPurchase(purchaseId, getUsername()));
    }
}
