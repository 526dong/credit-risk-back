package com.ccx.index.service.impl;	
	
import com.ccx.index.model.AbsIndexModelRule;	
import com.ccx.index.service.AbsIndexModelRuleService;	
import com.ccx.index.dao.AbsIndexModelRuleMapper;	
import com.ccx.util.page.Page;	
import org.springframework.beans.factory.annotation.Autowired;	
import org.springframework.stereotype.Service;	
import java.util.*;	
	
@Service	
public class AbsIndexModelRuleServiceImpl implements AbsIndexModelRuleService {	
		
	@Autowired	
    private AbsIndexModelRuleMapper absIndexModelRuleMapper;	
		
	//主键获取	
	@Override	
	public AbsIndexModelRule getById(Integer id) {	
		return absIndexModelRuleMapper.selectByPrimaryKey(id);	
	}	
		
	//获取无参list	
	@Override	
	public List<AbsIndexModelRule> getList() {	
		return null;	
	}	
		
	//获取有参数list	
	@Override	
	public List<AbsIndexModelRule> getList(AbsIndexModelRule model) {	
		return null;	
	}	
		
	//获取带分页list	
	@Override	
	public Page<AbsIndexModelRule> getPageList(Page<AbsIndexModelRule> page) {	
		return null;	
	}	
		
	//通过条件获取	
	@Override	
	public AbsIndexModelRule getByModel(AbsIndexModelRule model) {	
		return null;	
	}	
	
	//保存对象	
	@Override	
	public int save(AbsIndexModelRule model) {	
		return absIndexModelRuleMapper.insert(model);	
	}	
	
	//更新对象	
	@Override	
	public int update(AbsIndexModelRule model) {	
		return absIndexModelRuleMapper.updateByPrimaryKey(model);	
	}	
		
	//删除对象	
	@Override	
	public int deleteById(Integer id) {	
		return absIndexModelRuleMapper.deleteByPrimaryKey(id);	
	}	
		
	//其他查询	
	@Override	
	public Map<String, Object> getOther() {	
		return null;	
	}	
}	
