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

import com.ccx.hierarchy.dao.InsdustryMapper;
import com.ccx.hierarchy.model.InsdustryBean;
import com.ccx.hierarchy.model.LayerCorrelationindexBean;
import com.ccx.hierarchy.service.InsdustryService;
import com.ccx.util.UsedUtil;
import com.ccx.util.page.Page;
	
@Service	
public class InsdustryServiceImpl implements InsdustryService {	
		
	private Logger logger = LogManager.getLogger(InsdustryServiceImpl.class);
	
	
	@Autowired	
    private InsdustryMapper insdustryMapper;	
	
	
	/**
	 * 
	 * @描述：查询资产相关性系数公式
	 * @创建时间：  2017年11月1日
	 * @作者：武向楠
	 */
	@Override
	public List<HashMap<String, Object>> getAssetCorrelationFormula(){
		return insdustryMapper.getAssetCorrelationFormula();
	}
	
	/**
	 * 
	 * @描述：查询后台行业列表
	 * @创建时间：  2017年10月17日
	 * @作者：武向楠
	 */
	@Override
	public Page<InsdustryBean> findBgInsdustryList(Page<InsdustryBean> page){
		page.setRows(insdustryMapper.findBgInsdustryList(page));
		return page;
	}
	
	/**
	 * 
	 * @描述：通过name查找后台行业
	 * @创建时间：  2017年10月31日
	 * @作者：武向楠
	 */
	@Override
	public List<InsdustryBean> getHierarchyLevelListByName(String bgInsdustryName){
		return insdustryMapper.getHierarchyLevelListByName(bgInsdustryName);
	}
	
	/**
	 * 
	 * @描述：查询后台行业表中已经存在的条数(包含所有状态)
	 * @创建时间：  2017年10月19日
	 * @作者：武向楠
	 */
	@Override
	public List<InsdustryBean> findAllBgInsdustryList(){
		return insdustryMapper.findAllBgInsdustryList();
	}
	
	/**
	 * 
	 * @描述：新增后台行业
	 * @创建时间：  2017年10月19日
	 * @作者：武向楠
	 */
	@Override
	public String saveAddBgInsdustry(InsdustryBean insdustryBean){
		String result = "999";
		try {
			int flag = insdustryMapper.saveAddBgInsdustry(insdustryBean);
			if (flag>0) {
				result = "1000";
			}
		} catch (Exception e) {
			logger.info("新增后台行业失败！",e);
			return result;
		}
		return result;
	}
	
	/**
	 * 
	 * @描述：根据后台行业id获取与它关联的前台行业list
	 * @创建时间：  2017年10月19日
	 * @作者：武向楠
	 */
	@Override
	public List<InsdustryBean> findMatchInsdustryList(int bgInsdustryId){
		return insdustryMapper.findMatchInsdustryList(bgInsdustryId);
	}
	
	/**
	 * 
	 * @描述：根据id查询后台行业实体
	 * @创建时间：  2017年10月19日
	 * @作者：武向楠
	 */
	@Override
	public InsdustryBean findInsdustryBean(int bgInsdustryId){
		return insdustryMapper.findInsdustryBean(bgInsdustryId);
	}
	
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
	@Override
	public String updateBgInsdustry(InsdustryBean insdustryBean,String updateflag){
		String result = "999";
		try {
			int flag = insdustryMapper.updateBgInsdustry(insdustryBean);
			if (flag>0) {
				if("delete" == updateflag || "delete".equals(updateflag)){
					//删除行业相关性表数据
					insdustryMapper.deleteLayerCorrelationindexById(insdustryBean.getId());
					//删除前后台行业关联
					insdustryMapper.deleteBgInsdustrycorrelation(insdustryBean.getId());
				}else{
					insdustryMapper.updateLayerCorrelationindex(insdustryBean.getId(),insdustryBean.getInsdustryName());
				}
				result = "1000";
			}
		} catch (Exception e) {
			logger.info("修改后台行业失败！",e);
			return result;
		}
		return result;
	}
	
	/**
	 * 
	 * @描述：删除所匹配的前台行业
	 * @创建时间：  2017年10月19日
	 * @作者：武向楠
	 */
	@Override
	public String deleteMatchInsdustry(int matchId,int bgInsdustryId){
		String result = "999";
		try {
			int flag = insdustryMapper.deleteMatchInsdustry(matchId);
			if (flag>0) {
				//根据后台行业id查询是否已经有所匹配的前台行业list
				List<LinkedHashMap<String, Object>> insdustryMatchByIdList = insdustryMapper.getInsdustryMatchByIdList(bgInsdustryId);
				if(null==insdustryMatchByIdList||insdustryMatchByIdList.isEmpty()){
					//删除行业相关性表数据
					insdustryMapper.deleteLayerCorrelationindexById(bgInsdustryId);
				}
				result = "1000";
			}
		} catch (Exception e) {
			logger.info("删除所匹配的前台行业失败！",e);
			return result;
		}
		return result;
	}
	
