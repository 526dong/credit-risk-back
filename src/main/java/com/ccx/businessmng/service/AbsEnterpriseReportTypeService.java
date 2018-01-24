package com.ccx.businessmng.service;
	
import com.ccx.businessmng.model.AbsEnterpriseReportType;	
import com.ccx.util.page.Page;	
import java.util.*;	
	
public interface AbsEnterpriseReportTypeService {	
		
	//主键获取	
	AbsEnterpriseReportType getById(Integer id);

	//通过主键获取名称
	String getNameById(Integer id);

	//获取无参list
	List<AbsEnterpriseReportType> getList();	
		
	//获取有参数list	
	List<AbsEnterpriseReportType> getList(AbsEnterpriseReportType model);	
		
	//获取带分页list	
	Page<AbsEnterpriseReportType> getPageList(Page<AbsEnterpriseReportType> page);	
		
	//通过条件获取	
	AbsEnterpriseReportType getByModel(AbsEnterpriseReportType model);	
	
	//保存对象	
	int save(AbsEnterpriseReportType model);	
	
	//更新对象	
	int update(AbsEnterpriseReportType model);	
		
	//删除对象	
	int deleteById(Integer id);	
		
	//其他查询	
	Map<String, Object> getOther();	
}	
