package com.ccx.hierarchy.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.ccx.base.BasicController;
import com.ccx.hierarchy.model.LayerLevelBean;
import com.ccx.hierarchy.service.HierarchyService;
import com.ccx.util.ControllerUtil;
import com.ccx.util.UsedUtil;
import com.ccx.util.page.Page;

/**
 * 
 * @类功能说明：  分层管理
 * @作者：武向楠  
 * @创建时间：2017年10月17日 下午2:32:15  
 * @版本：V1.0
 */
@Controller
@RequestMapping("/hierarchyManage")
public class HierarchyController extends BasicController {

	private static final Logger logger = LoggerFactory.getLogger(HierarchyController.class);
	
	@Autowired
	private HierarchyService hierarchyService;

	/**
	 * 分层层级管理页
	 * 
	 * @return
	 */
	@GetMapping("/toHierarchyManagerPage")
	public String toHierarchyManagerPage() {
		return "hierarchyMng/levelMng/levelList";
	}
	
	
	/**
	 * 
	 * @描述：查询分层层级列表
	 * @创建时间：  2017年10月17日
	 * @作者：武向楠
	 * @参数： @return      
	 * @return Page<LayerLevelBean>     
	 * @throws
	 */
	@PostMapping("/findLevelList")
	@ResponseBody
	public Page<LayerLevelBean> findLevelList(HttpServletRequest request) {
		//获取请求参数
    	Map<String,Object> map = ControllerUtil.request2Map(request);
    	Page<LayerLevelBean> pages = new Page<LayerLevelBean>();
    	Map<String,Object> params = new HashMap<String,Object>();
    	logger.info("参数=========================>"+map);
		// 获取当前页数
		String currentPage = (String) map.get("currentPage");
		//获取每页展示数
		String pageSize = (String) map.get("pageSize");
		//当前页数
		int pageNum = 1;
		if (UsedUtil.isNotNull(currentPage)) { 
			pageNum = Integer.valueOf(currentPage);
		}
		// 设置当前页数
		pages.setPageNo(pageNum);
		//设置参数
		pages.setParams(params);
		//设置每页展示数
		if(UsedUtil.isNotNull(pageSize)){ 
			pages.setPageSize(Integer.valueOf(pageSize));
		}
		//查询账号列表
		pages = hierarchyService.findLevelList(pages);
		return pages;
	}
	
	/**
	 * 
	 * @描述：删除分层
	 * @创建时间：  2017年10月19日
	 * @作者：武向楠
	 * @参数： @param request
	 * @参数： @return      
	 * @return String     
	 * @throws
	 */
	@RequestMapping(value = "/updateHierarchyLevel", method = RequestMethod.POST)
	@ResponseBody
	public String updateHierarchyLevel(HttpServletRequest request) {
		String levelIds = null==request.getParameter("levelId")?"":request.getParameter("levelId").trim();
		String result = "999";
		if(UsedUtil.isNotNull(levelIds)){
			int levelId = Integer.parseInt(levelIds);
			LayerLevelBean layerLevelBean = hierarchyService.selectHierarchyLevelById(levelId);
			layerLevelBean.setIsDel(1);
			result = hierarchyService.updateHierarchyLevel(layerLevelBean);
		}
		return result;
	}
	
	/**
	 * 
	 * @描述：修改分层
	 * @创建时间：  2017年10月26日
	 * @作者：武向楠
	 * @参数： @return      
	 * @return String     
	 * @throws
	 */
	@GetMapping("/toUpdateHierarchyLevelPage")
	public String toUpdateHierarchyLevelPage(HttpServletRequest request) {
		String levelId = null==request.getParameter("levelId")?"":request.getParameter("levelId").trim();
		if (UsedUtil.isNotNull(levelId)) {
			LayerLevelBean layerLevelBean = hierarchyService.selectHierarchyLevelById(Integer.valueOf(levelId));
			request.setAttribute("layerLevelBean", layerLevelBean);
		}
		request.setAttribute("levelId", levelId);
		return "hierarchyMng/levelMng/updateHierarchyLevel";
	}
	
	/**
	 * 
	 * @描述：验证层级是否唯一
	 * @创建时间：  2017年10月26日
	 * @作者：武向楠
	 * @参数： @param map
	 * @参数： @return      
	 * @return String     
	 * @throws
	 */
    @RequestMapping(value="/isOnlyLayerName", method=RequestMethod.POST) 
   	@ResponseBody
   	public String isOnlyLayerName(@RequestBody Map<String,Object> map) {
    	String layerName = (String)map.get("layerName");
    	if(UsedUtil.isNotNull(layerName)){
    		layerName = layerName.trim();
    	}
    	String result = "999";
    	List<LayerLevelBean> layerLevelList = hierarchyService.getHierarchyLevelListByName(layerName);
    	if(null != layerLevelList && layerLevelList.size()>0){
    		result = "999";
    	}else{
    		result = "1000";
    	}
		return result;
    }
    
