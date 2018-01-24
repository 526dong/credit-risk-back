package com.ccx.financialAnaly.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ccx.financialAnaly.model.FinancialAnalyBean;
import com.ccx.util.page.Page;

public interface FinancialAnalyMapper {
	
	/**
	 * 
	 * @描述：查询报表类型已经被改变的list
	 * @创建时间：  2017年10月18日
	 * @作者：武向楠
	 * @参数： @return      
	 * @return List<FinancialAnalyBean>     
	 * @throws
	 */
	List<FinancialAnalyBean> getAllHistoryTypeTemplate();
    
	/**
	 * 
	 * @描述：获取企业财务分析模板list
	 * @创建时间：  2017年9月26日
	 * @作者：武向楠
	 * @参数： @param pages
	 * @参数： @return      
	 * @return List<FinancialAnalyBean>     
	 * @throws
	 */
	List<FinancialAnalyBean> getFinancialTemplateList(Page<FinancialAnalyBean> pages);
	
	/**
	 * 
	 * @描述：获取报表类型数据
	 * @创建时间：  2017年9月28日
	 * @作者：武向楠
	 * @参数： @return      
	 * @return List<LinkedHashMap<String,Object>>     
	 * @throws
	 */
	List<LinkedHashMap<String, Object>> getReportTypeList();
	
	/**
	 * 
	 * @描述：更改财务分析状态 启用/禁用/删除
	 * @创建时间：  2017年9月28日
	 * @作者：武向楠
	 * @参数： @param financialAnalyBean
	 * @参数： @return      
	 * @return int     
	 * @throws
	 */
	int updateFinancialStates(FinancialAnalyBean financialAnalyBean);
	
	/**
	 * 
	 * @描述：根据id获取财务模板信息
	 * @创建时间：  2017年9月28日
	 * @作者：武向楠
	 * @参数： @param financialId
	 * @参数： @return      
	 * @return FinancialAnalyBean     
	 * @throws
	 */
	FinancialAnalyBean getFinancialTemplateById(Long financialId);
	
	/**
	 * 
	 * @描述：根据报表类型id获取报表信息
	 * @创建时间：  2017年9月28日
	 * @作者：武向楠
	 * @参数： @param dataTypeId
	 * @参数： @return      
	 * @return Map<String,Object>     
	 * @throws
	 */
	Map<String, Object> getReportType(int dataTypeId);
	
	/**
	 * 
	 * @描述：存储财务模板信息
	 * @创建时间：  2017年9月27日
	 * @作者：武向楠
	 * @参数： @param financialAnalyBean
	 * @参数： @return      
	 * @return int     
	 * @throws
	 */
	int saveFinancialTemplate(FinancialAnalyBean financialAnalyBean);
	
	/**
	 * 
	 * @描述：根据报表类型查找模板实体
	 * @创建时间：  2017年10月17日
	 * @作者：武向楠
	 * @参数： @param oldType
	 * @参数： @return      
	 * @return List<FinancialAnalyBean>     
	 * @throws
	 */
	List<FinancialAnalyBean> getFinancialTemplateByType(int oldType);
	
	

   
    
}