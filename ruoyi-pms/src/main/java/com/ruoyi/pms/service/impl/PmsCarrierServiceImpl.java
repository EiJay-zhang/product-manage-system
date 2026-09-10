package com.ruoyi.pms.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.pms.domain.PmsCarrier;
import com.ruoyi.pms.domain.PmsLogistics;
import com.ruoyi.pms.mapper.PmsCarrierMapper;
import com.ruoyi.pms.mapper.PmsLogisticsMapper;
import com.ruoyi.pms.service.IPmsCarrierService;

@Service
public class PmsCarrierServiceImpl implements IPmsCarrierService
{
    @Autowired
    private PmsCarrierMapper carrierMapper;
    @Autowired
    private PmsLogisticsMapper logisticsMapper;

    @Override
    public PmsCarrier selectCarrierById(Long carrierId)
    {
        return carrierMapper.selectCarrierById(carrierId);
    }

    @Override
    public List<PmsCarrier> selectCarrierList(PmsCarrier carrier)
    {
        return carrierMapper.selectCarrierList(carrier);
    }

    private void checkNameUnique(PmsCarrier carrier)
    {
        PmsCarrier exist = carrierMapper.checkCarrierNameUnique(carrier.getCarrierName());
        if (exist != null && !exist.getCarrierId().equals(carrier.getCarrierId()))
        {
            throw new ServiceException("物流商名称已存在");
        }
    }

    @Override
    public int insertCarrier(PmsCarrier carrier)
    {
        if (StringUtils.isEmpty(carrier.getStatus()))
        {
            carrier.setStatus("0");
        }
        checkNameUnique(carrier);
        return carrierMapper.insertCarrier(carrier);
    }

    @Override
    public int updateCarrier(PmsCarrier carrier)
    {
        checkNameUnique(carrier);
        return carrierMapper.updateCarrier(carrier);
    }

    @Override
    public int deleteCarrierByIds(Long[] carrierIds)
    {
        for (Long id : carrierIds)
        {
            if (carrierMapper.countLogisticsByCarrierId(id) > 0)
            {
                throw new ServiceException("物流商存在关联物流单，无法删除");
            }
        }
        return carrierMapper.deleteCarrierByIds(carrierIds);
    }

    @Override
    public List<PmsLogistics> selectLogistics(Long carrierId)
    {
        PmsLogistics q = new PmsLogistics();
        q.setCarrierId(carrierId);
        return logisticsMapper.selectLogisticsList(q);
    }
}
