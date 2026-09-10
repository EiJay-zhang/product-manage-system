package com.ruoyi.pms.service.impl;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.pms.constant.PmsConstants;
import com.ruoyi.pms.domain.PmsProduct;
import com.ruoyi.pms.domain.PmsStockLog;
import com.ruoyi.pms.mapper.PmsProductMapper;
import com.ruoyi.pms.mapper.PmsStockLogMapper;
import com.ruoyi.pms.service.IPmsInventoryService;
import com.ruoyi.pms.service.IPmsStockLogService;

@Service
public class PmsStockLogServiceImpl implements IPmsStockLogService
{
    private static final List<String> ADJUST_TYPES = Arrays.asList(
        PmsConstants.STOCK_CHECK, PmsConstants.STOCK_LOSS, PmsConstants.STOCK_TRANSFER);

    @Autowired
    private PmsStockLogMapper stockLogMapper;
    @Autowired
    private PmsProductMapper productMapper;
    @Autowired
    private IPmsInventoryService inventoryService;

    @Override
    public List<PmsStockLog> selectStockLogList(PmsStockLog log)
    {
        return stockLogMapper.selectStockLogList(log);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int adjust(PmsStockLog log)
    {
        if (log.getProductId() == null)
        {
            throw new ServiceException("请选择商品");
        }
        if (log.getAfterQtyTarget() == null)
        {
            throw new ServiceException("请填写调整后库存");
        }
        String type = StringUtils.isEmpty(log.getChangeType()) ? PmsConstants.STOCK_CHECK : log.getChangeType();
        if (!ADJUST_TYPES.contains(type))
        {
            throw new ServiceException("库存调整类型仅支持盘点、损耗、调拨");
        }
        PmsProduct product = productMapper.selectProductById(log.getProductId());
        if (product == null)
        {
            throw new ServiceException("商品不存在");
        }
        inventoryService.changeStock(product, log.getAfterQtyTarget(), type, "adjust", product.getProductId(), log.getRemark(), log.getCreateBy());
        return 1;
    }
}
