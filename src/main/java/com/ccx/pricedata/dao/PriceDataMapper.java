package com.ccx.pricedata.dao;

import java.util.List;

import com.ccx.pricedata.model.PriceData;
import com.ccx.util.page.Page;

public interface PriceDataMapper {
    PriceData selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(PriceData record);
    
    List<PriceData> findAll(Page<PriceData> page);
}