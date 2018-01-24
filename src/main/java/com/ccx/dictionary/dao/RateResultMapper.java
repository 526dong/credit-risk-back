package com.ccx.dictionary.dao;

import java.util.List;

import com.ccx.dictionary.model.RateResult;
import com.ccx.util.page.Page;

public interface RateResultMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RateResult record);
    
    int insertSelective(RateResult record);

    RateResult selectByPrimaryKey(Integer id);
    
    //name获取	
    RateResult getByName(String name);

    int updateByPrimaryKeySelective(RateResult record);

    int updateByPrimaryKey(RateResult record);
    
    //无参
    List<RateResult> findAllRateResult();
    
    //有参，分页
    List<RateResult> findAll(Page<RateResult> page);
}