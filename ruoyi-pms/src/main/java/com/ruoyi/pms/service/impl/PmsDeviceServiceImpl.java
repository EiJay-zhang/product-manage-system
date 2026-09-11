package com.ruoyi.pms.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.pms.constant.PmsConstants;
import com.ruoyi.pms.domain.PmsDevice;
import com.ruoyi.pms.domain.PmsDeviceSyncLog;
import com.ruoyi.pms.domain.PmsProduct;
import com.ruoyi.pms.domain.PmsSetting;
import com.ruoyi.pms.domain.vo.PmsEinkContentVo;
import com.ruoyi.pms.mapper.PmsDeviceMapper;
import com.ruoyi.pms.mapper.PmsDeviceSyncLogMapper;
import com.ruoyi.pms.mapper.PmsProductMapper;
import com.ruoyi.pms.mapper.PmsSettingMapper;
import com.ruoyi.pms.service.IPmsDeviceService;

@Service
public class PmsDeviceServiceImpl implements IPmsDeviceService
{
    @Autowired
    private PmsDeviceMapper deviceMapper;
    @Autowired
    private PmsDeviceSyncLogMapper syncLogMapper;
    @Autowired
    private PmsProductMapper productMapper;
    @Autowired
    private PmsSettingMapper settingMapper;

    @Override
    public PmsDevice selectDeviceById(Long deviceId)
    {
        return refreshOnline(deviceMapper.selectDeviceById(deviceId));
    }

    @Override
    public List<PmsDevice> selectDeviceList(PmsDevice device)
    {
        List<PmsDevice> list = deviceMapper.selectDeviceList(device);
        for (PmsDevice item : list)
        {
            refreshOnline(item);
        }
        return list;
    }

    private PmsDevice refreshOnline(PmsDevice device)
    {
        if (device == null || device.getLastHeartbeat() == null)
        {
            if (device != null)
            {
                device.setOnlineStatus(PmsConstants.ONLINE_OFF);
            }
            return device;
        }
        long age = System.currentTimeMillis() - device.getLastHeartbeat().getTime();
        if (age > PmsConstants.HEARTBEAT_TIMEOUT_SEC * 1000L)
        {
            device.setOnlineStatus(PmsConstants.ONLINE_OFF);
        }
        return device;
    }

    @Override
    public int bind(PmsDevice device)
    {
        if (StringUtils.isEmpty(device.getSn()))
        {
            throw new ServiceException("设备SN不能为空");
        }
        PmsDevice exist = deviceMapper.selectDeviceBySn(device.getSn());
        Date now = DateUtils.getNowDate();
        if (exist == null)
        {
            device.setOnlineStatus(PmsConstants.ONLINE_OFF);
            device.setAbnormalFlag(PmsConstants.FLAG_NO);
            if (StringUtils.isEmpty(device.getDeviceCode()))
            {
                device.setDeviceCode(device.getSn());
            }
            if (device.getProductId() != null)
            {
                device.setBindTime(now);
            }
            deviceMapper.insertDevice(device);
            exist = device;
        }
        else
        {
            exist.setProductId(device.getProductId());
            exist.setShelfNo(device.getShelfNo());
            exist.setDeviceCode(StringUtils.isEmpty(device.getDeviceCode()) ? exist.getDeviceCode() : device.getDeviceCode());
            exist.setBindTime(device.getProductId() == null ? exist.getBindTime() : now);
            exist.setUpdateBy(device.getCreateBy());
            deviceMapper.updateDevice(exist);
            device.setDeviceId(exist.getDeviceId());
        }
        if (exist.getProductId() != null)
        {
            push(exist.getDeviceId(), "refresh", exist.getCreateBy() == null ? device.getCreateBy() : exist.getCreateBy());
        }
        return 1;
    }

    @Override
    public int unbind(Long deviceId, String operator)
    {
        PmsDevice device = require(deviceId);
        device.setProductId(null);
        device.setUpdateBy(operator);
        deviceMapper.updateDevice(device);
        push(deviceId, "unbind", operator);
        return 1;
    }

    @Override
    public int refresh(Long deviceId, String operator)
    {
        return push(deviceId, "refresh", operator);
    }

