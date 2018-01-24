package com.ccx.reporttype.service;	
	
import com.ccx.reporttype.model.AbsEnterpriseReportCheck;	
import com.ccx.util.page.Page;	
import java.util.*;	
	
public interface AbsEnterpriseReportCheckService {	
		
	//主键获取	
	AbsEnterpriseReportCheck getById(Integer id);	
		
	//获取无参list	
	List<AbsEnterpriseReportCheck> getList();	
		
	//获取有参数list	
	List<AbsEnterpriseReportCheck> getList(AbsEnterpriseReportCheck model);	
		
	//获取带分页list	
	Page<AbsEnterpriseReportCheck> getPageList(Page<AbsEnterpriseReportCheck> page);	
		
	//通过条件获取	
	AbsEnterpriseReportCheck getByModel(AbsEnterpriseReportCheck model);	
	
	//保存对象	
	int save(AbsEnterpriseReportCheck model);	
	
	//更新对象	
	int update(AbsEnterpriseReportCheck model);	
		
	//删除对象	
	int deleteById(Integer id);	
		
	//其他查询	
	Map<String, Object> getOther();

	//保存规则
    void saveSetRule(Integer typeId, String formulaHtmlContent, String formulaContent);
}
