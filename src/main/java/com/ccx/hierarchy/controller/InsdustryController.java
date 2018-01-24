package com.ccx.hierarchy.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import com.ccx.hierarchy.model.InsdustryBean;
import com.ccx.hierarchy.service.InsdustryService;
import com.ccx.system.model.UserBg;
import com.ccx.util.ControllerUtil;
import com.ccx.util.UsedUtil;
import com.ccx.util.page.Page;

/**
 * 
 * @类功能说明：  后台行业管理
 * @作者：武向楠  
 * @创建时间：2017年10月17日 下午2:32:15  
 * @版本：V1.0
 */
@Controller
@RequestMapping("/insdustryManage")
public class InsdustryController extends BasicController {

	private static final Logger logger = LoggerFactory.getLogger(InsdustryController.class);
	
	@Autowired
	private InsdustryService insdustryService;

	/**
     * 
     * @描述：跳转到行业相关性页面
     * @创建时间：  2017年10月26日
     * @作者：武向楠
     * @参数： @param request
     * @参数： @return      
     * @return String     
     * @throws
     */
    @GetMapping("/toCorrelationSetPage")
	public String toCorrelationSetPage(HttpServletRequest request) {
    	String tabFlag = null==request.getParameter("tabFlag")?"":request.getParameter("tabFlag").trim();
    	request.setAttribute("tabFlag", tabFlag);
    	return "hierarchyMng/bgIndustryMng/industryCorrelationList";
	}
    
    /**
     * 
     * @描述：查询资产相关性系数公式
     * @创建时间：  2017年11月1日
     * @作者：武向楠
     * @参数： @param request
     * @参数： @return      
     * @return String     
     * @throws
     */
	@PostMapping("/getAssetCorrelationFormula")
	@ResponseBody
	public String getAssetCorrelationFormula(HttpServletRequest request) {
		List<HashMap<String, Object>> assetCorrelationMapList = new ArrayList<HashMap<String, Object>>();
    	try {
    		//查询资产相关性系数公式
    		assetCorrelationMapList = insdustryService.getAssetCorrelationFormula();
		} catch (Exception e) {
			logger.info("查询资产相关性系数公式失败！",e);
		}
		return JSON.toJSONString(assetCorrelationMapList);
	}
	
	/**
     * 
     * @描述：查询后台行业相关性list
     * @创建时间：  2017年11月1日
     * @作者：武向楠
     * @参数： @param request
     * @参数： @return      
     * @return String     
     * @throws
     */
	@PostMapping("/getInsdustryCorrelationList")
	@ResponseBody
	public String getInsdustryCorrelationList(HttpServletRequest request) {
		List<LinkedHashMap<String, Object>> InsdustryCorrelationList = new ArrayList<LinkedHashMap<String, Object>>();
    	try {
    		//查询后台行业相关性list
    		InsdustryCorrelationList = insdustryService.getInsdustryCorrelationList();
		} catch (Exception e) {
			logger.info("查询后台行业相关性list失败！",e);
		}
		return JSON.toJSONString(InsdustryCorrelationList);
	}
	
	/**
	 * 
	 * @描述：查询后台行业列表
	 * @创建时间：  2017年10月19日
	 * @作者：武向楠
	 * @参数： @return      
	 * @return Page<LayerLevelBean>     
	 * @throws
	 */
	@PostMapping("/findBgInsdustryList")
	@ResponseBody
	public Page<InsdustryBean> findBgInsdustryList(HttpServletRequest request) {
		//获取请求参数
    	Map<String,Object> map = ControllerUtil.request2Map(request);
    	Page<InsdustryBean> pages = new Page<InsdustryBean>();
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
		pages = insdustryService.findBgInsdustryList(pages);
		return pages;
	}
	
	/**
     * 
     * @描述：跳转到新增后台行业页面
     * @创建时间：  2017年10月26日
     * @作者：武向楠
     * @参数： @param request
     * @参数： @return      
     * @return String     
     * @throws
     */
    @GetMapping("/toAddBgInsdustryPage")
	public String toAddBgInsdustryPage(HttpServletRequest request) {
    	return "hierarchyMng/bgIndustryMng/industryAdd";
	}
    
    /**
	 * 
	 * @描述：验证后台行业名称是否唯一
	 * @创建时间：  2017年10月26日
	 * @作者：武向楠
	 * @参数： @param map
	 * @参数： @return      
	 * @return String     
	 * @throws
	 */
    @RequestMapping(value="/isOnlyBgInsdustryName", method=RequestMethod.POST) 
   	@ResponseBody
   	public String isOnlyBgInsdustryName(@RequestBody Map<String,Object> map) {
    	String bgInsdustryName = (String)map.get("bgInsdustryName");
    	if(UsedUtil.isNotNull(bgInsdustryName)){
    		bgInsdustryName = bgInsdustryName.trim();
    	}
    	String result = "999";
    	List<InsdustryBean> insdustryBeanList = insdustryService.getHierarchyLevelListByName(bgInsdustryName);
    	if(null != insdustryBeanList && insdustryBeanList.size()>0){
    		result = "999";
    	}else{
    		result = "1000";
    	}
		return result;
    }
	
