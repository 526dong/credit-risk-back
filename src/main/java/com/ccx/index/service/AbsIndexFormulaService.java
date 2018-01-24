package com.ccx.index.service;	
	
import com.ccx.index.model.AbsIndex;
import com.ccx.index.model.AbsIndexFormula;
import com.ccx.util.page.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
	
public interface AbsIndexFormulaService {	
		
	//主键获取	
	AbsIndexFormula getById(Integer id);	
		
	//获取无参list	
	List<AbsIndexFormula> getList();	
		
	//获取有参数list	
	List<AbsIndexFormula> getList(AbsIndexFormula model);

	//获取带分页withoutlist
	Page<AbsIndexFormula> getPageWithoutFormulaList(Page<AbsIndexFormula> page);

	//获取带分页list	
	Page<AbsIndexFormula> getPageList(Page<AbsIndexFormula> page);	
		
	//通过条件获取	
	AbsIndexFormula getByModel(AbsIndexFormula model);	
	
	//保存对象	
	int save(AbsIndexFormula model);	
	
	//更新对象	
	int update(AbsIndexFormula model);	
		
	//删除对象	
	int deleteById(Integer id);	
		
	//其他查询	
	Map<String, Object> getOther();

	//名字模糊查询
    List<AbsIndexFormula> getbyLikeName(String formulaName, Integer reportType);

    //保存或新增
    void saveOrUpdate(HttpServletRequest request, AbsIndexFormula formula);

    //查找公式在指标的引用
    List<AbsIndex> findIndexUsage(Integer id);

    //删除公式同时删除指标中对公式的引用
	void deleteFormulaAndIndexUsage(Integer id);

	List<Integer> getIdByFormulaName(String name);
}	
