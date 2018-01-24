package com.ccx.hierarchy.service.impl;	
	
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccx.hierarchy.dao.HierarchyMapper;
import com.ccx.hierarchy.model.LayerLevelBean;
import com.ccx.hierarchy.service.HierarchyService;
import com.ccx.util.page.Page;
	
@Service	
public class HierarchyServiceImpl implements HierarchyService {	
		
	private Logger logger = LogManager.getLogger(HierarchyServiceImpl.class);
	
	
	@Autowired	
    private HierarchyMapper hierarchyMapper;	
	
	/**
	 * 
	 * @描述：查询分层层级列表
	 * @创建时间：  2017年10月17日
	 * @作者：武向楠
	 */
	@Override
	public Page<LayerLevelBean> findLevelList(Page<LayerLevelBean> page){
		page.setRows(hierarchyMapper.findLevelList(page));
		return page;
	}
	
	/**
	 * 
	 * @描述：根据id查询分层信息
	 * @创建时间：  2017年10月19日
	 * @作者：武向楠
	 */
	@Override
	public LayerLevelBean selectHierarchyLevelById(int id){
		return hierarchyMapper.selectHierarchyLevelById(id);
	}
	
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
	@Override
	public String updateHierarchyLevel(LayerLevelBean layerLevelBean){
		String result = "999";
		try {
			int flag = hierarchyMapper.updateHierarchyLevel(layerLevelBean);
			if(flag>0){
				result = "1000";
			}
		} catch (Exception e) {
			logger.info("更新分层失败！",e);
			return result;
		}
		return result;
	}
	
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
	@Override
	public List<LayerLevelBean> getHierarchyLevelListByName(String layerName){
		return hierarchyMapper.getHierarchyLevelListByName(layerName);
	}
	
	/**
	 * 
	 * @描述：获取等级list
	 * @创建时间：  2017年10月26日
	 * @作者：武向楠
	 */
	@Override
	public List<String> getGradeList(){
		return hierarchyMapper.getGradeList();
	}
	
	/**
	 * 
	 * @描述：获取违约率list
	 * @创建时间：  2017年10月26日
	 * @作者：武向楠
	 */
	@Override
	public List<LinkedHashMap<String, Object>> getLayerDefaulRatesList(){
		return hierarchyMapper.getLayerDefaulRatesList();
	}
	
	/**
	 * 
	 * @描述：获取回收率list
	 * @创建时间：  2017年10月31日
	 * @作者：武向楠
	 */
	@Override
	public List<LinkedHashMap<String, Object>> getLayerRecoveryRatesList(String param){
		return hierarchyMapper.getLayerRecoveryRatesList(param);
	}
	
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
	@Override
	public String saveUpdateDefaulrates(Map<String,Object> map){
		String result = "999";
		int flag = hierarchyMapper.saveUpdateDefaulrates(map);
		if(flag>0){
			result = "1000";
		}
		return result;
	}
	
	/**
	 * 
	 * @描述：保存回收率
	 * @创建时间：  2017年10月31日
	 * @作者：武向楠
	 */
	@Override
	public String saveUpdateRecoveryrates(Map<String,Object> map){
		String result = "999";
		int flag = hierarchyMapper.saveUpdateRecoveryrates(map);
		if(flag>0){
			result = "1000";
		}
		return result;
	}
	
	/**
	 * 
	 * @描述：解析Excel中的违约率入库
	 * @创建时间：  2017年11月13日
	 * @作者：武向楠
	 * @参数： @param paramList      
	 * @return void     
	 * @throws
	 */
	@Override
	public void anaysisExcelAndSave(List<Map<String,Object>> paramList){
		if (null!=paramList&&!paramList.isEmpty()) {
			for (int i = 0; i < paramList.size(); i++) {
				Map<String,Object> map = paramList.get(i);
				List<Object> list = (List<Object>) map.get("breakRateList");
				
				List<Map<String,Object>> paramList2 = new ArrayList<Map<String,Object>>();
				for (int j = 0; j < list.size(); j++) {
					Map<String,Object> paramMap = new HashMap<String, Object>();;
					paramMap.put("ratingLevel", (String) map.get("ratingLevel"));
					paramMap.put("breakRate", list.get(j));
					paramMap.put("month", j+1);
					paramList2.add(paramMap);
				}
				System.err.print(paramList2.toString());
				hierarchyMapper.anaysisExcelAndSave(paramList2);
			}
		}
	}
	
	
	
	
	
}	