	/**
	 * 
	 * @描述：保存新增的后台行业
	 * @创建时间：  2017年10月19日
	 * @作者：武向楠
	 * @参数： @param request
	 * @参数： @return      
	 * @return String     
	 * @throws
	 */
	@RequestMapping(value = "/saveAddBgInsdustry", method = RequestMethod.POST)
	@ResponseBody
	public String saveAddBgInsdustry(@RequestBody Map<String,Object> map) {
		String bgInsdustryName = (String) map.get("bgInsdustryName");
		String result = "999";
		if(UsedUtil.isNotNull(bgInsdustryName)){
			try {
				//查询后台行业表中已经存在的条数
				List<InsdustryBean> insdustryBeanList = insdustryService.findAllBgInsdustryList();
				String code = "";
				if(null == insdustryBeanList || insdustryBeanList.isEmpty()){
					code = String.format("%04d", 1);
				}else{
					code = String.format("%04d", insdustryBeanList.size()+1);
				}
				//获取当前登录用户信息
		    	UserBg userr = (UserBg)ControllerUtil.getSessionUser(request);
		    	InsdustryBean insdustryBean = new InsdustryBean();
		    	insdustryBean.setInsdustryCode(code);
		    	insdustryBean.setInsdustryName(bgInsdustryName);
		    	insdustryBean.setIsDel(0);
		    	insdustryBean.setCreatorName(userr.getLoginName());
		    	insdustryBean.setCreateTime(new Date());
		    	//新增操作
		    	result = insdustryService.saveAddBgInsdustry(insdustryBean);
			} catch (Exception e) {
				logger.info("保存新增后台行业失败！",e);
				return result;
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @描述：查看后台行业
	 * @创建时间：  2017年10月19日
	 * @作者：武向楠
	 * @参数： @param request
	 * @参数： @return      
	 * @return String     
	 * @throws
	 */
	@GetMapping("/toLookBgInsdustryPage")
	public String toLookBgInsdustryPage(HttpServletRequest request) {
		String bgInsdustryId = null==request.getParameter("bgInsdustryId")?"":request.getParameter("bgInsdustryId").trim();
		List<InsdustryBean> insdustryBeanList = new ArrayList<InsdustryBean>();
		InsdustryBean insdustryBean = new InsdustryBean();
		if(UsedUtil.isNotNull(bgInsdustryId)){
			insdustryBean = insdustryService.findInsdustryBean(Integer.valueOf(bgInsdustryId));
			insdustryBeanList =  insdustryService.findMatchInsdustryList(Integer.valueOf(bgInsdustryId));
		}
		request.setAttribute("bgInsdustryId", bgInsdustryId);
		request.setAttribute("insdustryBean", insdustryBean);
		request.setAttribute("insdustryBeanList", insdustryBeanList);
		return "hierarchyMng/bgIndustryMng/bgInsdustryDetail";
	}
	
	/**
     * 
     * @描述：跳转到修改后台行业页面
     * @创建时间：  2017年10月26日
     * @作者：武向楠
     * @参数： @param request
     * @参数： @return      
     * @return String     
     * @throws
     */
    @GetMapping("/toUpdateBgInsdustryPage")
	public String toUpdateBgInsdustryPage(HttpServletRequest request) {
    	String bgInsdustryId = null==request.getParameter("bgInsdustryId")?"":request.getParameter("bgInsdustryId").trim();
    	InsdustryBean insdustryBean = new InsdustryBean();
		if(UsedUtil.isNotNull(bgInsdustryId)){
			insdustryBean = insdustryService.findInsdustryBean(Integer.valueOf(bgInsdustryId));
		}
		request.setAttribute("bgInsdustryId", bgInsdustryId);
		request.setAttribute("insdustryBean", insdustryBean);
    	return "hierarchyMng/bgIndustryMng/industryUpdate";
	}
	
	/**
	 * 
	 * @描述：修改后台行业
	 * @创建时间：  2017年10月19日
	 * @作者：武向楠
	 * @参数： @param request
	 * @参数： @return      
	 * @return String 
	 * @throws
	 */
	@RequestMapping(value = "/updateBgInsdustry", method = RequestMethod.POST)
	@ResponseBody
	public String updateBgInsdustry(@RequestBody Map<String,Object> map) {
		String bgInsdustryId = (String) map.get("bgInsdustryId");
		String bgInsdustryName = (String) map.get("bgInsdustryName");
		String result = "999";
		if (UsedUtil.isNotNull(bgInsdustryId) && UsedUtil.isNotNull(bgInsdustryName)) {
			try {
		    	InsdustryBean insdustryBean = insdustryService.findInsdustryBean(Integer.valueOf(bgInsdustryId));
		    	insdustryBean.setInsdustryName(bgInsdustryName);
		    	//新增操作
		    	result = insdustryService.updateBgInsdustry(insdustryBean,"update");
			} catch (Exception e) {
				logger.info("保存新增后台行业失败！",e);
				return result;
			}
		} else {
			logger.info("传参错误，导致修改失败！");
			return result;
		}
		return result;
	}
	
	/**
	 * 
	 * @描述：匹配前台行业
	 * @创建时间：  2017年10月19日
	 * @作者：武向楠
	 * @参数： @return      
	 * @return String     
	 * @throws
	 */
	@GetMapping("/toMatchFgInsdustryPage")
	public String toMatchFgInsdustryPage(HttpServletRequest request) {
		String bgInsdustryId = null==request.getParameter("bgInsdustryId")?"":request.getParameter("bgInsdustryId").trim();
		List<InsdustryBean> insdustryBeanList = new ArrayList<InsdustryBean>();
		InsdustryBean insdustryBean = new InsdustryBean();
		if(UsedUtil.isNotNull(bgInsdustryId)){
			insdustryBeanList =  insdustryService.findMatchInsdustryList(Integer.valueOf(bgInsdustryId));
			insdustryBean = insdustryService.findInsdustryBean(Integer.valueOf(bgInsdustryId));
		}
		request.setAttribute("insdustryBeanList", insdustryBeanList);
		request.setAttribute("insdustryBean", insdustryBean);
		request.setAttribute("bgInsdustryId", bgInsdustryId);
		return "hierarchyMng/bgIndustryMng/matchFgInsdustry";
	}
	
	/**
	 * 
	 * @描述：删除所匹配的前台行业
	 * @创建时间： 2017年10月19日
	 * @作者：武向楠
	 * @参数： @param request
	 * @参数： @return      
	 * @return String 
	 * @throws
	 */
	@RequestMapping(value = "/deleteMatchInsdustry", method = RequestMethod.POST)
	@ResponseBody
	public String deleteMatchInsdustry(@RequestBody Map<String,Object> map) {
		String matchId = (String) map.get("matchId");
		String bgInsdustryId = (String) map.get("bgInsdustryId");
		String result = "999";
		if (UsedUtil.isNotNull(matchId)) {
			try {
		    	//删除操作
		    	result = insdustryService.deleteMatchInsdustry(Integer.valueOf(matchId),Integer.valueOf(bgInsdustryId));
			} catch (Exception e) {
				logger.info("删除所匹配的前台行业失败！",e);
				return result;
			}
		} else {
			logger.info("删除所匹配的前台行业失败！");
			return result;
		}
		return result;
	}
	
	/**
	 * 
	 * @描述：查询剩余可以添加的前台行业list(分页)
	 * @创建时间： 2017年10月19日
	 * @作者：武向楠
	 * @参数： @param request
	 * @参数： @return      
	 * @return String 
	 * @throws
	 */
	@RequestMapping(value = "/selectCanMatchInsdustry", method = RequestMethod.POST)
	@ResponseBody
	public Page<InsdustryBean> selectCanMatchInsdustry(HttpServletRequest request) {
		//获取请求参数
    	Map<String,Object> map = ControllerUtil.request2Map(request);
    	Page<InsdustryBean> pages = new Page<InsdustryBean>();
    	Map<String,Object> params = new HashMap<String,Object>();
    	logger.info("参数=========================>"+map);
    	//当前后台id
    	String bgInsdustryId = (String) map.get("bgInsdustryId");
    	params.put("bgInsdustryId", bgInsdustryId);
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
		pages = insdustryService.selectCanMatchInsdustry(pages);
		return pages;
	}
	
	/**
	 * 
	 * @描述：保存匹配的前台行业
	 * @创建时间：  2017年10月31日
	 * @作者：武向楠
	 * @参数： @param map
	 * @参数： @return      
	 * @return String     
	 * @throws
	 */
	@RequestMapping(value = "/addMatchInsdustry", method = RequestMethod.POST)
	@ResponseBody
	public String addMatchInsdustry(@RequestBody Map<String,Object> map) {
		String bgInsdustryId = (String) map.get("bgInsdustryId");
		String matchSecondInsdustryIdStr = (String) map.get("matchSecondInsdustryIdStr");
		String result = "999";
		if (UsedUtil.isNotNull(bgInsdustryId)) {
			if (UsedUtil.isNotNull(matchSecondInsdustryIdStr)) {
				try {
					matchSecondInsdustryIdStr = matchSecondInsdustryIdStr.substring(0,matchSecondInsdustryIdStr.lastIndexOf(","));
			    	String[] matchSecondInsdustryIdStrList = matchSecondInsdustryIdStr.split(",");
			    	int[] matchSecondInsdustryIdList = new int[matchSecondInsdustryIdStrList.length];
			    	for(int i = 0; i < matchSecondInsdustryIdStrList.length; i++){
			    	    //先由字符串转换成char,再转换成String,然后Integer
			    		matchSecondInsdustryIdList[i] = Integer.parseInt(matchSecondInsdustryIdStrList[i]);
			    	}
			    	Map<String,Object> paramMap = new HashMap<String,Object>();
			    	paramMap.put("bgInsdustryId", Integer.valueOf(bgInsdustryId));
			    	paramMap.put("matchSecondInsdustryIdList", matchSecondInsdustryIdList);
			    	result = insdustryService.addMatchInsdustry(paramMap);
				} catch (Exception e) {
					logger.info("保存匹配的前台行业失败！",e);
					return result;
				}
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @描述：删除行业
	 * @创建时间： 2017年10月19日
	 * @作者：武向楠
	 * @参数： @param request
	 * @参数： @return      
	 * @return String 
	 * @throws
	 */
	@RequestMapping(value = "/deleteBgInsdustry", method = RequestMethod.POST)
	@ResponseBody
	public String deleteBgInsdustry(@RequestBody Map<String,Object> map) {
		String bgInsdustryId = (String) map.get("bgInsdustryId");
		String result = "999";
		if (UsedUtil.isNotNull(bgInsdustryId)) {
			try {
				InsdustryBean insdustryBean = insdustryService.findInsdustryBean(Integer.valueOf(bgInsdustryId));
		    	insdustryBean.setIsDel(1);;
		    	//删除操作
		    	result = insdustryService.updateBgInsdustry(insdustryBean,"delete");
			} catch (Exception e) {
				logger.info("删除后台行业失败！",e);
				return result;
			}
		} else {
			logger.info("删除后台行业失败！");
			return result;
		}
		return result;
	}
	
	/**
	 * 
	 * @描述：保存资产相关性系数公式设置
	 * @创建时间： 2017年10月19日
	 * @作者：武向楠
	 * @参数： @param request
	 * @参数： @return      
	 * @return String 
	 * @throws
	 */
	@RequestMapping(value = "/updateCorrelationFormula", method = RequestMethod.POST)
	@ResponseBody
	public String updateCorrelationFormula(@RequestBody Map<String,Object> map) {
		String correlationSetupId = (String) map.get("correlationSetupId");
		String paramValue = (String) map.get("paramValue");
		String result = "999";
		if (UsedUtil.isNotNull(correlationSetupId)&&UsedUtil.isNotNull(paramValue)) {
			try {
		    	//保存资产相关性系数公式设置
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("correlationSetupId", Integer.valueOf(correlationSetupId));
				paramMap.put("paramValue", new BigDecimal(paramValue));
		    	result = insdustryService.updateCorrelationFormula(paramMap);
			} catch (Exception e) {
				logger.info("保存资产相关性系数公式设置失败！",e);
				return result;
			}
		} else {
			logger.info("保存资产相关性系数公式设置失败！");
			return result;
		}
		return result;
	}
	
	/**
	 * 
	 * @描述：保存行业相关性数据
	 * @创建时间：  2017年11月2日
	 * @作者：武向楠
	 * @参数： @param map
	 * @参数： @return      
	 * @return String     
	 * @throws
	 */
	@RequestMapping(value = "/saveBgInsdustryCorrelation", method = RequestMethod.POST)
	@ResponseBody
	public String saveBgInsdustryCorrelation(@RequestBody Map<String,Object> map) {
		String correlationId1 = (String) map.get("correlationId1");
		String correlationId2 = (String) map.get("correlationId2");
		String correlationValue = (String) map.get("correlationValue");
		String result = "999";
		if (UsedUtil.isNotNull(correlationId1)&&UsedUtil.isNotNull(correlationId2)&&UsedUtil.isNotNull(correlationValue)) {
			try {
		    	//保存资产相关性系数公式设置
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("correlationId1", Integer.valueOf(correlationId1));
				paramMap.put("correlationId2", Integer.valueOf(correlationId2));
				paramMap.put("correlationValue", new BigDecimal(correlationValue));
		    	result = insdustryService.saveBgInsdustryCorrelation(paramMap);
			} catch (Exception e) {
				logger.info("保存行业相关性数据失败！",e);
				return result;
			}
		} else {
			logger.info("保存行业相关性数据失败！");
			return result;
		}
		return result;
	}
	
	
	

}
