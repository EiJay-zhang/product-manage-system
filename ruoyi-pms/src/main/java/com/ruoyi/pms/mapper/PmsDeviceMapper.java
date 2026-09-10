package com.ruoyi.pms.mapper;

import java.util.List;
import com.ruoyi.pms.domain.PmsDevice;

public interface PmsDeviceMapper
{
    public PmsDevice selectDeviceById(Long deviceId);
    public PmsDevice selectDeviceBySn(String sn);
    public List<PmsDevice> selectDeviceList(PmsDevice device);
    public int insertDevice(PmsDevice device);
    public int updateDevice(PmsDevice device);
    public int countByProductId(Long productId);
    public List<PmsDevice> selectByProductIds(Long[] productIds);
}