    @Override
    public int restart(Long deviceId, String operator)
    {
        return push(deviceId, "restart", operator);
    }

    @Override
    public int mark(PmsDevice device)
    {
        PmsDevice exist = require(device.getDeviceId());
        exist.setAbnormalFlag(device.getAbnormalFlag());
        exist.setAbnormalRemark(device.getAbnormalRemark());
        exist.setUpdateBy(device.getUpdateBy());
        return deviceMapper.updateDevice(exist);
    }

    @Override
    public int batchRefresh(PmsDevice device, String operator)
    {
        List<PmsDevice> targets = new ArrayList<PmsDevice>();
        if (device.getDeviceIds() != null && device.getDeviceIds().length > 0)
        {
            for (Long id : device.getDeviceIds())
            {
                PmsDevice one = deviceMapper.selectDeviceById(id);
                if (one != null)
                {
                    targets.add(one);
                }
            }
        }
        else if (device.getProductIds() != null && device.getProductIds().length > 0)
        {
            targets.addAll(deviceMapper.selectByProductIds(device.getProductIds()));
        }
        for (PmsDevice item : targets)
        {
            push(item.getDeviceId(), "refresh", operator);
        }
        return targets.size();
    }

    @Override
    public void refreshByProductId(Long productId, String command)
    {
        if (productId == null)
        {
            return;
        }
        List<PmsDevice> list = deviceMapper.selectByProductIds(new Long[] { productId });
        for (PmsDevice device : list)
        {
            push(device.getDeviceId(), command, "system");
        }
    }

    private int push(Long deviceId, String command, String operator)
    {
        PmsDevice device = require(deviceId);
        refreshOnline(device);
        PmsEinkContentVo content = buildContent(device);
        String json = JSON.toJSONString(content);
        PmsDeviceSyncLog log = new PmsDeviceSyncLog();
        log.setDeviceId(device.getDeviceId());
        log.setSn(device.getSn());
        log.setProductId(device.getProductId());
        log.setSyncContent(json);
        log.setCommandType(command);
        log.setRemark(operator);
        if (PmsConstants.ONLINE_ON.equals(device.getOnlineStatus()))
        {
            log.setSyncStatus(PmsConstants.SYNC_OK);
        }
        else
        {
            log.setSyncStatus(PmsConstants.SYNC_PENDING);
        }
        syncLogMapper.insertSyncLog(log);
        device.setLastSyncTime(DateUtils.getNowDate());
        device.setUpdateBy(operator);
        deviceMapper.updateDevice(device);
        return 1;
    }

    @Override
    public List<PmsDeviceSyncLog> selectSyncLogList(PmsDeviceSyncLog log)
    {
        return syncLogMapper.selectSyncLogList(log);
    }

    @Override
    public PmsSetting getTemplate()
    {
        return settingMapper.selectSetting();
    }

    @Override
    public int saveTemplate(PmsSetting setting)
    {
        setting.setShowName(defaultY(setting.getShowName(), PmsConstants.YES));
        setting.setShowSpec(defaultY(setting.getShowSpec(), PmsConstants.YES));
        setting.setShowSalePrice(defaultY(setting.getShowSalePrice(), PmsConstants.YES));
        setting.setShowIntro(defaultY(setting.getShowIntro(), PmsConstants.YES));
        setting.setShowStock(defaultY(setting.getShowStock(), PmsConstants.NO));
        setting.setShowSupplier(defaultY(setting.getShowSupplier(), PmsConstants.NO));
        return settingMapper.updateSetting(setting);
    }

    private String defaultY(String val, String def)
    {
        return StringUtils.isEmpty(val) ? def : val;
    }

    @Override
    public PmsDevice heartbeat(PmsDevice device)
    {
        if (StringUtils.isEmpty(device.getSn()))
        {
            throw new ServiceException("SN不能为空");
        }
        PmsDevice exist = deviceMapper.selectDeviceBySn(device.getSn());
        Date now = DateUtils.getNowDate();
        if (exist == null)
        {
            exist = new PmsDevice();
            exist.setSn(device.getSn());
            exist.setDeviceCode(device.getSn());
            exist.setOnlineStatus(PmsConstants.ONLINE_ON);
            exist.setLastHeartbeat(now);
            exist.setBattery(device.getBattery());
            exist.setFirmware(device.getFirmware());
            exist.setAbnormalFlag(PmsConstants.FLAG_NO);
            exist.setCreateBy("device");
            deviceMapper.insertDevice(exist);
            return exist;
        }
        exist.setOnlineStatus(PmsConstants.ONLINE_ON);
        exist.setLastHeartbeat(now);
        exist.setBattery(device.getBattery());
        exist.setFirmware(device.getFirmware());
        exist.setUpdateBy("device");
        deviceMapper.updateDevice(exist);
        return exist;
    }

