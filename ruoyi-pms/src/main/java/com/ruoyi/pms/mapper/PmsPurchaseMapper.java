package com.ruoyi.pms.mapper;

import java.util.List;
import com.ruoyi.pms.domain.PmsPurchase;

public interface PmsPurchaseMapper
{
    public PmsPurchase selectPurchaseById(Long purchaseId);
    public List<PmsPurchase> selectPurchaseList(PmsPurchase purchase);
    public String selectMaxPurchaseNo(String prefix);
    public int insertPurchase(PmsPurchase purchase);
    public int updatePurchaseStatus(PmsPurchase purchase);
}
