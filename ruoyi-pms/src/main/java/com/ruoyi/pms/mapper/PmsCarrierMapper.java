package com.ruoyi.pms.mapper;

import java.util.List;
import com.ruoyi.pms.domain.PmsCarrier;

public interface PmsCarrierMapper
{
    public PmsCarrier selectCarrierById(Long carrierId);

    public List<PmsCarrier> selectCarrierList(PmsCarrier carrier);

    public PmsCarrier checkCarrierNameUnique(String carrierName);

    public int insertCarrier(PmsCarrier carrier);

    public int updateCarrier(PmsCarrier carrier);

    public int deleteCarrierByIds(Long[] carrierIds);

    public int countLogisticsByCarrierId(Long carrierId);
}
