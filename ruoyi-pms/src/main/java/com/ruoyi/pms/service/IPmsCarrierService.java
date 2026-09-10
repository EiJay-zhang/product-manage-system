package com.ruoyi.pms.service;

import java.util.List;
import com.ruoyi.pms.domain.PmsCarrier;
import com.ruoyi.pms.domain.PmsLogistics;

public interface IPmsCarrierService
{
    public PmsCarrier selectCarrierById(Long carrierId);

    public List<PmsCarrier> selectCarrierList(PmsCarrier carrier);

    public int insertCarrier(PmsCarrier carrier);

    public int updateCarrier(PmsCarrier carrier);

    public int deleteCarrierByIds(Long[] carrierIds);

    public List<PmsLogistics> selectLogistics(Long carrierId);
}
