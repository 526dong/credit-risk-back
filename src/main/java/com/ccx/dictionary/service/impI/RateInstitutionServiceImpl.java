package com.ccx.dictionary.service.impI;	
	
import com.ccx.util.page.Page;
import com.ccx.dictionary.dao.RateInstitutionMapper;
import com.ccx.dictionary.model.RateInstitution;
import com.ccx.dictionary.service.RateInstitutionService;

import org.springframework.beans.factory.annotation.Autowired;	
import org.springframework.stereotype.Service;	
import java.util.*;	
	
@Service	
public class RateInstitutionServiceImpl implements RateInstitutionService {	
		
	@Autowired	
    private RateInstitutionMapper rateInstitutionMapper;	
		
	//主键获取	
	@Override	
	public RateInstitution getById(Integer id) {	
		return rateInstitutionMapper.selectByPrimaryKey(id);	
	}	
	
	//name获取	
	@Override
	public RateInstitution getByName(String name) {
		return rateInstitutionMapper.findByName(name);
	}

	@Override
	public int findCountByName(String name) {
		return rateInstitutionMapper.findCountByName(name);
	}

	@Override
	public int findByCode(String code) {
		return rateInstitutionMapper.findByCode(code);
	}

	//获取无参list	
	@Override	
	public List<RateInstitution> getList() {	
		return rateInstitutionMapper.findAllRateInstitution();	
	}	
		
	//获取有参数list	
	@Override	
	public List<RateInstitution> getList(RateInstitution model) {	
		return null;	
	}	
		
	//获取带分页list	
	@Override	
	public Page<RateInstitution> getPageList(Page<RateInstitution> page) {	
		page.setRows(rateInstitutionMapper.findAll(page));
		return page;	
	}	
		
	//通过条件获取	
	@Override	
	public RateInstitution getByModel(RateInstitution model) {	
		return null;	
	}	
	
	//保存对象	
	@Override	
	public int save(RateInstitution model) {	
		return rateInstitutionMapper.insert(model);	
	}

	//保证增删改查在同一个事务中(非事务方法)
	@Override
	public int mySaveRateIns(RateInstitution model) {
		return rateInstitutionMapper.insert(model);
	}
	
	//更新对象	
	@Override	
	public int update(RateInstitution model) {	
		return rateInstitutionMapper.updateByPrimaryKey(model);	
	}	
		
	//删除对象	
	@Override	
	public int deleteById(Integer id) {	
		return rateInstitutionMapper.deleteByPrimaryKey(id);	
	}	
		
	//其他查询	
	@Override	
	public Map<String, Object> getOther() {	
		return null;	
	}

}	
