package com.ccx.index.service;


import java.util.List;
import java.util.Map;

import com.ccx.index.model.AbsModelElement;
import com.ccx.util.page.Page;

public interface AbsModelElementService{
	//主键获取
	AbsModelElement getById(Integer id);
		
	//获取无参list	
	List<AbsModelElement> getList();
		
	//获取有参数list	
	List<AbsModelElement> getList(AbsModelElement model);
		
	//获取带分页list	
	Page<AbsModelElement> getPageList(Page<AbsModelElement> page);
		
	//通过条件获取	
	AbsModelElement getByModel(AbsModelElement model);
	
	//保存对象	
	int save(AbsModelElement model);
	
	//更新对象	
	int update(AbsModelElement model);
		
	//删除对象	
	int deleteById(Integer id);	
		
	//其他查询	
	Map<String, Object> getOther();

	//删除因素和对应的指标
    void deleteElementAndIndex(AbsModelElement element);

    //更新状态
    void UpdateState(Integer id, Integer state);

    //检测编号
	int checkCode(Integer modelId, String code);

	//选择更新
    void updateSelective(AbsModelElement element);
    
    //通过行业modelId查找因素
    List<AbsModelElement> getListByModelId(Integer modelId);
}	