	/**
	 * 
	 * @描述：查询剩余可以添加的前台行业list(分页)
	 * @创建时间：  2017年10月19日
	 * @作者：武向楠
	 */
	@Override
	public Page<InsdustryBean> selectCanMatchInsdustry(Page<InsdustryBean> page){
		page.setRows(insdustryMapper.selectCanMatchInsdustry(page));
		return page;
	}
	
	/**
	 * 
	 * @描述：保存匹配的前台行业
	 * @创建时间：  2017年10月31日
	 * @作者：武向楠
	 */
	@Override
	public String addMatchInsdustry(Map<String,Object> paramMap){
		String result = "999";
		try {
			int flag = insdustryMapper.addMatchInsdustry(paramMap);
			if (flag>0) {
				int bgInsdustryId = (Integer)paramMap.get("bgInsdustryId");
				if(UsedUtil.isNotNull(bgInsdustryId)){
					//根据后台行业id查询是否已经有所匹配的前台行业list
//					List<LinkedHashMap<String, Object>> insdustryMatchByIdList = insdustryMapper.getInsdustryMatchByIdList(bgInsdustryId);
					//根据后台行业id查询行业相关性list
					List<LinkedHashMap<String, Object>> insdustryCorrelationByIdList = insdustryMapper.getInsdustryCorrelationByIdList(bgInsdustryId);
					List<LayerCorrelationindexBean> layerCorrelationindexBeanList = new ArrayList<LayerCorrelationindexBean>();
					if(null==insdustryCorrelationByIdList||insdustryCorrelationByIdList.isEmpty()){
						List<InsdustryBean> insdustryBeanList = insdustryMapper.getInsdustryBeanList();
						if (null != insdustryBeanList && !insdustryBeanList.isEmpty()) {
							for (int i = 0; i < insdustryBeanList.size(); i++) {
								for (int j = 0; j < insdustryBeanList.size(); j++) {
									if(bgInsdustryId == insdustryBeanList.get(i).getId() || bgInsdustryId == insdustryBeanList.get(j).getId()){
										LayerCorrelationindexBean layerCorrelationindexBean = new LayerCorrelationindexBean();
										layerCorrelationindexBean.setInsdustryFirst(insdustryBeanList.get(i).getId());
										layerCorrelationindexBean.setInsdustryFirstName(insdustryBeanList.get(i).getInsdustryName());
										layerCorrelationindexBean.setInsdustrySecond(insdustryBeanList.get(j).getId());
										layerCorrelationindexBean.setInsdustrySecondName(insdustryBeanList.get(j).getInsdustryName());
										layerCorrelationindexBeanList.add(layerCorrelationindexBean);
									}
								}
							}
						}
					}
					if (null != layerCorrelationindexBeanList && !layerCorrelationindexBeanList.isEmpty()) {
						insdustryMapper.saveLayerCorrelationindex(layerCorrelationindexBeanList);
					}
				}
				result = "1000";
			}
		} catch (Exception e) {
			logger.info("保存匹配的前台行业失败！",e);
			return result;
		}
		return result;
	}
	
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
	@Override
	public String updateCorrelationFormula(Map<String,Object> paramMap){
		String result = "999";
		try {
			int flag = insdustryMapper.updateCorrelationFormula(paramMap);
			if (flag>0) {
				result = "1000";
			}
		} catch (Exception e) {
			logger.info("保存资产相关性系数公式设置失败！",e);
			return result;
		}
		return result;
	}
	
	/**
	 * 
	 * @描述：查询后台行业相关性list
	 * @创建时间：  2017年11月2日
	 * @作者：武向楠
	 * @参数： @return      
	 * @return List<LinkedHashMap<String,Object>>     
	 * @throws
	 */
	@Override
	public List<LinkedHashMap<String, Object>> getInsdustryCorrelationList(){
		return insdustryMapper.getInsdustryCorrelationList();
	}
	
	/**
	 * 
	 * @描述：保存行业相关性数据
	 * @创建时间：  2017年11月2日
	 * @作者：武向楠
	 */
	@Override
	public String saveBgInsdustryCorrelation(Map<String,Object> paramMap){
		String result = "999";
		try {
			int flag = insdustryMapper.saveBgInsdustryCorrelation(paramMap);
			if (flag>0) {
				result = "1000";
			}
		} catch (Exception e) {
			logger.info("保存行业相关性数据失败！",e);
			return result;
		}
		return result;
	}
	
	
	
	
}	
