package com.ccx.hierarchy.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ccx.hierarchy.model.InsdustryBean;
import com.ccx.hierarchy.model.LayerCorrelationindexBean;
import com.ccx.util.page.Page;

public interface InsdustryMapper {
    
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
	 * @参数： @param page
	 * @参数： @return      
	 * @return List<LayerLevelBean>     
	 * @throws
	 */
	List<InsdustryBean> findBgInsdustryList(Page<InsdustryBean> page);
	
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
	List<InsdustryBean> getHierarchyLevelListByName(@Param("bgInsdustryName")String bgInsdustryName);
	
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
	 * @return int     
	 * @throws
	 */
	int saveAddBgInsdustry(InsdustryBean insdustryBean);
	
	/**
	 * 
	 * @描述：查询后台行业list
	 * @创建时间：  2017年11月2日
	 * @作者：武向楠
	 * @参数： @return      
	 * @return List<InsdustryBean>     
	 * @throws
	 */
	List<InsdustryBean> getInsdustryBeanList();
	
	/**
	 * 
	 * @描述：根据后台行业id查询行业相关性list
	 * @创建时间：  2017年11月10日
	 * @作者：武向楠
	 * @参数： @param bgInsdustryId
	 * @参数： @return      
	 * @return List<LinkedHashMap<String,Object>>     
	 * @throws
	 */
	List<LinkedHashMap<String, Object>> getInsdustryCorrelationByIdList(@Param("insdustryId")int bgInsdustryId);
	
	/**
	 * 
	 * @描述：根据后台行业id查询是否已经有所匹配的前台行业list
	 * @创建时间：  2017年11月10日
	 * @作者：武向楠
	 * @参数： @param bgInsdustryId
	 * @参数： @return      
	 * @return List<LinkedHashMap<String,Object>>     
	 * @throws
	 */
	List<LinkedHashMap<String, Object>> getInsdustryMatchByIdList(@Param("insdustryId")int bgInsdustryId);
	
	/**
	 * 
	 * @描述：删除行业相关性表数据
	 * @创建时间：  2017年11月2日
	 * @作者：武向楠
	 * @参数： @return      
	 * @return int     
	 * @throws
	 */
	int deleteLayerCorrelationindex();
	
	/**
	 * 
	 * @描述：保存行业相关性表数据
	 * @创建时间：  2017年11月2日
	 * @作者：武向楠
	 * @参数： @param layerCorrelationindexBeanList
	 * @参数： @return      
	 * @return int     
	 * @throws
	 */
	int saveLayerCorrelationindex(List<LayerCorrelationindexBean> layerCorrelationindexBeanList);
	
	/**
	 * 
	 * @描述：修改行业相关性表数据
	 * @创建时间：  2017年11月2日
	 * @作者：武向楠
	 * @参数： @param insdustryName
	 * @参数： @return      
	 * @return int     
	 * @throws
	 */
	int updateLayerCorrelationindex(@Param("insdustryId")int insdustryId,@Param("insdustryName")String insdustryName);
	
	/**
	 * 
	 * @描述：删除行业相关性表数据
	 * @创建时间：  2017年11月2日
	 * @作者：武向楠
	 * @参数： @param insdustryName
	 * @参数： @return      
	 * @return int     
	 * @throws
	 */
	int deleteLayerCorrelationindexById(@Param("insdustryId")int insdustryId);
	
	/**
	 * 
	 * @描述：删除前后台行业关联
	 * @创建时间：  2017年11月2日
	 * @作者：武向楠
	 * @参数： @param insdustryName
	 * @参数： @return      
	 * @return int     
	 * @throws
	 */
	int deleteBgInsdustrycorrelation(@Param("insdustryId")int insdustryId);
	
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
	 * @return int     
	 * @throws
	 */
	int updateBgInsdustry(InsdustryBean insdustryBean);
	
	/**
	 * 
	 * @描述：删除所匹配的前台行业
	 * @创建时间：  2017年10月19日
	 * @作者：武向楠
	 * @参数： @param matchId
	 * @参数： @return      
	 * @return int     
	 * @throws
	 */
	int deleteMatchInsdustry(@Param("matchId")int matchId);
	
	/**
	 * 
	 * @描述：查询剩余可以添加的前台行业list(分页)
	 * @创建时间：  2017年10月19日
	 * @作者：武向楠
	 * @参数： @param page
	 * @参数： @return      
	 * @return List<InsdustryBean>     
	 * @throws
	 */
	List<InsdustryBean> selectCanMatchInsdustry(Page<InsdustryBean> page);
	
	/**
	 * 
	 * @描述：保存匹配的前台行业
	 * @创建时间：  2017年11月1日
	 * @作者：武向楠
	 * @参数： @param paramMap
	 * @参数： @return      
	 * @return int     
	 * @throws
	 */
	int addMatchInsdustry(Map<String,Object> paramMap);
	
	/**
	 * 
	 * @描述：保存资产相关性系数公式设置
	 * @创建时间：  2017年11月1日
	 * @作者：武向楠
	 * @参数： @param correlationSetupId
	 * @参数： @param paramValue
	 * @参数： @return      
	 * @return int     
	 * @throws
	 */
	int updateCorrelationFormula(Map<String,Object> paramMap);
	
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
	 * @return int     
	 * @throws
	 */
	int saveBgInsdustryCorrelation(Map<String,Object> paramMap);

   
    
}