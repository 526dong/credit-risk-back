package com.ccx.index.service;	
	
import com.ccx.index.model.AbsIndexRule;	
import com.ccx.util.page.Page;	
import java.util.*;	
	
public interface AbsIndexRuleService {	
		
	//主键获取	
	AbsIndexRule getById(Integer id);	
		
	//获取无参list	
	List<AbsIndexRule> getList();	
		
	//获取有参数list	
	List<AbsIndexRule> getList(AbsIndexRule model);	
		
	//获取带分页list	
	Page<AbsIndexRule> getPageList(Page<AbsIndexRule> page);	
		
	//通过条件获取	
	AbsIndexRule getByModel(AbsIndexRule model);	
	
	//保存对象	
	int save(AbsIndexRule model);	
	
	//更新对象	
	int update(AbsIndexRule model);	
		
	//删除对象	
	int deleteById(Integer id);	
		
	//其他查询	
	Map<String, Object> getOther();	
}	
