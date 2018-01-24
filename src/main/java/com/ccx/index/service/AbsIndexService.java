package com.ccx.index.service;	
	
import com.ccx.index.model.AbsIndex;	
import com.ccx.util.page.Page;	
import java.util.*;	
	
public interface AbsIndexService {	
		
	//主键获取	
	AbsIndex getById(Integer id);	
		
	//获取无参list	
	List<AbsIndex> getList();	
		
	//获取有参数list	
	List<AbsIndex> getList(AbsIndex model);	
		
	//获取带分页list	
	Page<AbsIndex> getPageList(Page<AbsIndex> page);	
		
	//通过条件获取	
	AbsIndex getByModel(AbsIndex model);	
	
	//保存对象	
	int save(AbsIndex model);	
	
	//更新对象	
	int update(AbsIndex model);	
		
	//删除对象	
	int deleteById(Integer id);	
		
	//其他查询	
	Map<String, Object> getOther();

	//保存指标和rule
    void saveOrUpdatIndexAndRule(String codes,
						  String ruleIds,
						  String delIds,
						  String valueMins,
						  String values,
						  String valueMaxs,
						  String scores,
						  String degrees,
						  Integer eleId,
						  AbsIndex index);
	//选择更新
    void updateSelective(AbsIndex index);

    //查行业内的指标编号是否重复
	Integer checkIndexCode(Integer backId, String indexCode);

	//查定性指标
	List<AbsIndex> findNatureIndexByElementIds(List<Integer> elementIds);

	//更好企业报表类型同时，更新index相关
	void syncIndex(Integer oldTypeId, Integer newTyoeId);
}
