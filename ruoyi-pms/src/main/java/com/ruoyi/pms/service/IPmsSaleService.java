package com.ruoyi.pms.service;

import java.util.List;
import com.ruoyi.pms.domain.PmsSale;

public interface IPmsSaleService
{
    public PmsSale selectSaleById(Long saleId);
    public List<PmsSale> selectSaleList(PmsSale sale);
    public int insertSale(PmsSale sale);
}
