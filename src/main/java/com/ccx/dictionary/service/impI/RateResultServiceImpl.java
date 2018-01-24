package com.ccx.dictionary.service.impI;	
	
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccx.dictionary.dao.RateResultMapper;
import com.ccx.dictionary.model.RateResult;
import com.ccx.dictionary.service.RateResultService;
import com.ccx.util.page.Page;	
	
@Service	
public class RateResultServiceImpl implements RateResultService {	
		
	@Autowired	
    private RateResultMapper rateResultMapper;	
		
	//主键获取	
	@Override	
	public RateResult getById(Integer id) {	
		return rateResultMapper.selectByPrimaryKey(id);	
	}
	
	//name获取	
	@Override
	public RateResult getByName(String name) {
		return rateResultMapper.getByName(name);
	}
		
	//获取无参list	
	@Override	
	public List<RateResult> getList() {	
		return rateResultMapper.findAllRateResult();	
	}	
		
	//获取有参数list	
	@Override	
	public List<RateResult> getList(RateResult model) {	
		return null;	
	}	
		
	//获取带分页list	
	@Override	
	public Page<RateResult> getPageList(Page<RateResult> page) {
		page.setRows(rateResultMapper.findAll(page));
		return page;	
	}	
		
	//通过条件获取	
	@Override	
	public RateResult getByModel(RateResult model) {	
		return null;	
	}	
	
	//保存对象	
	@Override	
	public int save(RateResult model) {	
		return rateResultMapper.insert(model);	
	}	
	
	//更新对象	
	@Override	
	public int update(RateResult model) {	
		return rateResultMapper.updateByPrimaryKey(model);	
	}	
		
	//删除对象	
	@Override	
	public int deleteById(Integer id) {	
		return rateResultMapper.deleteByPrimaryKey(id);	
	}	
		
	//其他查询	
	@Override	
	public Map<String, Object> getOther() {	
		return null;	
	}

}	
