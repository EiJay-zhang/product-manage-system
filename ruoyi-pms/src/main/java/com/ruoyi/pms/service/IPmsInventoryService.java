package com.ruoyi.pms.service;

import com.ruoyi.pms.domain.PmsProduct;

public interface IPmsInventoryService
{
    public void changeStock(PmsProduct product, int afterQty, String changeType, String bizType, Long bizId, String remark, String operator);
}
