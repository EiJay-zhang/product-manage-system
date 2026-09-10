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
import com.ruoyi.pms.domain.PmsCarrier;
import com.ruoyi.pms.service.IPmsCarrierService;

@Tag(name = "物流商")
@RestController
@RequestMapping("/pms/carrier")
public class PmsCarrierController extends BaseController
{
    @Autowired
    private IPmsCarrierService carrierService;

    @PreAuthorize("@ss.hasPermi('pms:carrier:list')")
    @GetMapping("/list")
    @Operation(summary = "物流商列表")
    public TableDataInfo list(PmsCarrier carrier)
    {
        startPage();
        return getDataTable(carrierService.selectCarrierList(carrier));
    }

    @PreAuthorize("@ss.hasPermi('pms:carrier:query')")
    @GetMapping("/optionselect")
    @Operation(summary = "物流商下拉")
    public AjaxResult optionselect(PmsCarrier carrier)
    {
        if (carrier.getStatus() == null)
        {
            carrier.setStatus("0");
        }
        return success(carrierService.selectCarrierList(carrier));
    }

    @Log(title = "物流商", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('pms:carrier:export')")
    @PostMapping("/export")
    @Operation(summary = "导出物流商")
    public void export(HttpServletResponse response, PmsCarrier carrier)
    {
        List<PmsCarrier> list = carrierService.selectCarrierList(carrier);
        new ExcelUtil<PmsCarrier>(PmsCarrier.class).exportExcel(response, list, "物流商数据");
    }

    @PreAuthorize("@ss.hasPermi('pms:carrier:query')")
    @GetMapping("/{carrierId}")
    @Operation(summary = "物流商详情")
    public AjaxResult getInfo(@PathVariable Long carrierId)
    {
        return success(carrierService.selectCarrierById(carrierId));
    }

    @PreAuthorize("@ss.hasPermi('pms:carrier:query')")
    @GetMapping("/{carrierId}/logistics")
    @Operation(summary = "物流商关联账单")
    public AjaxResult logistics(@PathVariable Long carrierId)
    {
        return success(carrierService.selectLogistics(carrierId));
    }

    @PreAuthorize("@ss.hasPermi('pms:carrier:add')")
    @Log(title = "物流商", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "新增物流商")
    public AjaxResult add(@Validated @RequestBody PmsCarrier carrier)
    {
        carrier.setCreateBy(getUsername());
        return toAjax(carrierService.insertCarrier(carrier));
    }

    @PreAuthorize("@ss.hasPermi('pms:carrier:edit')")
    @Log(title = "物流商", businessType = BusinessType.UPDATE)
    @PutMapping
    @Operation(summary = "编辑物流商")
    public AjaxResult edit(@Validated @RequestBody PmsCarrier carrier)
    {
        carrier.setUpdateBy(getUsername());
        return toAjax(carrierService.updateCarrier(carrier));
    }

    @PreAuthorize("@ss.hasPermi('pms:carrier:remove')")
    @Log(title = "物流商", businessType = BusinessType.DELETE)
    @DeleteMapping("/{carrierIds}")
    @Operation(summary = "删除物流商")
    public AjaxResult remove(@PathVariable Long[] carrierIds)
    {
        return toAjax(carrierService.deleteCarrierByIds(carrierIds));
    }
}
