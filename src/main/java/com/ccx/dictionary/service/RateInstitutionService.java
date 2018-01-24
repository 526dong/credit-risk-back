package com.ccx.dictionary.service;	
	
import com.ccx.dictionary.model.RateInstitution;
import com.ccx.util.page.Page;

import java.util.*;	
	
public interface RateInstitutionService {	
		
	//主键获取	
	RateInstitution getById(Integer id);	
	
	//name获取	
	RateInstitution getByName(String name);

	//校验唯一
	int findCountByName(String name);

	//校验统一信用代码
	int findByCode(String code);
		
	//获取无参list	
	List<RateInstitution> getList();	
		
	//获取有参数list	
	List<RateInstitution> getList(RateInstitution model);	
		
	//获取带分页list	
	Page<RateInstitution> getPageList(Page<RateInstitution> page);	
		
	//通过条件获取	
	RateInstitution getByModel(RateInstitution model);	
	
	//保存对象	
	int save(RateInstitution model);

	//保证增删改查在同一个事务中(非事务方法)
	int mySaveRateIns(RateInstitution model);
	
	//更新对象	
	int update(RateInstitution model);	
		
	//删除对象	
	int deleteById(Integer id);	
		
	//其他查询	
	Map<String, Object> getOther();	
}	
