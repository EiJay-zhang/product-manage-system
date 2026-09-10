package com.ruoyi.pms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.pms.domain.PmsProduct;
import com.ruoyi.pms.domain.PmsStockLog;
import com.ruoyi.pms.mapper.PmsProductMapper;
import com.ruoyi.pms.mapper.PmsStockLogMapper;
import com.ruoyi.pms.service.IPmsDeviceService;
import com.ruoyi.pms.service.IPmsInventoryService;

@Service
public class PmsInventoryServiceImpl implements IPmsInventoryService
{
    @Autowired
    private PmsProductMapper productMapper;
    @Autowired
    private PmsStockLogMapper stockLogMapper;
    @Autowired
    @Lazy
    private IPmsDeviceService deviceService;

    @Override
    public void changeStock(PmsProduct product, int afterQty, String changeType, String bizType, Long bizId, String remark, String operator)
    {
        if (afterQty < 0)
        {
            throw new ServiceException("库存数量不可为负数");
        }
        int before = product.getStockQty() == null ? 0 : product.getStockQty();
        PmsProduct upd = new PmsProduct();
        upd.setProductId(product.getProductId());
        upd.setStockQty(afterQty);
        upd.setUpdateBy(operator);
        productMapper.updateProductStock(upd);

        PmsStockLog log = new PmsStockLog();
        log.setProductId(product.getProductId());
        log.setProductName(product.getProductName());
        log.setChangeType(changeType);
        log.setBeforeQty(before);
        log.setChangeQty(afterQty - before);
        log.setAfterQty(afterQty);
        log.setBizType(bizType);
        log.setBizId(bizId);
        log.setCreateBy(operator);
        log.setRemark(remark);
        stockLogMapper.insertStockLog(log);

        product.setStockQty(afterQty);
        deviceService.refreshByProductId(product.getProductId(), "refresh");
    }
}
