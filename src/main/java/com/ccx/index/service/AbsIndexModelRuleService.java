package com.ccx.index.service;	
	
import com.ccx.index.model.AbsIndexModelRule;	
import com.ccx.util.page.Page;	
import java.util.*;	
	
public interface AbsIndexModelRuleService {	
		
	//主键获取	
	AbsIndexModelRule getById(Integer id);	
		
	//获取无参list	
	List<AbsIndexModelRule> getList();	
		
	//获取有参数list	
	List<AbsIndexModelRule> getList(AbsIndexModelRule model);	
		
	//获取带分页list	
	Page<AbsIndexModelRule> getPageList(Page<AbsIndexModelRule> page);	
		
	//通过条件获取	
	AbsIndexModelRule getByModel(AbsIndexModelRule model);	
	
	//保存对象	
	int save(AbsIndexModelRule model);	
	
	//更新对象	
	int update(AbsIndexModelRule model);	
		
	//删除对象	
	int deleteById(Integer id);	
		
	//其他查询	
	Map<String, Object> getOther();	
}	
