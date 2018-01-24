package com.ccx.index.service.impl;	
	
import com.ccx.index.model.AbsIndexRule;	
import com.ccx.index.service.AbsIndexRuleService;	
import com.ccx.index.dao.AbsIndexRuleMapper;	
import com.ccx.util.page.Page;	
import org.springframework.beans.factory.annotation.Autowired;	
import org.springframework.stereotype.Service;	
import java.util.*;	
	
@Service	
public class AbsIndexRuleServiceImpl implements AbsIndexRuleService {	
		
	@Autowired	
    private AbsIndexRuleMapper absIndexRuleMapper;	
		
	//主键获取	
	@Override	
	public AbsIndexRule getById(Integer id) {	
		return absIndexRuleMapper.selectByPrimaryKey(id);	
	}	
		
	//获取无参list	
	@Override	
	public List<AbsIndexRule> getList() {	
		return null;	
	}	
		
	//获取有参数list	
	@Override	
	public List<AbsIndexRule> getList(AbsIndexRule model) {	
		return null;	
	}	
		
	//获取带分页list	
	@Override	
	public Page<AbsIndexRule> getPageList(Page<AbsIndexRule> page) {	
		return null;	
	}	
		
	//通过条件获取	
	@Override	
	public AbsIndexRule getByModel(AbsIndexRule model) {	
		return null;	
	}	
	
	//保存对象	
	@Override	
	public int save(AbsIndexRule model) {	
		return absIndexRuleMapper.insert(model);	
	}	
	
	//更新对象	
	@Override	
	public int update(AbsIndexRule model) {	
		return absIndexRuleMapper.updateByPrimaryKey(model);	
	}	
		
	//删除对象	
	@Override	
	public int deleteById(Integer id) {	
		return absIndexRuleMapper.deleteByPrimaryKey(id);	
	}	
		
	//其他查询	
	@Override	
	public Map<String, Object> getOther() {	
		return null;	
	}	
}	
