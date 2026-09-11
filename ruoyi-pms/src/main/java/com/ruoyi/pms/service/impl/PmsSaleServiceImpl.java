package com.ruoyi.pms.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.pms.constant.PmsConstants;
import com.ruoyi.pms.domain.PmsProduct;
import com.ruoyi.pms.domain.PmsSale;
import com.ruoyi.pms.mapper.PmsProductMapper;
import com.ruoyi.pms.mapper.PmsSaleMapper;
import com.ruoyi.pms.service.IPmsInventoryService;
import com.ruoyi.pms.service.IPmsSaleService;
import com.ruoyi.pms.util.PmsNoUtils;

@Service
public class PmsSaleServiceImpl implements IPmsSaleService
{
    @Autowired
    private PmsSaleMapper saleMapper;
    @Autowired
    private PmsProductMapper productMapper;
    @Autowired
    private IPmsInventoryService inventoryService;

    @Override
    public PmsSale selectSaleById(Long saleId)
    {
        return saleMapper.selectSaleById(saleId);
    }

    @Override
    public List<PmsSale> selectSaleList(PmsSale sale)
    {
        return saleMapper.selectSaleList(sale);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSale(PmsSale sale)
    {
        if (sale.getQty() == null || sale.getQty() <= 0)
        {
            throw new ServiceException("销售数量必须为正整数");
        }
        PmsProduct product = productMapper.selectProductById(sale.getProductId());
        if (product == null || PmsConstants.DEL_REMOVED.equals(product.getDelFlag()))
        {
            throw new ServiceException("商品不存在");
        }
        int stock = product.getStockQty() == null ? 0 : product.getStockQty();
        if (stock < sale.getQty())
        {
            throw new ServiceException("库存不足，当前库存 " + stock);
        }
        if (sale.getSalePrice() == null)
        {
            sale.setSalePrice(product.getSalePrice());
        }
        if (sale.getSalePrice().compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new ServiceException("售价必须为正数");
        }
        if (sale.getSaleTime() == null)
        {
            sale.setSaleTime(new Date());
        }
        String prefix = "OUT" + DateUtils.parseDateToStr("yyyyMMdd", new Date());
        sale.setSaleNo(PmsNoUtils.next("OUT", saleMapper.selectMaxSaleNo(prefix)));
        sale.setProductName(product.getProductName());
        sale.setSpec(product.getSpec());
        sale.setCategoryId(product.getCategoryId());
        sale.setAmount(sale.getSalePrice().multiply(new BigDecimal(sale.getQty())));
        sale.setStatus(PmsConstants.BILL_NORMAL);
        int rows = saleMapper.insertSale(sale);
        inventoryService.changeStock(product, stock - sale.getQty(), PmsConstants.STOCK_SALE, "sale", sale.getSaleId(), "销售出库", sale.getCreateBy());
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int voidSale(Long saleId, String operator)
    {
        PmsSale bill = saleMapper.selectSaleById(saleId);
        if (bill == null)
        {
            throw new ServiceException("销售单不存在");
        }
        if (PmsConstants.BILL_VOID.equals(bill.getStatus()))
        {
            throw new ServiceException("销售单已退货");
        }
        PmsProduct product = productMapper.selectProductById(bill.getProductId());
        if (product == null || PmsConstants.DEL_REMOVED.equals(product.getDelFlag()))
        {
            throw new ServiceException("商品不存在");
        }
        int stock = product.getStockQty() == null ? 0 : product.getStockQty();
        inventoryService.changeStock(product, stock + bill.getQty(), PmsConstants.STOCK_RETURN, "sale", bill.getSaleId(),
            "销售退货 " + bill.getSaleNo(), operator);
        bill.setStatus(PmsConstants.BILL_VOID);
        return saleMapper.updateSaleStatus(bill);
    }
}
