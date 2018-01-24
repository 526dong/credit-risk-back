package com.ccx.dictionary.service;	
	
import java.util.List;
import java.util.Map;

import com.ccx.dictionary.model.RateResult;
import com.ccx.util.page.Page;	
	
public interface RateResultService {	
		
	//主键获取	
	RateResult getById(Integer id);	
	
	//name获取	
	RateResult getByName(String name);
		
	//获取无参list	
	List<RateResult> getList();	
		
	//获取有参数list	
	List<RateResult> getList(RateResult model);	
		
	//获取带分页list	
	Page<RateResult> getPageList(Page<RateResult> page);	
		
	//通过条件获取	
	RateResult getByModel(RateResult model);	
	
	//保存对象	
	int save(RateResult model);	
	
	//更新对象	
	int update(RateResult model);	
		
	//删除对象	
	int deleteById(Integer id);	
		
	//其他查询	
	Map<String, Object> getOther();	
}	