    /**
     * 
     * @描述：saveUpdateHierarchyLevel
     * @创建时间：  2017年10月26日
     * @作者：武向楠
     * @参数： @param map
     * @参数： @return      
     * @return String     
     * @throws
     */
    @RequestMapping(value="/saveUpdateHierarchyLevel", method=RequestMethod.POST) 
   	@ResponseBody
   	public String saveUpdateHierarchyLevel(@RequestBody Map<String,Object> map) {
    	String result = "999";
		try {
			LayerLevelBean layerLevelBean = hierarchyService.selectHierarchyLevelById(Integer.valueOf((String)map.get("levelId")));
			if(null!=layerLevelBean){
				layerLevelBean.setId(Integer.valueOf((String)map.get("levelId")));
    	    	layerLevelBean.setLayerName((String)map.get("layerName"));
    	    	layerLevelBean.setFloatValue(new BigDecimal((String)map.get("floatValue")));
    	    	layerLevelBean.setFloatUp(new BigDecimal((String)map.get("floatUp")));
    	    	layerLevelBean.setFloatDown(new BigDecimal((String)map.get("floatDown")));
    			result = hierarchyService.updateHierarchyLevel(layerLevelBean);
    			logger.debug("层级修改成功！");
			}
		} catch (Exception e) {
			logger.error("层级修改失败！",e);
			return result;
		}
		return result;
    }
    
    /**
     * 
     * @描述：跳转到违约与回收页面
     * @创建时间：  2017年10月26日
     * @作者：武向楠
     * @参数： @param request
     * @参数： @return      
     * @return String     
     * @throws
     */
    @GetMapping("/toBreakOrRecycleManagerPage")
	public String toBreakOrRecycleManagerPage(HttpServletRequest request) {
    	return "hierarchyMng/breakOrRecycleMng/breakOrRecycleList";
	}
    
    /**
     * 
     * @描述：获取违约率list
     * @创建时间：  2017年11月1日
     * @作者：武向楠
     * @参数： @param request
     * @参数： @return      
     * @return String     
     * @throws
     */
	@PostMapping("/getBreakRateList")
	@ResponseBody
	public String getBreakRateList(HttpServletRequest request) {
		//获取等级list
    	List<String> gradeList = new ArrayList<String>();
    	//获取违约率list
    	List<LinkedHashMap<String, Object>> layerDefaulRatesList = new ArrayList<LinkedHashMap<String, Object>>();
    	try {
    		gradeList = hierarchyService.getGradeList();
        	layerDefaulRatesList = hierarchyService.getLayerDefaulRatesList();
		} catch (Exception e) {
			logger.info("查询违约率表报错！",e);
		}
    	Map<String, Object> resultMap = new HashMap<String,Object>();
    	resultMap.put("gradeList", gradeList);
    	resultMap.put("layerDefaulRatesList", layerDefaulRatesList);
		return JSON.toJSONString(resultMap);
	}
	
	/**
     * 
     * @描述：获取回收率list
     * @创建时间：  2017年11月1日
     * @作者：武向楠
     * @参数： @param request
     * @参数： @return      
     * @return String     
     * @throws
     */
	@PostMapping("/getRecoveryRateList")
	@ResponseBody
	public String getRecoveryRateList(HttpServletRequest request) {
		//获取回收率list A
    	List<LinkedHashMap<String, Object>> layerRecoveryRatesAList = new ArrayList<LinkedHashMap<String, Object>>();
    	//获取回收率list B
    	List<LinkedHashMap<String, Object>> layerRecoveryRatesBList = new ArrayList<LinkedHashMap<String, Object>>();
    	//获取回收率list C
    	List<LinkedHashMap<String, Object>> layerRecoveryRatesCList = new ArrayList<LinkedHashMap<String, Object>>();
    	try {
    		layerRecoveryRatesAList = hierarchyService.getLayerRecoveryRatesList("A");
        	layerRecoveryRatesBList = hierarchyService.getLayerRecoveryRatesList("B");
        	layerRecoveryRatesCList = hierarchyService.getLayerRecoveryRatesList("C");
		} catch (Exception e) {
			logger.info("查询回收率表报错！",e);
		}
    	Map<String, Object> resultMap = new HashMap<String,Object>();
    	resultMap.put("layerRecoveryRatesAList", layerRecoveryRatesAList);
    	resultMap.put("layerRecoveryRatesBList", layerRecoveryRatesBList);
    	resultMap.put("layerRecoveryRatesCList", layerRecoveryRatesCList);
		return JSON.toJSONString(resultMap);
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
    @RequestMapping(value="/saveUpdateDefaulrates", method=RequestMethod.POST) 
   	@ResponseBody
   	public String saveUpdateDefaulrates(@RequestBody Map<String,Object> map) {
    	String result = "999";
		try {
			result = hierarchyService.saveUpdateDefaulrates(map);
		} catch (Exception e) {
			logger.error("违约率修改失败！",e);
			return result;
		}
		return result;
    }
    
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
    @RequestMapping(value="/saveUpdateRecoveryrates", method=RequestMethod.POST) 
   	@ResponseBody
   	public String saveUpdateRecoveryrates(@RequestBody Map<String,Object> map) {
    	String result = "999";
		try {
			result = hierarchyService.saveUpdateRecoveryrates(map);
		} catch (Exception e) {
			logger.error("回收率修改失败！",e);
			return result;
		}
		return result;
    }
    
    

}