    @Override
    public PmsEinkContentVo content(String sn)
    {
        PmsDevice device = deviceMapper.selectDeviceBySn(sn);
        if (device == null)
        {
            throw new ServiceException("设备未注册");
        }
        return buildContent(device);
    }

    private void fillPollAndPending(PmsDevice device, PmsEinkContentVo vo)
    {
        PmsSetting tpl = settingMapper.selectSetting();
        Integer configured = tpl == null ? null : tpl.getEinkSyncIntervalSec();
        vo.setPollIntervalSec(Integer.valueOf(PmsConstants.resolvePollIntervalSec(configured)));
        PmsDeviceSyncLog pending = syncLogMapper.selectLatestPendingBySn(device.getSn());
        if (pending != null)
        {
            vo.setPendingCommand(pending.getCommandType());
            vo.setPendingLogId(pending.getLogId());
        }
    }

    private PmsEinkContentVo buildContent(PmsDevice device)
    {
        PmsSetting tpl = settingMapper.selectSetting();
        PmsEinkContentVo vo = new PmsEinkContentVo();
        vo.setSn(device.getSn());
        vo.setFontStyle(tpl == null ? "default" : tpl.getEinkFontStyle());
        if (device.getProductId() == null)
        {
            vo.setEmpty("Y");
            fillPollAndPending(device, vo);
            return vo;
        }
        PmsProduct product = productMapper.selectProductById(device.getProductId());
        if (product == null)
        {
            vo.setEmpty("Y");
            fillPollAndPending(device, vo);
            return vo;
        }
        if (tpl == null || PmsConstants.YES.equals(tpl.getShowName()))
        {
            vo.setProductName(product.getProductName());
        }
        if (tpl == null || PmsConstants.YES.equals(tpl.getShowSpec()))
        {
            vo.setSpec(product.getSpec());
        }
        if (tpl == null || PmsConstants.YES.equals(tpl.getShowSalePrice()))
        {
            vo.setSalePrice(product.getSalePrice());
        }
        if (tpl == null || PmsConstants.YES.equals(tpl.getShowIntro()))
        {
            vo.setIntro(product.getIntro());
        }
        if (tpl != null && PmsConstants.YES.equals(tpl.getShowStock()))
        {
            vo.setStockQty(product.getStockQty());
        }
        if (tpl != null && PmsConstants.YES.equals(tpl.getShowSupplier()))
        {
            vo.setSupplierName(product.getSupplierName());
        }
        vo.setEmpty("N");
        fillPollAndPending(device, vo);
        return vo;
    }

    @Override
    public int ack(PmsDeviceSyncLog log)
    {
        if (log.getLogId() == null && StringUtils.isEmpty(log.getSn()))
        {
            throw new ServiceException("回执缺少设备信息");
        }
        if (log.getLogId() != null)
        {
            return syncLogMapper.updateSyncLogStatus(log);
        }
        PmsDeviceSyncLog q = new PmsDeviceSyncLog();
        q.setSn(log.getSn());
        q.setSyncStatus(PmsConstants.SYNC_PENDING);
        List<PmsDeviceSyncLog> list = syncLogMapper.selectSyncLogList(q);
        if (list.isEmpty())
        {
            return 0;
        }
        PmsDeviceSyncLog last = list.get(0);
        last.setSyncStatus(log.getSyncStatus());
        last.setRemark(log.getRemark());
        return syncLogMapper.updateSyncLogStatus(last);
    }

    private PmsDevice require(Long deviceId)
    {
        PmsDevice device = deviceMapper.selectDeviceById(deviceId);
        if (device == null)
        {
            throw new ServiceException("设备不存在");
        }
        return device;
    }
}
