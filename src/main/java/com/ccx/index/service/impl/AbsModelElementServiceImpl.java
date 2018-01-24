package com.ccx.index.service.impl;
	
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccx.index.dao.AbsIndexMapper;
import com.ccx.index.dao.AbsIndexRuleMapper;
import com.ccx.index.dao.AbsModelElementMapper;
import com.ccx.index.model.AbsIndex;
import com.ccx.index.model.AbsModelElement;
import com.ccx.index.service.AbsModelElementService;
import com.ccx.util.page.Page;	
	
@Service	
public class AbsModelElementServiceImpl implements AbsModelElementService {
		
	@Autowired	
    private AbsModelElementMapper AbsModelElementMapper;

	@Autowired
	private AbsIndexMapper indexMapper;

	@Autowired
	private AbsIndexRuleMapper indexRuleMapper;
		
	//主键获取	
	@Override	
	public AbsModelElement getById(Integer id) {
		return AbsModelElementMapper.selectByPrimaryKey(id);
	}	
		
	//获取无参list	
	@Override	
	public List<AbsModelElement> getList() {
		return null;	
	}	
		
	//获取有参数list	
	@Override	
	public List<AbsModelElement> getList(AbsModelElement model) {
		return null;	
	}	
		
	//获取带分页list	
	@Override	
	public Page<AbsModelElement> getPageList(Page<AbsModelElement> page) {
		page.setRows(AbsModelElementMapper.getPageList(page));
		return page;
	}	
		
	//通过条件获取	
	@Override	
	public AbsModelElement getByModel(AbsModelElement model) {
		return null;	
	}	
	
	//保存对象	
	@Override	
	public int save(AbsModelElement model) {
		return AbsModelElementMapper.insert(model);
	}	
	
	//更新对象	
	@Override	
	public int update(AbsModelElement model) {
		return AbsModelElementMapper.updateByPrimaryKey(model);
	}	
		
	//删除对象	
	@Override	
	public int deleteById(Integer id) {	
		return AbsModelElementMapper.deleteByPrimaryKey(id);
	}	
		
	//其他查询	
	@Override	
	public Map<String, Object> getOther() {	
		return null;	
	}

	//删除因素和对应的指标
	@Override
	public void deleteElementAndIndex(AbsModelElement element) {
		List<Integer> indexIdList = new ArrayList<>();

		AbsModelElementMapper.deleteByPrimaryKey(element.getId());
		List<AbsIndex> indexList = indexMapper.getListByElementId(element.getId());
		for(AbsIndex index: indexList) {
			indexIdList.add(index.getId());
		}
		if (indexIdList.size() > 0) {
			indexRuleMapper.deleteByIndexIds(indexIdList);
		}

		indexMapper.deleteByElementId(element.getId());

	}

	//更新状态
	@Override
	public void UpdateState(Integer id, Integer state) {
		AbsModelElementMapper.UpdateState(id, state);
	}

	//检测编号
	@Override
	public int checkCode(Integer modelId, String code) {
		return AbsModelElementMapper.checkCode(modelId, code);
	}

	//选择更新
	@Override
	public void updateSelective(AbsModelElement element) {
		AbsModelElementMapper.updateByPrimaryKeySelective(element);
	}
	
	//通过行业modelId查找因素
	@Override
	public List<AbsModelElement> getListByModelId(Integer modelId) {
		return AbsModelElementMapper.getListByModelId(modelId);
	}
}	
