package com.ccx.index.service;	
	
import com.ccx.index.model.AbsIndexModel;
import com.ccx.util.page.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
	
public interface AbsIndexModelService {	
		
	//主键获取	
	AbsIndexModel getById(Integer id);	
		
	//获取无参list	
	List<AbsIndexModel> getList();	
		
	//获取有参数list	
	List<AbsIndexModel> getList(AbsIndexModel model);	
		
	//获取带分页list	
	Page<AbsIndexModel> getPageList(Page<AbsIndexModel> page);	
		
	//通过条件获取	
	AbsIndexModel getByModel(AbsIndexModel model);	
	
	//保存对象	
	int save(AbsIndexModel model);	
	
	//更新对象	
	int update(AbsIndexModel model);

	//更新对象
	int updateSelective(AbsIndexModel model);
		
	//删除对象	
	int deleteById(Integer id);	
		
	//其他查询	
	Map<String, Object> getOther();

    //保存模型和规则
    void saveOrUpdateModelAndRule(
			              HttpServletRequest request,
    					  String codes,
						  String ruleIds,
						  String delIds,
						  String valueMins,
						  String valueMaxs,
						  String scores,
						  String degrees,
						  Integer id,
						  String name);

    //删除模型和规则
	void deleteModelAndRule(AbsIndexModel model);

	//保存匹配
    void saveMatch(String id, Integer reportTypeId, String industryId, String type, String delId, String delType, boolean batchFlag);

    //通过名字模糊查询
    List<AbsIndexModel> getByLikeName(String name);
}	
