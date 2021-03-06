package com.ccx.hierarchy.service;	
	
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ccx.hierarchy.model.LayerLevelBean;
import com.ccx.util.page.Page;
	
public interface HierarchyService {	
		
	/**
	 * 
	 * @描述：查询分层层级列表
	 * @创建时间：  2017年10月17日
	 * @作者：武向楠
	 * @参数： @param pages
	 * @参数： @return      
	 * @return Page<LayerLevelBean>     
	 * @throws
	 */
	Page<LayerLevelBean> findLevelList(Page<LayerLevelBean> pages);	
	
	/**
	 * 
	 * @描述：根据id查询分层信息
	 * @创建时间：  2017年10月19日
	 * @作者：武向楠
	 * @参数： @param id
	 * @参数： @return      
	 * @return LayerLevelBean     
	 * @throws
	 */
	LayerLevelBean selectHierarchyLevelById(int id);
	
	/**
	 * 
	 * @描述：更新分层
	 * @创建时间：  2017年10月19日
	 * @作者：武向楠
	 * @参数： @param layerLevelBean
	 * @参数： @return      
	 * @return String     
	 * @throws
	 */
	String updateHierarchyLevel(LayerLevelBean layerLevelBean);
	
	/**
	 * 
	 * @描述：验证层级是否唯一
	 * @创建时间：  2017年10月26日
	 * @作者：武向楠
	 * @参数： @param layerName
	 * @参数： @return      
	 * @return List<LayerLevelBean>     
	 * @throws
	 */
	List<LayerLevelBean> getHierarchyLevelListByName(String layerName);
	
	/**
	 * 
	 * @描述：获取等级list
	 * @创建时间：  2017年10月26日
	 * @作者：武向楠
	 * @参数： @return      
	 * @return List<String>     
	 * @throws
	 */
	List<String> getGradeList();
	
	/**
	 * 
	 * @描述：获取违约率list
	 * @创建时间：  2017年10月26日
	 * @作者：武向楠
	 * @参数： @return      
	 * @return List<LinkedHashMap<String,Object>>     
	 * @throws
	 */
	List<LinkedHashMap<String, Object>> getLayerDefaulRatesList();
	
	/**
	 * 
	 * @描述：获取回收率list
	 * @创建时间：  2017年10月31日
	 * @作者：武向楠
	 * @参数： @param param
	 * @参数： @return      
	 * @return List<LinkedHashMap<String,Object>>     
	 * @throws
	 */
	List<LinkedHashMap<String, Object>> getLayerRecoveryRatesList(String param);
	
	/**
	 * 
	 * @描述：保存违约率
	 * @创建时间：  2017年10月31日
	 * @作者：武向楠
	 * @参数： @param map
	 * @参数： @return      
	 * @return String     
	 * @throws
	 */
	String saveUpdateDefaulrates(Map<String,Object> map);
	
	/**
	 * 
	 * @描述：保存回收率
	 * @创建时间：  2017年10月31日
	 * @作者：武向楠
	 * @参数： @param map
	 * @参数： @return      
	 * @return String     
	 * @throws
	 */
	String saveUpdateRecoveryrates(Map<String,Object> map);
	
	/**
	 * 
	 * @描述：解析Excel中的违约率入库
	 * @创建时间：  2017年11月13日
	 * @作者：武向楠
	 * @参数： @param paramList      
	 * @return void     
	 * @throws
	 */
	void anaysisExcelAndSave(List<Map<String,Object>> paramList);
	
	
		
	
}	
