package com.ruoyi.pms.service.impl;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.pms.constant.PmsConstants;
import com.ruoyi.pms.domain.PmsCarrier;
import com.ruoyi.pms.domain.PmsLogistics;
import com.ruoyi.pms.domain.PmsPurchase;
import com.ruoyi.pms.mapper.PmsCarrierMapper;
import com.ruoyi.pms.domain.vo.PmsLogisticsStatsVo;
import com.ruoyi.pms.domain.vo.PmsReportQuery;
import com.ruoyi.pms.mapper.PmsLogisticsMapper;
import com.ruoyi.pms.mapper.PmsPurchaseMapper;
import com.ruoyi.pms.service.IPmsLogisticsService;
import com.ruoyi.pms.util.PmsDateRange;

@Service
public class PmsLogisticsServiceImpl implements IPmsLogisticsService
{
    @Autowired
    private PmsLogisticsMapper logisticsMapper;
    @Autowired
    private PmsPurchaseMapper purchaseMapper;
    @Autowired
    private PmsCarrierMapper carrierMapper;

    @Override
    public PmsLogistics selectLogisticsById(Long logisticsId)
    {
        return logisticsMapper.selectLogisticsById(logisticsId);
    }

    @Override
    public List<PmsLogistics> selectLogisticsList(PmsLogistics logistics)
    {
        return logisticsMapper.selectLogisticsList(logistics);
    }

    private BigDecimal nvl(BigDecimal v)
    {
        return v == null ? BigDecimal.ZERO : v;
    }

    private void fillFee(PmsLogistics logistics)
    {
        logistics.setFreight(nvl(logistics.getFreight()));
        logistics.setInsuranceFee(nvl(logistics.getInsuranceFee()));
        logistics.setOtherFee(nvl(logistics.getOtherFee()));
        logistics.setTotalFee(logistics.getFreight().add(logistics.getInsuranceFee()).add(logistics.getOtherFee()));
    }

    private void fillPurchase(PmsLogistics logistics)
    {
        PmsPurchase purchase = purchaseMapper.selectPurchaseById(logistics.getPurchaseId());
        if (purchase == null)
        {
            throw new ServiceException("进货批次不存在");
        }
        logistics.setProductId(purchase.getProductId());
        logistics.setSupplierId(purchase.getSupplierId());
    }

    private void fillCarrier(PmsLogistics logistics)
    {
        if (logistics.getCarrierId() == null)
        {
            return;
        }
        PmsCarrier carrier = carrierMapper.selectCarrierById(logistics.getCarrierId());
        if (carrier == null || PmsConstants.DEL_REMOVED.equals(carrier.getDelFlag()))
        {
            throw new ServiceException("物流商不存在");
        }
        if (!"0".equals(carrier.getStatus()))
        {
            throw new ServiceException("物流商已停用");
        }
        logistics.setCarrier(carrier.getCarrierName());
    }

    private void checkNo(PmsLogistics logistics)
    {
        PmsLogistics exist = logisticsMapper.checkLogisticsNoUnique(logistics.getLogisticsNo());
        if (exist != null && !exist.getLogisticsId().equals(logistics.getLogisticsId()))
        {
            throw new ServiceException("物流单号已存在");
        }
    }

    @Override
    public int insertLogistics(PmsLogistics logistics)
    {
        fillPurchase(logistics);
        fillCarrier(logistics);
        checkNo(logistics);
        fillFee(logistics);
        if (StringUtils.isEmpty(logistics.getPayStatus()))
        {
            logistics.setPayStatus(PmsConstants.PAY_UNCHECKED);
        }
        if (StringUtils.isEmpty(logistics.getAbnormalFlag()))
        {
            logistics.setAbnormalFlag(PmsConstants.FLAG_NO);
        }
        return logisticsMapper.insertLogistics(logistics);
    }

    @Override
    public int updateLogistics(PmsLogistics logistics)
    {
        fillCarrier(logistics);
        fillFee(logistics);
        return logisticsMapper.updateLogistics(logistics);
    }

    @Override
    public int updateStatus(PmsLogistics logistics)
    {
        if (logistics.getIds() == null || logistics.getIds().length == 0)
        {
            throw new ServiceException("请选择账单");
        }
        if (StringUtils.isEmpty(logistics.getPayStatus()))
        {
            throw new ServiceException("请选择付款状态");
        }
        if (PmsConstants.PAY_CHECKED.equals(logistics.getPayStatus()) || PmsConstants.PAY_SETTLED.equals(logistics.getPayStatus()))
        {
            logistics.setReconcileTime(DateUtils.getNowDate());
            logistics.setReconcileBy(logistics.getUpdateBy());
        }
        return logisticsMapper.updateLogisticsStatus(logistics);
    }

    @Override
    public int mark(PmsLogistics logistics)
    {
        logistics.setAbnormalFlag(PmsConstants.FLAG_YES);
        return logisticsMapper.updateLogistics(logistics);
    }

    @Override
    public int batchReconcile(PmsLogistics logistics)
    {
        logistics.setPayStatus(PmsConstants.PAY_CHECKED);
        return updateStatus(logistics);
    }

    private void applyRange(PmsReportQuery query)
    {
        PmsDateRange range = PmsDateRange.of(query.getRange(), query.getBeginTime(), query.getEndTime());
        query.setBeginTime(range.begin);
        query.setEndTime(range.end);
    }

    @Override
    public PmsLogisticsStatsVo monthly(PmsReportQuery query)
    {
        applyRange(query);
        return logisticsMapper.selectMonthlyStats(query);
    }

    @Override
    public List<PmsLogisticsStatsVo> carrier(PmsReportQuery query)
    {
        applyRange(query);
        return logisticsMapper.selectCarrierStats(query);
    }

    @Override
    public List<PmsLogisticsStatsVo> supplier(PmsReportQuery query)
    {
        applyRange(query);
        return logisticsMapper.selectSupplierStats(query);
    }
}
