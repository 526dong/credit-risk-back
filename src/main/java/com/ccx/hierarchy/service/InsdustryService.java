package com.ccx.hierarchy.service;	
	
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ccx.hierarchy.model.InsdustryBean;
import com.ccx.util.page.Page;
	
public interface InsdustryService {	
	
	/**
	 * 
	 * @描述：查询资产相关性系数公式
	 * @创建时间：  2017年11月1日
	 * @作者：武向楠
	 * @参数： @return      
	 * @return List<HashMap<String, Object>>   
	 * @throws
	 */
	List<HashMap<String, Object>> getAssetCorrelationFormula();
	
	/**
	 * 
	 * @描述：查询后台行业列表
	 * @创建时间：  2017年10月17日
	 * @作者：武向楠
	 * @参数： @param pages
	 * @参数： @return      
	 * @return Page<LayerLevelBean>     
	 * @throws
	 */
	Page<InsdustryBean> findBgInsdustryList(Page<InsdustryBean> pages);	
	
	/**
	 * 
	 * @描述：通过name查找后台行业
	 * @创建时间：  2017年10月31日
	 * @作者：武向楠
	 * @参数： @param bgInsdustryName
	 * @参数： @return      
	 * @return List<InsdustryBean>     
	 * @throws
	 */
	List<InsdustryBean> getHierarchyLevelListByName(String bgInsdustryName);
	
	/**
	 * 
	 * @描述：查询后台行业表中已经存在的条数(包含所有状态)
	 * @创建时间：  2017年10月19日
	 * @作者：武向楠
	 * @参数： @return      
	 * @return List<InsdustryBean>     
	 * @throws
	 */
	List<InsdustryBean> findAllBgInsdustryList();
	
	/**
	 * 
	 * @描述：新增后台行业
	 * @创建时间：  2017年10月19日
	 * @作者：武向楠
	 * @参数： @param insdustryBean
	 * @参数： @return      
	 * @return String     
	 * @throws
	 */
	String saveAddBgInsdustry(InsdustryBean insdustryBean);
	
	/**
	 * 
	 * @描述：根据后台行业id获取与它关联的前台行业list
	 * @创建时间：  2017年10月19日
	 * @作者：武向楠
	 * @参数： @param bgInsdustryId
	 * @参数： @return      
	 * @return List<InsdustryBean>     
	 * @throws
	 */
	List<InsdustryBean> findMatchInsdustryList(int bgInsdustryId);
	
	/**
	 * 
	 * @描述：根据id查询后台行业实体
	 * @创建时间：  2017年10月19日
	 * @作者：武向楠
	 * @参数： @param bgInsdustryId
	 * @参数： @return      
	 * @return InsdustryBean     
	 * @throws
	 */
	InsdustryBean findInsdustryBean(int bgInsdustryId);
	
	/**
	 * 
	 * @描述：修改后台行业
	 * @创建时间：  2017年10月19日
	 * @作者：武向楠
	 * @参数： @param insdustryBean
	 * @参数： @return      
	 * @return String     
	 * @throws
	 */
	String updateBgInsdustry(InsdustryBean insdustryBean,String updateflag);
	
	/**
	 * 
	 * @描述：删除所匹配的前台行业
	 * @创建时间：  2017年10月19日
	 * @作者：武向楠
	 * @参数： @param matchId
	 * @参数： @return      
	 * @return String     
	 * @throws
	 */
	String deleteMatchInsdustry(int matchId,int bgInsdustryId);
	
	/**
	 * 
	 * @描述：查询剩余可以添加的前台行业list(分页)
	 * @创建时间：  2017年10月19日
	 * @作者：武向楠
	 * @参数： @param page
	 * @参数： @return      
	 * @return Page<InsdustryBean>     
	 * @throws
	 */
	Page<InsdustryBean> selectCanMatchInsdustry(Page<InsdustryBean> page);
	
	/**
	 * 
	 * @描述：保存匹配的前台行业
	 * @创建时间：  2017年11月1日
	 * @作者：武向楠
	 * @参数： @param paramMap
	 * @参数： @return      
	 * @return String     
	 * @throws
	 */
	String addMatchInsdustry(Map<String,Object> paramMap);
	
	/**
	 * 
	 * @描述：保存资产相关性系数公式设置
	 * @创建时间：  2017年11月1日
	 * @作者：武向楠
	 * @参数： @param correlationSetupId
	 * @参数： @param paramValue
	 * @参数： @return      
	 * @return Map<String,Object> paramMap     
	 * @throws
	 */
	String updateCorrelationFormula(Map<String,Object> paramMap);
	
	/**
	 * 
	 * @描述：查询后台行业相关性list
	 * @创建时间：  2017年11月2日
	 * @作者：武向楠
	 * @参数： @return      
	 * @return List<LinkedHashMap<String,Object>>     
	 * @throws
	 */
	List<LinkedHashMap<String, Object>> getInsdustryCorrelationList();
	
	/**
	 * 
	 * @描述：保存行业相关性数据
	 * @创建时间：  2017年11月2日
	 * @作者：武向楠
	 * @参数： @param paramMap
	 * @参数： @return      
	 * @return String     
	 * @throws
	 */
	String saveBgInsdustryCorrelation(Map<String,Object> paramMap);
	
}	
