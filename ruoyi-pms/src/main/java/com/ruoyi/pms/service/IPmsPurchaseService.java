package com.ruoyi.pms.service;

import java.util.List;
import com.ruoyi.pms.domain.PmsPurchase;

public interface IPmsPurchaseService
{
    public PmsPurchase selectPurchaseById(Long purchaseId);
    public List<PmsPurchase> selectPurchaseList(PmsPurchase purchase);
    public int insertPurchase(PmsPurchase purchase);
}
