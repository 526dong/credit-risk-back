package com.ccx.financialAnaly.service.impl;	
	
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ccx.financialAnaly.dao.FinancialAnalyMapper;
import com.ccx.financialAnaly.model.FinancialAnalyBean;
import com.ccx.financialAnaly.service.FinancialAnalyService;
import com.ccx.util.page.Page;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.poifs.storage.ListManagedBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;	
	
@Service	
public class FinancialAnalyServiceImpl implements FinancialAnalyService {	
		
	private Logger logger = LogManager.getLogger(FinancialAnalyServiceImpl.class);
	
	
	@Autowired	
    private FinancialAnalyMapper financialAnalyMapper;	
	
	
	/**
	 * 
	 * @描述：查询报表类型已经被改变的list
	 * @创建时间：  2017年10月18日
	 * @作者：武向楠
	 */
	@Override
	public List<FinancialAnalyBean> getAllHistoryTypeTemplate(){
		return financialAnalyMapper.getAllHistoryTypeTemplate();
	}
		
	/**
	 * 
	 * @描述：获取企业财务分析模板list
	 * @创建时间：  2017年9月27日
	 * @作者：武向楠
	 */
	@Override
	public Page<FinancialAnalyBean> getFinancialTemplateList(Page<FinancialAnalyBean> page) {
		page.setRows(financialAnalyMapper.getFinancialTemplateList(page));
		return page;
	}
	
	/**
	 * 
	 * @描述：获取报表类型数据
	 * @创建时间：  2017年9月28日
	 * @作者：武向楠
	 */
	@Override
	public List<LinkedHashMap<String, Object>> getReportTypeList(){
		return financialAnalyMapper.getReportTypeList();
	}
	
	/**
	 * 
	 * @描述：更改财务分析状态 启用/禁用/删除
	 * @创建时间：  2017年9月28日
	 * @作者：武向楠
	 * @参数： @param financialAnalyBean
	 * @参数： @return      
	 * @return String     
	 * @throws
	 */
	@Override
	public String updateFinancialStates(FinancialAnalyBean financialAnalyBean){
		String result = "9999";
		try {
			int msg = financialAnalyMapper.updateFinancialStates(financialAnalyBean);
			if(msg>0){
				result = "1000";
			}
		} catch (Exception e) {
			logger.error("更改财务分析状态 启用/禁用/删除 失败，失败原因：",e);
			result = "9999";
			return result;
		}
		return result;
	}
	
	/**
	 * 
	 * @描述：根据id获取财务模板信息
	 * @创建时间：  2017年9月28日
	 * @作者：武向楠
	 */
	@Override
	public FinancialAnalyBean getFinancialTemplateById(Long financialId){
		return financialAnalyMapper.getFinancialTemplateById(financialId);
	}
	
	/**
	 * 
	 * @描述：根据报表类型id获取报表信息
	 * @创建时间：  2017年9月28日
	 * @作者：武向楠
	 */
	@Override
	public Map<String, Object> getReportType(int dataTypeId){
		return financialAnalyMapper.getReportType(dataTypeId);
	}
	
	/**
	 * 
	 * @描述：存储财务模板信息
	 * @创建时间：  2017年9月27日
	 * @作者：武向楠
	 */
	@Override
	public int saveFinancialTemplate(FinancialAnalyBean financialAnalyBean){
		int result = -1;
		try {
			result = financialAnalyMapper.saveFinancialTemplate(financialAnalyBean);
		} catch (Exception e) {
			result = -1;
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 
	 * @描述：修改财务模板信息
	 * @创建时间：  2017年10月18日
	 * @作者：武向楠
	 */
	@Override
	public int updateFinancialTemplate(FinancialAnalyBean financialAnalyBean){
		int result = -1;
		try {
			result = financialAnalyMapper.updateFinancialStates(financialAnalyBean);
		} catch (Exception e) {
			result = -1;
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 
	 * @描述：修改财务模板报表类型
	 * @创建时间：  2017年10月17日
	 * @作者：武向楠
	 */
	@Override
	public boolean modifiFinancialTemplateReportType(int oldType,int newType){
		boolean result = false;
		try {
			//根据报表类型查找模板实体
			List<FinancialAnalyBean> financialAnalyBeanList = financialAnalyMapper.getFinancialTemplateByType(oldType);
			if(null != financialAnalyBeanList && !financialAnalyBeanList.isEmpty()){
				for (int i = 0; i < financialAnalyBeanList.size(); i++) {
					FinancialAnalyBean financialAnalyBean = financialAnalyBeanList.get(i);
					financialAnalyBean.setReportTypeId(newType);
					financialAnalyBean.setVersions(1);
					//根据模板id修改模板报表类型
					int flag = financialAnalyMapper.updateFinancialStates(financialAnalyBean);
					if(flag>0){
						result = true;
					}else{
						result = false;
						break;
					}
				}
			}else{//模板信息表中没有此类型的数据
				result = true;
			}
		} catch (Exception e) {
			logger.info("修改财务模板报表类型失败",e);
			return false;
		}
		return result;
	}
	
	
	
	
}	
