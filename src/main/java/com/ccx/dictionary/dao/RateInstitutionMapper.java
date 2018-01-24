package com.ccx.dictionary.dao;

import java.util.List;

import com.ccx.dictionary.model.RateInstitution;
import com.ccx.util.page.Page;

public interface RateInstitutionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RateInstitution record);

    int insertSelective(RateInstitution record);

    RateInstitution selectByPrimaryKey(Integer id);
    
    //name获取	
  	RateInstitution findByName(String name);

  	//校验名称
    int findCountByName(String name);

    //校验统一信用代码
    int findByCode(String code);

    int updateByPrimaryKeySelective(RateInstitution record);

    int updateByPrimaryKey(RateInstitution record);
    
    //无参
    List<RateInstitution> findAllRateInstitution();
    
    //有参，分页
    List<RateInstitution> findAll(Page<RateInstitution> page);
}