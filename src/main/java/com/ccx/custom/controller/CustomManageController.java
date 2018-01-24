package com.ccx.custom.controller;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.ccx.base.BasicController;
import com.ccx.custom.model.IPBean;
import com.ccx.custom.model.Institution;
import com.ccx.custom.model.Module;
import com.ccx.custom.model.RoleFg;
import com.ccx.custom.model.UserFg;
import com.ccx.custom.model.UserNumBean;
import com.ccx.custom.model.UserRoleFg;
import com.ccx.custom.model.UserVoFg;
import com.ccx.custom.service.CustomManageService;
import com.ccx.dictionary.model.EnterpriseNature;
import com.ccx.dictionary.service.EnterpriseNatureService;
import com.ccx.enterprise.model.EnterpriseIndustry;
import com.ccx.enterprise.service.EnterpriseIndustryService;
import com.ccx.system.model.UserBg;
import com.ccx.util.ControllerUtil;
import com.ccx.util.DateUtils;
import com.ccx.util.EnterpriseDataUtils;
import com.ccx.util.MD5;
import com.ccx.util.UsedUtil;
import com.ccx.util.page.Page;



/**
 * 
* @ClassName: institutionManage 
* @Description: 机构管理(这里用一句话描述这个类的作用) 
* @author WXN 
* @date 2017年7月3日 下午1:37:05 
*
 */
@Controller
@RequestMapping("/custom")
public class CustomManageController extends BasicController{
	
	private static final Logger logger = LoggerFactory.getLogger(CustomManageController.class);
	@Autowired
	private CustomManageService customManageService;
	@Autowired
	private EnterpriseNatureService enterpriseNatureService;
	/*行业*/
	@Autowired
	private EnterpriseIndustryService industryService;
	
	/**
     * 机构管理页
     *
     * @return
     */
    @GetMapping("/insManage")
    public String insManage() {
        return "customMng/insList";
    }
    
    /**
     * 创建机构页
     *
     * @return
     */
    @GetMapping("/insAdd")
    public String insAdd() {
        return "customMng/insAdd";
    }
    
    /**
     * 查询所有机构列表
     * @Title: listdata  
     * @author: WXN
     * @Description: 查询所有机构列表(这里用一句话描述这个方法的作用)   
     * @param: @param map
     * @param: @return      
     * @return: Page<Institution>      
     * @throws
     */
    @RequestMapping(value="/findAllIns",method=RequestMethod.POST)
	@ResponseBody
	public Page<Institution> findAllIns(HttpServletRequest request){
    	//获取请求参数
    	Map<String,Object> map = ControllerUtil.request2Map(request);
    	Page<Institution> pages = new Page<Institution>();
    	Map<String,Object> params = new HashMap<String,Object>();
    	logger.info("参数=========================>"+map);
		params.put("keyWord", map.get("keyWord"));
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
		pages = customManageService.findAllIns(pages);
		return pages;
    }
    
    /**
     * 
     * @Title: findInsNuture  
     * @author: WXN
     * @Description: 查询机构性质(这里用一句话描述这个方法的作用)   
     * @param: @param map
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/findInsNuture",method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ResponseBody
	public String findInsNuture(@RequestBody Map<String,Object> map){
    	List<EnterpriseNature> insNutureList = enterpriseNatureService.findAllNature();
		return JSON.toJSONString(insNutureList);
    }
    
    /**
     * 
     * @Title: findInsFirstIndustry  
     * @author: WXN
     * @Description: 查询机构行业(这里用一句话描述这个方法的作用)   
     * @param: @param map
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/findInsFirstIndustry",method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ResponseBody
	public String findInsFirstIndustry(@RequestBody Map<String,Object> map){
    	List<EnterpriseIndustry> insNutureList = industryService.findAllIndustryByPid(0);
		return JSON.toJSONString(insNutureList);
    }
    
    /**
	 * 异步加载二级行业
	 */
	@ResponseBody
	@PostMapping("/getIndustry")
	public Map<String, Object> getIndustry(HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<>();
		
		//加载二级行业
		resultMap = EnterpriseDataUtils.loadIndustry(request, industryService);
		
		return resultMap;
	}
	
	/**
	 * 
	 * @描述：校验机构是否已存在
	 * @创建时间：  2017年8月16日
	 * @作者：武向楠
	 * @参数： @param request
	 * @参数： @return      
	 * @return Map<String,String>     
	 * @throws
	 */
	@RequestMapping(value="/checkInsName",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> checkInsName(HttpServletRequest request){
		Map<String,String> map = new HashMap<String,String>();
		String insName = request.getParameter("insName");
		List<Institution> institution = customManageService.getInstitutionByName(insName);
		if(null != institution && institution.size()>0 ){
			map.put("result", "0");
		}else{
			map.put("result", "1");
		}
		return map;
	}
	
	/**
	 * 
	 * @描述：检验组织代码是否唯一
	 * @创建时间：  2017年9月4日
	 * @作者：武向楠
	 * @参数： @param request
	 * @参数： @return      
	 * @return Map<String,String>     
	 * @throws
	 */
	@RequestMapping(value="/checkInsOrganizationCode",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> checkInsOrganizationCode(HttpServletRequest request){
		Map<String,String> map = new HashMap<>();
		String organizationCodeFlag = request.getParameter("organizationCodeFlag");
		String organizationCode = request.getParameter("organizationCode");
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("organizationCodeFlag", organizationCodeFlag);
		paramMap.put("organizationCode", organizationCode);
		List<Institution> institution = customManageService.checkInsOrganizationCode(paramMap);
		if(null != institution && institution.size()>0 ){
			map.put("result", "0");
		}else{
			map.put("result", "1");
		}
		return map;
	}
    
    /**
     * 
     * @Title: saveCsutomAdd  
     * @author: WXN
     * @Description: 新增机构(这里用一句话描述这个方法的作用)   
     * @param: @param institution
     * @param: @param request
     * @param: @param response
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/saveCsutomAdd",method=RequestMethod.POST)
   	@ResponseBody
   	public String saveCsutomAdd(@RequestBody Institution institution, HttpServletRequest request){
    	//获取当前登录用户信息
    	UserBg user = (UserBg)ControllerUtil.getSessionUser(request);
		//添加创建人
		if (null != user) {
			//添加创建人
	    	institution.setCreater(user.getLoginName());
		}
		//添加创建时间
    	institution.setCreatetime(new Date());
		//添加修改时间
    	institution.setModifytime(new Date());
    	//添加审核状态
    	int examinestate = 1;
    	institution.setExaminestate(examinestate);
    	//添加使用状态
    	int state = 1;
    	institution.setState(state);
    	logger.info(institution.toString());
    	//进行保存操作
    	String s = customManageService.saveCsutomAdd(institution);
    	return s;
    }
    
    /**
     * 
     * @描述：跳转到编辑机构页面
     * @创建时间：  2017年8月16日
     * @作者：武向楠
     * @参数： @param request
     * @参数： @return      
     * @return String     
     * @throws
     */
	@RequestMapping(value = "/insUpdate", method = RequestMethod.GET)
	public String insUpdate(HttpServletRequest request) {
		Long insId = (long) Integer.parseInt(request.getParameter("id"));
		// 根据机构id查询到对应的机构实体
		Institution institution = customManageService.selectInstitutionById(insId);
		String industryFirst = institution.getIndustryFirst();
		Integer industryFirstId = null;
		//调xiezhaodong方法获取所有一级行业
		List<EnterpriseIndustry> insNutureList = industryService.findAllIndustryByPid(0);
		for (int i = 0; i < insNutureList.size(); i++) {
			if(industryFirst == insNutureList.get(i).getName() || industryFirst.equals(insNutureList.get(i).getName())){
				industryFirstId = insNutureList.get(i).getId();
				break;
			}
		}
		String industrySecond = institution.getIndustrySecond();
		Integer industrySecondId = null;
		//调xiezhaodong方法获取二级行业
		List<EnterpriseIndustry> insNutureList2 = industryService.findAllIndustryByPid(industryFirstId);
		for (int i = 0; i < insNutureList2.size(); i++) {
			if(industrySecond == insNutureList2.get(i).getName() || industrySecond.equals(insNutureList2.get(i).getName())){
				industrySecondId = insNutureList2.get(i).getId();
				break;
			}
		}
		request.setAttribute("industryFirstId", industryFirstId);
		request.setAttribute("industrySecondId", industrySecondId);
		request.setAttribute("institution", institution);
		request.setAttribute("insId", insId);
		return "customMng/insUpdate";
	}
    
    /**
     * 
     * @Title: saveCsutomEdit  
     * @author: WXN
     * @Description: 编辑机构(这里用一句话描述这个方法的作用)   
     * @param: @param institution
     * @param: @param request
     * @param: @param response
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/saveCsutomEdit",method=RequestMethod.POST)
   	@ResponseBody
   	public String saveCsutomEdit(@RequestBody Institution institution,HttpServletRequest request,
			HttpServletResponse response){
    	//获取当前登录用户信息
    	UserBg user = (UserBg)ControllerUtil.getSessionUser(request);
		if (null != user) {
			//添加修改人
	    	institution.setModifier(user.getLoginName());
		}
		//添加修改时间
    	institution.setModifytime(new Date());
    	//添加审核状态
    	int examinestate = 1;
    	institution.setExaminestate(examinestate);
    	logger.info(institution.toString());
    	//进行保存操作
    	String s = customManageService.saveCsutomEdit(institution);
    	return s;
    }
    
    /**
     * 
     * @描述：跳转到查看机构页面
     * @创建时间：  2017年8月16日
     * @作者：武向楠
     * @参数： @param request
     * @参数： @return      
     * @return String     
     * @throws
     */
	@RequestMapping(value = "/lookDetailIns", method = RequestMethod.GET)
	public String lookDetailIns(HttpServletRequest request) {
		Long insId = (long) Integer.parseInt(request.getParameter("id"));
		// 根据机构id查询到对应的机构实体
		Institution institution = customManageService.selectInstitutionById(insId);
		request.setAttribute("institution", institution);
		request.setAttribute("insId", insId);
		return "customMng/insDetail";
	}
    
    /**
     * 
     * @描述：跳转到审核机构页面
     * @创建时间：  2017年8月16日
     * @作者：武向楠
     * @参数： @param request
     * @参数： @return      
     * @return String     
     * @throws
     */
	@RequestMapping(value = "/examineIns", method = RequestMethod.GET)
	public String examineIns(HttpServletRequest request) {
		Long insId = (long) Integer.parseInt(request.getParameter("id"));
		// 根据机构id查询到对应的机构实体
		Institution institution = customManageService.selectInstitutionById(insId);
		request.setAttribute("institution", institution);
		request.setAttribute("insId", insId);
		return "customMng/insExamine";
	}
    
    /**
     * 
     * @Title: csutomExamine  
     * @author: WXN
     * @Description: 机构审核(这里用一句话描述这个方法的作用)   
     * @param: @param institution
     * @param: @param request
     * @param: @param response
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/csutomExamine",method=RequestMethod.POST)
   	@ResponseBody
   	public String csutomExamine(@RequestBody Institution institution,HttpServletRequest request,
			HttpServletResponse response){
    	//获取当前登录用户信息
    	UserBg user = (UserBg)ControllerUtil.getSessionUser(request);
		if (null != user) {
			//添加修改人
	    	institution.setModifier(user.getLoginName());
		}
		//添加修改时间
    	institution.setModifytime(new Date());
    	logger.info(institution.toString());
    	//进行保存操作
    	String s = customManageService.csutomExamine(institution);
    	return s;
    }
    
    
    /**
     * 机构管理——账号管理页
     *
     * @return
     */
    @GetMapping("/insAccountManager")
    public String insAccountManager() {
        return "customMng/insAccountList";
    }
    
    /**
     * 查询所有机构列表
     * @Title: listdata  
     * @author: WXN
     * @Description: 查询所有审核通过的机构列表(这里用一句话描述这个方法的作用)   
     * @param: @param map
     * @param: @return      
     * @return: Page<Institution>      
     * @throws
     */
    @RequestMapping(value="/findAllInsAccountList",method=RequestMethod.POST)
	@ResponseBody
	public Page<Institution> findAllInsAccountList(HttpServletRequest request){
    	//获取请求参数
    	Map<String,Object> map = ControllerUtil.request2Map(request);
    	Page<Institution> pages = new Page<Institution>();
    	Map<String,Object> params = new HashMap<String,Object>();
    	logger.info("参数=========================>"+map);
		//增加查询条件，只查询启用，并且在使用中的账号
    	params.put("examinestate", "3");
//		params.put("state", "1");
		params.put("keyWord", map.get("keyWord"));
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
		pages = customManageService.findAllIns(pages);
		return pages;
    }
    
    /**
     * 
     * @描述：查询该该机构是否已分配有模块，若未分配，则先进性模块分配才能进行用户管理和角色管理和ip管理
     * @创建时间：  2017年8月23日
     * @作者：武向楠
     * @参数： @param request
     * @参数： @param response
     * @参数： @return      
     * @return String     
     * @throws
     */
    @RequestMapping(value="/checkIsHaveModule",method=RequestMethod.POST)
   	@ResponseBody
   	public String checkIsHaveModule(HttpServletRequest request,
			HttpServletResponse response){
    	//获取请求参数
    	Map<String,Object> map = ControllerUtil.request2Map(request);
    	String insId = (String) map.get("insId");
       	List<Module> moduleList = new ArrayList<Module>();
       	if(null != insId && "" != insId){
       	  //查询该机构已经分配的模块
          moduleList = customManageService.getModuleByInsId(Long.parseLong(insId));
          moduleList = moduleList==null?new ArrayList<Module>():moduleList;
       	}
       	String result = "9999";
    	if(null != moduleList && moduleList.size()>0 ){
    		result = "0000";
    	}else{
    		result = "9999";
    	}
    	return result;
    }
    
    /**
     * 跳转模块分配页
     *
     * @return
     */
    @GetMapping("/toModuleAllocationPage")
    public String toModuleAllocationPage(HttpServletRequest request,HttpServletResponse response) {
    	Long insId = (long) Integer.parseInt(request.getParameter("id"));
        // 根据机构id查询到对应的机构实体
     	Institution institution = customManageService.selectInstitutionById(insId);
        request.setAttribute("insId", insId);
        request.setAttribute("institution", institution);
    	return "customMng/moduleList";
    }
    
    /**
     * 
     * @Title: getModuleByInsId  
     * @author: WXN
     * @Description: 根据机构id获取模块list(这里用一句话描述这个方法的作用)   
     * @param: @param map
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/getModuleByInsId",method=RequestMethod.POST,produces = "application/json; charset=utf-8")
   	@ResponseBody
   	public Page<Module> getModuleByInsId(HttpServletRequest request){
    	//获取请求参数
    	Map<String,Object> map = ControllerUtil.request2Map(request);
       	logger.info("参数=========================>"+map);
       	String insId = (String) map.get("insId");
       	//分页结果集
    	Page<Module> pages = new Page<Module>();
    	try {
    		//获取当前登录时间，用于与模块到期时间对比
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
            String currentDate = df.format(new Date());
        	//查询前，先查出该机构所有模块，并判断是否存在到期，如果到期则更改状态
        	List<Module> allModuleList = new ArrayList<Module>();
            //查询该所有模块
	    	allModuleList = customManageService.getModuleByInsId(Long.parseLong(insId));
	       	if (null != allModuleList && allModuleList.size()>0) {
				for (int i = 0; i < allModuleList.size(); i++) {
					String endDate = allModuleList.get(i).getEndDate();
					String startDate = allModuleList.get(i).getStartDate();
					long moduleId = allModuleList.get(i).getId();
					Date oDate = df.parse(currentDate);
	        		Date fDate = df.parse(endDate);
	        		Date sDate = df.parse(startDate);
	        		Module module = new Module();
	        		//计算当前时间与模块到期时间差
	        		int days = DateUtils.daysOfTwo(fDate,oDate);
	        		//计算当前时间与模块开始时间差
	        		int days2 = DateUtils.daysOfTwo(sDate,oDate);
	        		//状态(0:启用中;1:暂停中;2:未开始;3:已到期;)
					if(days<0){
	        			module.setState(3);
	        		}else{
	        			if(days2>0){
	            			module.setState(2);
	            		}else{
							if(allModuleList.get(i).getState() != 1){
								module.setState(0);
							}
	            		}
	        		}
	        		module.setId(moduleId);
					if(null != module.getState()){
						customManageService.updateModuleState(module);
					}
				}
			}
	       	Map<String,Object> params = new HashMap<String,Object>();
			//增加查询条件
			params.put("insId", map.get("insId"));
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
//			pages = customManageService.getModuleListByInsId(pages);
			pages = customManageService.getAllModuleList(pages);
    	} catch (ParseException e) {
			e.printStackTrace();
		}
		return pages;
    }
    
    /**
     * 
     * @描述：校验是否该机构还有未分配的模块，如果有，则进入分配页面；如果没有，则不进入
     * @创建时间：  2017年8月17日
     * @作者：武向楠
     * @参数： @param request
     * @参数： @param response
     * @参数： @return      
     * @return String     
     * @throws
     */
    @RequestMapping(value="/isHaveWeiFenPeiModule",method=RequestMethod.POST)
   	@ResponseBody
   	public String isHaveWeiFenPeiModule(HttpServletRequest request,
			HttpServletResponse response){
    	//获取请求参数
    	Map<String,Object> map = ControllerUtil.request2Map(request);
    	String insId = (String) map.get("insId");
       	List<Module> allModuleList = new ArrayList<Module>();
        //查询该所有模块
       	allModuleList = customManageService.getAllModule();
       	allModuleList = allModuleList==null?new ArrayList<Module>():allModuleList;
       	List<Module> moduleList = new ArrayList<Module>();
       	if(null != insId && "" != insId){
       	  //查询该机构已经分配的模块
          moduleList = customManageService.getModuleByInsId(Long.parseLong(insId));
          moduleList = moduleList==null?new ArrayList<Module>():moduleList;
       	}
       	//临时模块列表
       	List<Module> weiFenModuleList = new ArrayList<Module>();
       	if(null != allModuleList && allModuleList.size()>0){
       		if(null != moduleList && moduleList.size()>0){
       			for (int i = 0; i < allModuleList.size(); i++) {
           			Module module1 = allModuleList.get(i);
           			String myselfId1 = module1.getMyselfId();
           			for (int j = 0; j < moduleList.size(); j++) {
           				Module module2 = moduleList.get(j);
               			String myselfId2 = module2.getMyselfId();
               			if(myselfId2 == myselfId1 || myselfId2.equals(myselfId1)){
               				weiFenModuleList.add(module1);
               			}
					}
    			}
       			allModuleList.removeAll(weiFenModuleList);
       		}
       	}
       	String result = "0000";
    	if(null==allModuleList||allModuleList.size()<1){
    		result = "0000";
    	}else{
    		result = "9999";
    	}
    	return result;
    }
    
    /**
     * 跳转新增模块分配页
     *
     * @return
     */
    @GetMapping("/toAddModuleAllocaPage")
    public String toAddModuleAllocaPage(HttpServletRequest request,HttpServletResponse response) {
    	Long insId = (long) Integer.parseInt(request.getParameter("id"));
        // 根据机构id查询到对应的机构实体
     	Institution institution = customManageService.selectInstitutionById(insId);
        request.setAttribute("insId", insId);
        request.setAttribute("institution", institution);
    	return "customMng/moduleAdd";
    }
    
    /**
     * 
     * @Title: getModuleByInsId  
     * @author: WXN
     * @Description: 根据机构id获取未分配模块list(这里用一句话描述这个方法的作用)   
     * @param: @param map
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/getWeiFenPeiModuleList",method=RequestMethod.POST,produces = "application/json; charset=utf-8")
   	@ResponseBody
   	public String getWeiFenPeiModuleList(@RequestBody Map<String,Object> map){
       	logger.info("参数=========================>"+map);
       	Map<String,Object> resultMap = new HashMap<String,Object>();
       	String insId = (String) map.get("insId");
       	List<Module> allModuleList = new ArrayList<Module>();
        //查询该所有模块
       	allModuleList = customManageService.getAllModule();
       	allModuleList = allModuleList==null?new ArrayList<Module>():allModuleList;
       	List<Module> moduleList = new ArrayList<Module>();
       	if(null != insId && "" != insId){
       	  //查询该机构已经分配的模块
          moduleList = customManageService.getModuleByInsId(Long.parseLong(insId));
          moduleList = moduleList==null?new ArrayList<Module>():moduleList;
       	}
       	//临时模块列表
       	List<Module> weiFenModuleList = new ArrayList<Module>();
       	if(null != allModuleList && allModuleList.size()>0){
       		if(null != moduleList && moduleList.size()>0){
       			for (int i = 0; i < allModuleList.size(); i++) {
           			Module module1 = allModuleList.get(i);
           			String myselfId1 = module1.getMyselfId();
           			for (int j = 0; j < moduleList.size(); j++) {
           				Module module2 = moduleList.get(j);
               			String myselfId2 = module2.getMyselfId();
               			if(myselfId2 == myselfId1 || myselfId2.equals(myselfId1)){
               				weiFenModuleList.add(module1);
               			}
					}
    			}
       			allModuleList.removeAll(weiFenModuleList);
       		}
       	}
       	resultMap.put("allModuleList", allModuleList);
   		return JSON.toJSONString(resultMap);
    }
    
    /**
     * 
     * @Title: saveModuleAdd  
     * @author: WXN
     * @Description: 新增模块(这里用一句话描述这个方法的作用)   
     * @param: @param map
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/saveModuleAdd",method=RequestMethod.POST)
   	@ResponseBody
   	public String saveModuleAdd(@RequestBody Map<String,Object> map){
    	logger.info("参数=========================>"+map);
    	String result = "999";
    	try {
    		//获取当前登录用户信息
    		UserBg user = (UserBg)ControllerUtil.getSessionUser(request);
        	String insId = (String) map.get("insId");
        	String description = (String) map.get("description");
        	if(UsedUtil.isNotNull(description)){
        		description = description.trim();
        	}
        	String myselfId = (String) map.get("myselfId");
        	String moduleName = (String) map.get("moduleName");
        	String startDate = (String) map.get("startDate");
        	String endDate = (String) map.get("endDate");
        	myselfId = myselfId.substring(0, myselfId.length()-1);
        	moduleName = moduleName.substring(0, moduleName.length()-1);
        	startDate = startDate.substring(0, startDate.length()-1);
        	endDate = endDate.substring(0, endDate.length()-1);
        	String[] myselfIdList = myselfId.split(",");
        	String[] moduleNameList = moduleName.split(",");
        	String[] startDateList = startDate.split(",");
        	String[] endDateList = endDate.split(",");
        	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
        	List<Module> moduleList = new ArrayList<Module>();
        	for (int i = 0; i < myselfIdList.length; i++) {
        		Module module = new Module();
        		module.setMyselfId(myselfIdList[i]);
        		module.setName(moduleNameList[i]);
        		try {
    				module.setStartDate(sdf.parse(startDateList[i]));
    				module.setEndDate(sdf.parse(endDateList[i]));
    			} catch (ParseException e) {
    				e.printStackTrace();
    			}
        		module.setInstitutionId(Integer.valueOf(insId));
        		module.setDescription(description);
        		//添加创建人
        		if (null != user) {
        			//添加分配人
        			module.setAllor(user.getLoginName());
        			//添加创建人
        			module.setCreater(user.getLoginName());
        		}
        		//添加分配时间
        		module.setAllorTime(new Date());
        		//添加创建时间
        		module.setCreateTime(new Date());
        		//添加状态
        		module.setState(0);
        		module.setIsDel(0);
        		moduleList.add(module);
    		}
        	result = customManageService.saveModuleAdd(moduleList);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return result;
    }
    
    /**
     * 
     * @Title: saveNewModuleAdd  
     * @author: WXN
     * @Description: 新增模块--新
     * @param: @param map
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/saveNewModuleAdd",method=RequestMethod.POST)
   	@ResponseBody
   	public String saveNewModuleAdd(@RequestBody Map<String,Object> map){
    	logger.info("参数=========================>"+map);
    	String result = "999";
    	try {
    		//获取当前登录用户信息
    		UserBg user = (UserBg)ControllerUtil.getSessionUser(request);
        	String insId = (String) map.get("insId");
        	String description = (String) map.get("description");
        	if(UsedUtil.isNotNull(description)){
        		description = description.trim();
        	}
        	String myselfId = (String) map.get("myselfId");
        	String moduleName = (String) map.get("moduleName");
        	String startDate = (String) map.get("startDate");
        	String endDate = (String) map.get("endDate");
        	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        	Module module = new Module();
    		module.setMyselfId(myselfId);
    		module.setName(moduleName);
			module.setStartDate(sdf.parse(startDate));
			module.setEndDate(sdf.parse(endDate));
    		module.setInstitutionId(Integer.valueOf(insId));
    		module.setDescription(description);
    		//添加创建人
    		if (null != user) {
    			//添加分配人
    			module.setAllor(user.getLoginName());
    			//添加创建人
    			module.setCreater(user.getLoginName());
    		}
    		//添加分配时间
    		module.setAllorTime(new Date());
    		//添加创建时间
    		module.setCreateTime(new Date());
    		//添加状态
    		String currentDate = sdf.format(new Date());
    		Date oDate = sdf.parse(currentDate);
    		Date fDate = sdf.parse(startDate);
    		Date eDate = sdf.parse(endDate);
    		//计算当前时间与模块开始时间差
    		int days = DateUtils.daysOfTwo(fDate,oDate);
    		//计算当前时间与模块到期时间差
    		int days2 = DateUtils.daysOfTwo(eDate,oDate);
    		//状态(0:启用中;1:暂停中;2:未开始;3:已到期;)
    		if(days2<0){
    			module.setState(3);
    		}else{
    			if(days>0){
        			module.setState(2);
        		}else{
        			module.setState(0);
        		}
    		}
    		module.setIsDel(0);
        	result = customManageService.saveNewModuleAdd(module);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return result;
    }
    
    /**
     * 
     * @Title: saveModuleEdit  
     * @author: WXN
     * @Description: 保存模块修改(这里用一句话描述这个方法的作用)   
     * @param: @param map
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/saveModuleEdit",method=RequestMethod.POST)
   	@ResponseBody
   	public String saveModuleEdit(@RequestBody Module module){
    	logger.info("参数=========================>"+module.toString());
    	String result = "999";
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    	try {
    		//获取当前登录用户信息
    		UserBg user = (UserBg)ControllerUtil.getSessionUser(request);
    		//添加创建人
    		if (null != user) {
    			//添加分配人
    			module.setAllor(user.getLoginName());
    		}
    		//添加分配时间
    		module.setAllorTime(new Date());
    		String currentDate = sdf.format(new Date());
    		Date oDate = sdf.parse(currentDate);
    		Date fDate = sdf.parse(module.getStartDate());
    		Date eDate = sdf.parse(module.getEndDate());
    		//计算当前时间与模块开始时间差
    		int days = DateUtils.daysOfTwo(fDate,oDate);
    		//计算当前时间与模块到期时间差
    		int days2 = DateUtils.daysOfTwo(eDate,oDate);
    		//状态(0:启用中;1:暂停中;2:未开始;3:已到期;)
    		if(days2<0){
    			module.setState(3);
    		}else{
    			if(days>0){
        			module.setState(2);
        		}else{
        			module.setState(0);
        		}
    		}
    		result = customManageService.saveModuleEdit(module);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return result;
    }
    
    /**
     * 
     * @Title: updateModuleState  
     * @author: WXN
     * @Description: 修改模块状态(这里用一句话描述这个方法的作用)   
     * @param: @param module
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/updateModuleState",method=RequestMethod.POST)
   	@ResponseBody
   	public String updateModuleState(@RequestBody Module module){
    	logger.info("参数=========================>"+module.toString());
    	String result = "999";
    	try {
    		result = customManageService.updateModuleState(module);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return result;
    }
    
    /**
     * 跳转机构角色管理页
     *
     * @return
     */
    @GetMapping("/toInsRoleManagePage")
    public String toInsRoleManagePage(HttpServletRequest request,HttpServletResponse response) {
    	Long insId = (long) Integer.parseInt(request.getParameter("id"));
        // 根据机构id查询到对应的机构实体
     	Institution institution = customManageService.selectInstitutionById(insId);
        request.setAttribute("insId", insId);
        request.setAttribute("institution", institution);
    	return "customMng/insRoleList";
    }
    
    /**
     * 查询所有机构角色列表
     * @Title: listdata  
     * @author: WXN
     * @Description: 查询所有机构列表(这里用一句话描述这个方法的作用)   
     * @param: @param map
     * @param: @return      
     * @return: Page<Institution>      
     * @throws
     */
    @RequestMapping(value="/findAllInsRoleList",method=RequestMethod.POST)
	@ResponseBody
	public Page<RoleFg> findAllInsRoleList(HttpServletRequest request){
    	//获取请求参数
    	Map<String,Object> map = ControllerUtil.request2Map(request);
    	Page<RoleFg> pages = new Page<RoleFg>();
    	Map<String,Object> params = new HashMap<String,Object>();
    	logger.info("参数=========================>"+map);
    	params.put("insId", map.get("insId"));
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
		pages = customManageService.findAllInsRoleList(pages);
		return pages;
    }
    
    /**
     * 跳转机构角色管理页
     *
     * @return
     */
    @GetMapping("/toInsRoleAddPage")
    public String toInsRoleAddPage(HttpServletRequest request,HttpServletResponse response) {
    	Long insId = (long) Integer.parseInt(request.getParameter("insId"));
        request.setAttribute("insId", insId);
    	return "customMng/insRoleAdd";
    }
    
    /**
     * 跳转机构角色修改页
     *
     * @return
     */
    @GetMapping("/toInsRoleUpdatePage")
    public String toInsRoleUpdatePage(HttpServletRequest request,HttpServletResponse response) {
    	Long roleId = (long) Integer.parseInt(request.getParameter("roleId"));
    	Long insId = (long) Integer.parseInt(request.getParameter("insId"));
    	RoleFg roleFg = customManageService.getInsRoleById(roleId);
    	request.setAttribute("roleId", roleId);
    	request.setAttribute("insId", insId);
    	request.setAttribute("roleFg", roleFg);
    	return "customMng/insRoleUpdate";
    }
    
    /**
	 * 检验角色是否存在
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/checkRoleName",method=RequestMethod.POST)
	@ResponseBody
	public String checkRoleName(HttpServletRequest request){
		//获取请求参数
    	Map<String,Object> map = ControllerUtil.request2Map(request);
		RoleFg roleFg = customManageService.getRoleByNameAndInsId(map);
		String result = "999";
		if(null != roleFg){
			result = "0000";
		}
		return result;
	}
    
    /**
     * 
     * @Title: saveInsRoleAdd  
     * @author: WXN
     * @Description: 保存新增机构角色(这里用一句话描述这个方法的作用)   
     * @param: @param map
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/saveInsRoleAdd")
    @ResponseBody
   	public String saveInsRoleAdd(@ModelAttribute RoleFg role,HttpServletRequest request,
			HttpServletResponse response){
    	UserBg userr = (UserBg)ControllerUtil.getSessionUser(request);
    	if(null != userr){
    		role.setCreater(userr.getLoginName());
    	}
    	role.setStatus(0);
    	role.setCreateTime(new Date());
    	logger.info("保存新增角色实体类=========="+role.toString());
    	String s = customManageService.saveInsRoleAdd(role);
		return JSON.toJSONString(s);
    }
    
    /**
     * 
     * @Title: saveInsRoleEdit  
     * @author: WXN
     * @Description: 保存编辑角色(这里用一句话描述这个方法的作用)   
     * @param: @param absRole
     * @param: @param request
     * @param: @param response
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/saveInsRoleEdit")
    @ResponseBody
   	public String saveInsRoleEdit(@ModelAttribute RoleFg Role,HttpServletRequest request,
			HttpServletResponse response){
    	logger.info("保存编辑角色实体类=========="+Role.toString());
    	String s = customManageService.saveInsRoleEdit(Role);
		return JSON.toJSONString(s);
    }
    
    /**
     * 
     * @Title: deleteInsRole  
     * @author: WXN
     * @Description: 删除角色(这里用一句话描述这个方法的作用)   
     * @param: @param map
     * @param: @return      
     * @return: Map<String,String>      
     * @throws
     */
    @RequestMapping(value="/deleteInsRole", method=RequestMethod.POST)
    @ResponseBody
    public String deleteInsRole(@RequestBody Map<String,Object> map){
    	logger.info("参数=========================>"+map);
    	String result = "999"; 
    	long roleId= Long.parseLong((String)map.get("id"));
    	int count = customManageService.finUserByRoleId(roleId);
    	if (count>0) {
    		result = "888";
		}else{
			RoleFg role=new RoleFg();
			role.setId(roleId);
			role.setStatus(1);
			try {
				result = customManageService.deleteInsRole(role);
			} catch (Exception e) {
				e.printStackTrace();
				result = "999"; 
			}
		}
		return result;
    }
    
    /**
     * 跳转角色权限分配页
     *
     * @return
     */
    @GetMapping("/toPermissionAssignPage")
    public String toPermissionAssignPage(HttpServletRequest request,HttpServletResponse response) {
    	String roleId = request.getParameter("roleId");
    	String insId = request.getParameter("insId");
    	// 根据机构id查询到对应的机构实体
     	Institution institution = customManageService.selectInstitutionById(Long.parseLong(insId));
     	//根据角色id查询到角色实体
     	RoleFg roleFg = customManageService.getInsRoleById(Long.parseLong(roleId));
        request.setAttribute("roleId", roleId);
        request.setAttribute("insId", insId);
        request.setAttribute("institution", institution);
        request.setAttribute("roleFg", roleFg);
    	return "customMng/permissionRoleAssign";
    }
    
    /**
   	 * 
   	 * @Title: findInsPermissionTree  
   	 * @author: WXN
   	 * @Description: 查询该机构下的权限树(这里用一句话描述这个方法的作用)   
   	 * @param: @param request
   	 * @param: @param response
   	 * @param: @return      
   	 * @return: String      
   	 * @throws
   	 */
   	@RequestMapping(value="/findInsPermissionTree",produces = "application/json; charset=utf-8")
	@ResponseBody
	public String findInsPermissionTree(HttpServletRequest request,HttpServletResponse response){
		String roleId = request.getParameter("roleId");
		String insId = request.getParameter("insId");
		logger.info("roleId:" + roleId );
		String tree = null;
		if(roleId!="" &&roleId!=null && roleId.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")){
			request.setAttribute("tree", tree);
			tree = customManageService.findInsPermissionTree(Long.parseLong(roleId),Long.parseLong(insId));
			return tree;
		}else{
			return null;
		}
	}
   	
	/**
   	 * 
   	 * @Title: addInsRolePermission  
   	 * @author: WXN
   	 * @Description: 保存分配后的权限(这里用一句话描述这个方法的作用)   
   	 * @param: @param request
   	 * @param: @param response
   	 * @param: @return      
   	 * @return: String      
   	 * @throws
   	 */
   	@RequestMapping(value="/addInsRolePermission",method=RequestMethod.POST)
	public @ResponseBody String addInsRolePermission(@RequestBody Map<String,Object> map){
		String permission_array = (String)map.get("permission_array");
		logger.info("获取得到的checkbox值：" + permission_array);
		String rid = (String)map.get("rid");
		String  result_mess="999";
		List<String> perIdList = new ArrayList<String>();
		if (rid == null || rid.trim().equals("") || !rid.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")) {
			result_mess = "888";//传参错误
		}else{
			if(null!=permission_array && !"".equals(permission_array)){
				logger.info("该角色拥有的权限ID："+permission_array);
				String [] array=permission_array.split(",");
				Collections.addAll(perIdList, array);
				//去掉重复的ID
				perIdList=removeListDuplicateObject(perIdList);
				logger.info("去重后该角色拥有的权限ID："+perIdList.toString());
				int flag = customManageService.addInsRolePermission(rid,perIdList);
				if(flag>0){
					result_mess="0000";//success
				}
			}
		}
		return JSON.toJSONString(result_mess);
	}
   	
   	//去重 wxn
   	private static List<String> removeListDuplicateObject(List<String> perIdList) {  
		HashSet<String> set = new HashSet<String>(perIdList);
		perIdList.clear();
		perIdList.addAll(set);
        return perIdList;
    }
   	
    /**
     * 跳转用户管理分配页
     *
     * @return
     */
    @GetMapping("/toInsUserManagePage")
    public String toInsUserManagePage(HttpServletRequest request,HttpServletResponse response) {
    	Long insId = (long) Integer.parseInt(request.getParameter("id"));
        // 根据机构id查询到对应的机构实体
     	Institution institution = customManageService.selectInstitutionById(insId);
        //通过机构id获取该机构下最多可设置的账号数量
		UserNumBean userNumBean = customManageService.getUserNumByInsId(insId);
		String userNum = "0";
		if(null != userNumBean){
			userNum = String.valueOf(userNumBean.getLimitNum());
		}
		//通过机构id获取该机构下已经拥有的账号数量
		String  hasUserNum = customManageService.getHasUserNum(insId);
		request.setAttribute("userNum", userNum);
		request.setAttribute("hasUserNum", hasUserNum);
        request.setAttribute("insId", insId);
        request.setAttribute("institution", institution);
    	return "customMng/insUserList";
    }
    
//    /**
//   	 * 
//   	 * @Title: getHasUserNum  
//   	 * @author: WXN
//   	 * @Description: 获取该机构下已经创建的账号数量(这里用一句话描述这个方法的作用)   
//   	 * @param: @param request
//   	 * @param: @param response
//   	 * @param: @return      
//   	 * @return: String      
//   	 * @throws
//   	 */
//   	@RequestMapping(value="/getHasUserNum",method=RequestMethod.POST)
//   	@ResponseBody
//	public String getHasUserNum(@RequestBody Map<String,Object> map){
//		String insId = (String)map.get("insId");
//		String  hasUserNum = customManageService.getHasUserNum(Long.parseLong(insId));
//		return JSON.toJSONString(hasUserNum);
//	}
   	
   	/**
   	 * 
   	 * @Title: saveEditUserNum  
   	 * @author: WXN
   	 * @Description: 修改该机构下设置账号的最大数量(这里用一句话描述这个方法的作用)   
   	 * @param: @param request
   	 * @param: @param response
   	 * @param: @return      
   	 * @return: String      
   	 * @throws
   	 */
   	@RequestMapping(value="/saveEditUserNum",method=RequestMethod.POST)
   	@ResponseBody
	public String saveEditUserNum(@RequestBody Map<String,Object> map){
		String insId = (String)map.get("insId");
		String insUserNum = (String)map.get("insUserNum");
		String result = "999";
		if (!UsedUtil.isNotNull(insId)) {
			result = "888";//传参错误
		}else{
			insId = insId.trim();
			if (!UsedUtil.isNotNull(insId)) {
				result = "888";//传参错误
			}
		}
		if (!UsedUtil.isNotNull(insUserNum)) {
			result = "888";//传参错误
		}else{
			insUserNum = insUserNum.trim();
			if (!UsedUtil.isNotNull(insUserNum)) {
				result = "888";//传参错误
			}
		}
		UserNumBean userNumBean = new UserNumBean();
		//获取当前登录用户信息
		UserBg user = (UserBg)ControllerUtil.getSessionUser(request);
		//添加创建人
		if (null != user) {
			//添加创建人
			userNumBean.setCreater(user.getLoginName());
		}
		//添加创建时间
		userNumBean.setCreateTime(new Date());
		userNumBean.setInstitutionId(Integer.valueOf(insId));
		userNumBean.setLimitNum(Integer.valueOf(insUserNum));
		if(insUserNum != "888" && !"888".equals(insUserNum)){
			result = customManageService.saveEditUserNum(userNumBean);
		}
		return JSON.toJSONString(result);
	}
   	
   	/**
     * 查询该机构所有用户列表
     * @Title: findAllInsUserList  
     * @author: WXN
     * @Description: 查询该机构所有用户列表(这里用一句话描述这个方法的作用)   
     * @param: @param map
     * @param: @return      
     * @return: Page<Institution>      
     * @throws
     */
    @RequestMapping(value="/findAllInsUserList",method=RequestMethod.POST)
	@ResponseBody
	public Page<UserFg> findAllInsUserList(HttpServletRequest request){
    	//获取请求参数
    	Map<String,Object> map = ControllerUtil.request2Map(request);
    	Page<UserFg> pages = new Page<UserFg>();
    	Map<String,Object> params = new HashMap<String,Object>();
    	logger.info("参数=========================>"+map);
		params.put("insId", map.get("insId"));
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
		pages = customManageService.findAllInsUserList(pages);
		return pages;
    }
    
    /**
     * 
     * @描述：进入用户管理之前检测是否已有角色，如果没有，则需要先进入角色管理
     * @创建时间：  2017年8月18日
     * @作者：武向楠
     * @参数： @param request
     * @参数： @return      
     * @return String     
     * @throws
     */
    @RequestMapping(value="/checkInsRoleListIsNotHave",method=RequestMethod.POST)
   	@ResponseBody
   	public String checkInsRoleListIsNotHave(HttpServletRequest request){
    	//获取请求参数
    	Map<String,Object> map = ControllerUtil.request2Map(request);
    	Long insId = Long.parseLong((String) map.get("insId"));
    	// 角色名称列表
    	List<RoleFg> roleList = customManageService.findAllInsRole(insId);
    	String result = "999";
    	if(null != roleList && roleList.size()>0){
    		result = "0000";
    	}
   		return result;
    }
    
    /**
     * 跳转新增机构用户页
     *
     * @return
     */
    @GetMapping("/toInsUserAddPage")
    public String toInsUserAddPage(HttpServletRequest request,HttpServletResponse response) {
    	Long insId = (long) Integer.parseInt(request.getParameter("insId"));
    	// 角色名称列表
    	List<RoleFg> roleList = customManageService.findAllInsRole(insId);
		request.setAttribute("roleList", roleList);
    	request.setAttribute("insId", insId);
    	return "customMng/insUserAdd";
    }
    
    /**
	 * 
	 * @Title: findAllInsRole  
	 * @author: WXN
	 * @Description: 获取所有该机构下的角色(这里用一句话描述这个方法的作用)   
	 * @param: @param request
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	@RequestMapping(value = "/findAllInsRole", method = RequestMethod.POST ,produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String findAllInsRole(@RequestBody Map<String,Object> map){
		logger.info("参数=========================>"+map);
		String insId = (String)map.get("insId");
		// 角色名称列表
		List<RoleFg> list = customManageService.findAllInsRole(Long.parseLong(insId));
		return JSON.toJSONString(list);
	}
	
	/**
     * 
     * @Title: saveAddInsUser  
     * @author: WXN
     * @Description: 新增用户(这里用一句话描述这个方法的作用)   
     * @param: @param map
     * @param: @return      
     * @return: String    
     * @throws
     */
    @RequestMapping(value="/saveAddInsUser", method=RequestMethod.POST) 
   	@ResponseBody
   	public String saveAddInsUser(@RequestBody Map<String,Object> map) {
    	UserBg userr = (UserBg)ControllerUtil.getSessionUser(request);
    	UserFg user = new UserFg();
    	if(null != userr){
    		user.setCreater(userr.getLoginName());
    	}
    	UserRoleFg userRole = new UserRoleFg();
		// 获取当前时间
		Date createTime = new Date();
		String password = MD5.encryption((String)map.get("password"));
		Long roleId = Long.valueOf((String)map.get("roleId"));
		user.setLoginName((String)map.get("loginName"));
		user.setName((String)map.get("name"));
		user.setPhone((String)map.get("phone"));
		user.setEmail((String)map.get("email"));
		user.setInstitutionId(Integer.valueOf((String) map.get("insId")));;
		user.setPassword(password);
		user.setUserType(1);
		user.setStatus(0);
		user.setIsDel(0);
		// 向角色表中添加创建时间
		user.setCreateTime(createTime);
		
		String result = "999";
		try {
			customManageService.saveAddInsUser(user);
			// 在用户角色中间表中插入对应的id
			userRole.setUserId(user.getId());
			userRole.setRoleId(roleId);
			customManageService.addInsRoleToInsUser(userRole);
			logger.info("角色创建成功！");
			result = "1000";
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("角色创建失败！");
			result = "999";
		}
		return result;
    }
    
    /**
     * 跳转机构用户详细页
     *
     * @return
     */
    @GetMapping("/toInsUserLookDetailPage")
    public String toInsUserLookDetailPage(HttpServletRequest request,HttpServletResponse response) {
    	Long userId = (long) Integer.parseInt(request.getParameter("userId"));
    	Long insId = (long) Integer.parseInt(request.getParameter("insId"));
    	//根据userId获取机构下的用户
    	UserVoFg userVoFg =  customManageService.getInsUserById(userId);
		request.setAttribute("userVoFg", userVoFg);
		request.setAttribute("insId", insId);
		request.setAttribute("userId", userId);
    	return "customMng/insUserDetail";
    }
    
    /**
     * 跳转机构用户详细页
     *
     * @return
     */
    @GetMapping("/toInsUserUpdatePage")
    public String toInsUserUpdatePage(HttpServletRequest request,HttpServletResponse response) {
    	Long userId = (long) Integer.parseInt(request.getParameter("userId"));
    	Long insId = (long) Integer.parseInt(request.getParameter("insId"));
    	// 角色名称列表
    	List<RoleFg> roleList = customManageService.findAllInsRole(insId);
    	//根据userId获取机构下的用户
    	UserVoFg userVoFg =  customManageService.getInsUserById(userId);
		request.setAttribute("userVoFg", userVoFg);
    	request.setAttribute("roleList", roleList);
		request.setAttribute("userId", userId);
    	request.setAttribute("insId", insId);
    	return "customMng/insUserUpdate";
    }
    
    /**
     * 
     * @Title: saveUpdateInsUser  
     * @author: WXN
     * @Description: 修改用户(这里用一句话描述这个方法的作用)   
     * @param: @param map
     * @param: @return      
     * @return: String    
     * @throws
     */
    @RequestMapping(value="/saveUpdateInsUser", method=RequestMethod.POST) 
   	@ResponseBody
   	public String saveUpdateInsUser(@RequestBody Map<String,Object> map) {
    	UserFg user = new UserFg();
    	UserRoleFg userRole = new UserRoleFg();
		Long roleId = Long.valueOf((String)map.get("roleId"));
		Long userId = Long.valueOf((String)map.get("userId"));
		user.setId(userId);
		//user.setLoginName((String)map.get("loginName"));
		user.setName((String)map.get("name"));
		user.setPhone((String)map.get("phone"));
		user.setEmail((String)map.get("email"));
		String result = "999";
		try {
			String result1 = customManageService.updateInsUser(user);
			if("1000" == result1 || "1000".equals(result1)){
				// 在用户角色中间表中插入对应的id
				userRole.setUserId(userId);
				userRole.setRoleId(roleId);
				String result2 = customManageService.updateInsRole(userRole);
				if("1000" == result2 || "1000".equals(result2)){
					result = "1000";
					logger.info("机构用户信息修改成功！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("角色修改失败！");
			result = "999";
		}
		return result;
    }
    
    /**
	 * 修改密码
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/updatePassword",method=RequestMethod.POST)
	@ResponseBody
	public String updatePassword(HttpServletRequest request){
		Long userId = Long.parseLong(request.getParameter("userId"));
		String password = (String)request.getParameter("newPassword");
		password = MD5.encryption(password);
		UserFg Userfg = new UserFg();
		String result = "999";
		try {
			Userfg.setId(userId);
			Userfg.setPassword(password);
			result = customManageService.updateInsUser(Userfg);
		} catch (Exception e) {
			result = "999";
		}
		return result;
	}
	
    /**
     * 
     * @Title: isOnlyLoginName  
     * @author: WXN
     * @Description: 查询该机构下登录名是否已存在====修改为在数据库中该用户名是否已存在
     * @param: @param map
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/isOnlyLoginName", method=RequestMethod.POST) 
   	@ResponseBody
   	public String isOnlyLoginName(@RequestBody Map<String,Object> map) {
    	String loginName = (String)map.get("loginName");
//    	String insId = (String)map.get("insId");
    	loginName = loginName.trim();
    	String result = "999";
    	Map<String,Object> mapParam = new HashMap<String,Object>();
    	mapParam.put("loginName", loginName);
//    	mapParam.put("insId", Integer.parseInt(insId));
//    	UserFg userFg = customManageService.getUserByName(mapParam);
    	List<UserFg> userFgList = customManageService.getUserListByName(mapParam);
    	if(null != userFgList && userFgList.size()>0){
    		result = "999";
    	}else{
    		result = "1000";
    	}
		return result;
    }
    
    /**
     * 
     * @Title: unLockInsUser  
     * @author: WXN
     * @Description: 解锁机构用户(这里用一句话描述这个方法的作用)   
     * @param: @param map
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/unLockInsUser", method=RequestMethod.POST) 
   	@ResponseBody
   	public String unLockInsUser(HttpServletRequest request) {
    	UserFg userBean = new UserFg();
    	long id= Long.parseLong(request.getParameter("id"));
    	userBean.setId(id);
    	userBean.setLoginNum(0);//登录次数重置为0
    	userBean.setStatus(0);//锁定状态修改为正常
    	userBean.setLoginTime(new Date());//更新登录时间
    	String result = customManageService.updateInsUser(userBean);
		return result;
    }
    
    /**
     * 
     * @Title: stopUseInsUser  
     * @author: WXN
     * @Description: 停用该机构用户(这里用一句话描述这个方法的作用)   
     * @param: @param map
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/stopUseInsUser", method=RequestMethod.POST) 
   	@ResponseBody
   	public String stopUseInsUser(HttpServletRequest request) {
    	UserFg userBean = new UserFg();
    	long id= Long.parseLong(request.getParameter("id"));
    	userBean.setId(id);
    	userBean.setStatus(1);//冻结状态
    	String result = customManageService.updateInsUser(userBean);
		return result;
    }
    
    /**
     * 
     * @Title: startUseInsUser  
     * @author: WXN
     * @Description: 启用该机构用户(这里用一句话描述这个方法的作用)   
     * @param: @param map
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/startUseInsUser", method=RequestMethod.POST) 
   	@ResponseBody
   	public String startUseInsUser(HttpServletRequest request) {
    	UserFg userBean = new UserFg();
    	long id= Long.parseLong(request.getParameter("id"));
    	userBean.setId(id);
    	userBean.setStatus(0);//可用状态
    	String result = customManageService.updateInsUser(userBean);
		return result;
    }
    
    /**
     * 
     * @Title: startUseInsUser  
     * @author: WXN
     * @Description: 删除该机构用户(这里用一句话描述这个方法的作用)   
     * @param: @param map
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/deleteInsUser", method=RequestMethod.POST) 
   	@ResponseBody
   	public String deleteInsUser(HttpServletRequest request) {
    	UserFg userBean = new UserFg();
    	long id= Long.parseLong(request.getParameter("id"));
    	userBean.setId(id);
    	userBean.setIsDel(1);//伪删除
    	String result = customManageService.updateInsUser(userBean);
		return result;
    }
    
    /**
     * 跳转IP管理页
     *
     * @return
     */
    @GetMapping("/toInsIPManagePage")
    public String toInsIPManagePage(HttpServletRequest request,HttpServletResponse response) {
    	Long insId = (long) Integer.parseInt(request.getParameter("id"));
        // 根据机构id查询到对应的机构实体
     	Institution institution = customManageService.selectInstitutionById(insId);
        request.setAttribute("insId", insId);
        request.setAttribute("institution", institution);
    	return "customMng/insIPList";
    }
    
    /**
     * 
     * @Title: findAllInsIPList  
     * @author: WXN
     * @Description: 查询该机构所有的ip列表(这里用一句话描述这个方法的作用)   
     * @param: @param map
     * @param: @return      
     * @return: Page<IPBean>      
     * @throws
     */
    @RequestMapping(value="/findAllInsIPList",method=RequestMethod.POST)
	@ResponseBody
	public Page<IPBean> findAllInsIPList(HttpServletRequest request){
    	//获取请求参数
    	Map<String,Object> map = ControllerUtil.request2Map(request);
    	logger.info("参数=========================>"+map);
    	Page<IPBean> pages = new Page<IPBean>();
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("insId", map.get("insId"));
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
		//查询该机构所有的ip列表
		pages = customManageService.findAllInsIPList(pages);
		return pages;
    }
    
    /**
     * 
     * @Title: saveInsIPAdd  
     * @author: WXN
     * @Description: 新增机构ip(这里用一句话描述这个方法的作用)   
     * @param: @param iPBean
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/saveInsIPAdd", method=RequestMethod.POST) 
   	@ResponseBody
   	public String saveInsIPAdd(HttpServletRequest request) {
    	//获取请求参数
    	Map<String,Object> map = ControllerUtil.request2Map(request);
    	logger.info("参数=========================>"+map);
    	IPBean iPBean = new IPBean();
    	UserBg user = (UserBg)ControllerUtil.getSessionUser(request);
    	if(null != user){
    		iPBean.setCreator(user.getLoginName());
    	}
		// 获取当前时间
		iPBean.setCreateTime(new Date());
		iPBean.setIpAddress((String)map.get("ipAdressFn"));
		iPBean.setCreatePlantform("1");
		iPBean.setInstitutionId(Integer.valueOf((String)map.get("insId")));
		iPBean.setState(0);
    	String result = customManageService.saveInsIPAdd(iPBean);
		return result;
    }
    
    /**
     * 
     * @Title: saveInsIPEdit  
     * @author: WXN
     * @Description: 编辑机构ip(这里用一句话描述这个方法的作用)   
     * @param: @param iPBean
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/saveInsIPEdit", method=RequestMethod.POST) 
   	@ResponseBody
   	public String saveInsIPEdit(HttpServletRequest request) {
    	//获取请求参数
    	Map<String,Object> map = ControllerUtil.request2Map(request);
    	logger.info("参数=========================>"+map);
    	IPBean iPBean = new IPBean();
    	iPBean.setIpAddress((String)map.get("ipAdressFn"));
		iPBean.setId(Integer.valueOf((String)map.get("IPId")));
    	String result = customManageService.saveInsIPEdit(iPBean);
		return result;
    }
    
    /**
     * 
     * @Title: deleteInsIP  
     * @author: WXN
     * @Description: 删除ip(这里用一句话描述这个方法的作用)   
     * @param: @param iPBean
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/deleteInsIP", method=RequestMethod.POST) 
   	@ResponseBody
   	public String deleteInsIP(HttpServletRequest request) {
    	//获取请求参数
    	Map<String,Object> map = ControllerUtil.request2Map(request);
    	logger.info("参数=========================>"+map);
    	IPBean iPBean = new IPBean();
    	iPBean.setId(Integer.valueOf((String)map.get("IPId")));
    	iPBean.setState(1);
    	String result = customManageService.saveInsIPEdit(iPBean);
		return result;
    }
    
    /**
   	 * 检验IP是否存在
   	 * @param request
   	 * @return
   	 */
   	@RequestMapping(value="/checkIPAdress",method=RequestMethod.POST)
   	@ResponseBody
   	public String checkIPAdress(HttpServletRequest request){
   		//获取请求参数
       	Map<String,Object> map = ControllerUtil.request2Map(request);
   		List<IPBean> iPBeanList = customManageService.checkIPAdress(map);
   		String result = "999";
   		if(null != iPBeanList && iPBeanList.size()>0){
   			result = "0000";
   		}
   		return result;
   	}
    
   	/**
   	 * 
   	 * @描述：导出机构列表
   	 * @创建时间：  2017年8月24日
   	 * @作者：武向楠
   	 * @参数： @param request
   	 * @参数： @param response
   	 * @return void     
   	 * @throws
   	 */
   	@RequestMapping(value = "/exportInsListExcel", method = RequestMethod.GET)
	public void exportInsListExcel(HttpServletRequest request, HttpServletResponse response){
   		try {
			String fileName = "机构列表.xls";
			List<Institution> list = customManageService.findAllExportIns();
			byte[] bytes = writeExcel(list);
			response.setContentType("application/x-msdownload");
			response.setContentLength(bytes.length);
			response.setHeader("Content-disposition",
					"attachment; filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
			response.getOutputStream().write(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
   	}
   	
   	/**
   	 * 
   	 * @描述：创建机构excel
   	 * @创建时间：  2017年8月24日
   	 * @作者：武向楠
   	 * @参数： @param list
   	 * @参数： @return
   	 * @参数： @throws Exception      
   	 * @return byte[]     
   	 * @throws
   	 */
	public static byte[] writeExcel(List<Institution> list) throws Exception {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("机构列表");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		// 设置这些样式  
		// 设置表格默认列宽度为15个字节  
//        sheet.setDefaultColumnWidth((short) 30);  
		// 背景色
		style.setFillForegroundColor(HSSFColor.CORNFLOWER_BLUE.index);  
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setFillBackgroundColor(HSSFColor.CORNFLOWER_BLUE.index); 
        // 设置边框
        style.setBorderBottom(HSSFCellStyle.BORDER_NONE);
        style.setBorderLeft(HSSFCellStyle.BORDER_NONE);
        style.setBorderRight(HSSFCellStyle.BORDER_NONE);
        style.setBorderTop(HSSFCellStyle.BORDER_NONE);  
        // 设置居中
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中 
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        // 生成一个字体
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setColor(HSSFColor.BLACK.index);
        font.setFontName("宋体");
        // 把字体 应用到当前样式
        style.setFont(font);
        
        sheet.setColumnWidth(0, 20 * 256);  
        //heightInPoints 设置的值永远是height属性值的20倍  
        row.setHeightInPoints(20);  
        
        // 第四步，创建单元格，并设置值内容
     	HSSFCellStyle style1 = wb.createCellStyle();
     	 // 设置边框
     	style1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
     	style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
     	style1.setBorderRight(HSSFCellStyle.BORDER_THIN);
     	style1.setBorderTop(HSSFCellStyle.BORDER_THIN); 
     	// 设置居中
     	style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中 
     	style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
     	// 生成一个字体
        HSSFFont font1 = wb.createFont();
        font1.setFontHeightInPoints((short) 10);
        font1.setColor(HSSFColor.BLACK.index);
        font1.setFontName("宋体");
        // 把字体 应用到当前样式
        style1.setFont(font1);
        
        //设置列宽
        sheet.setColumnWidth(0,256*10);
        sheet.setColumnWidth(1,256*40);
        sheet.setColumnWidth(2,256*30);
        sheet.setColumnWidth(3,256*25);
        sheet.setColumnWidth(4,256*25);
        sheet.setColumnWidth(5,256*20);
        //生成表头
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("序号");
		//style设置好后，为cell设置样式
		cell.setCellStyle(style);

		cell = row.createCell(1);
		cell.setCellValue("机构名称");
		cell.setCellStyle(style);

		cell = row.createCell(2);
		cell.setCellValue("代码标识");
		cell.setCellStyle(style);

		cell = row.createCell(3);
		cell.setCellValue("负责人");
		cell.setCellStyle(style);

		cell = row.createCell(4);
		cell.setCellValue("创建时间");
		cell.setCellStyle(style);

		cell = row.createCell(5);
		cell.setCellValue("状态");
		cell.setCellStyle(style);

		//创建一个DataFormat对象 
		HSSFDataFormat format= wb.createDataFormat();
		//设置时间格式
		HSSFCellStyle datesStyle = wb.createCellStyle();
		datesStyle.setDataFormat(format.getFormat("yyyy-MM-dd"));
		// 设置边框
		datesStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		datesStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		datesStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		datesStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); 
     	// 设置居中
		datesStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中 
		datesStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
		// 把字体 应用到当前样式
		datesStyle.setFont(font1);
		//设置时间格式
		HSSFCellStyle textStyle = wb.createCellStyle();
		textStyle.setDataFormat(format.getFormat("@")); 
		// 设置边框
		textStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		textStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		textStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		textStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); 
     	// 设置居中
		textStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中 
		textStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
		// 把字体 应用到当前样式
		textStyle.setFont(font1);
		
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(i + 1);
			Institution institution = list.get(i);
			// 第四步，创建单元格，并设置值
			HSSFCell cell1 = row.createCell(0);
			cell1.setCellValue(i+1);
			cell1.setCellStyle(style1);
			
			cell1 = row.createCell(1);
			cell1.setCellValue(institution.getName());
			cell1.setCellStyle(style1);
			
			cell1 = row.createCell(2);
			cell1.setCellValue(institution.getOrganizationCode());
			cell1.setCellStyle(textStyle);
			
			cell1 = row.createCell(3);
			cell1.setCellValue(institution.getCreater());
			cell1.setCellStyle(style1);
			
			cell1 = row.createCell(4);
			if (null != institution.getCreatetime()) {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				String dateString = formatter.format(institution.getCreatetime());
				cell1.setCellValue(dateString);
				cell1.setCellStyle(datesStyle);
			} else {
				cell1.setCellValue("");
				cell1.setCellStyle(style1);
			}
			
			cell1 = row.createCell(5);
			int state = institution.getState();
			int examinestate = institution.getExaminestate();
			if (2 == state) {
				cell1.setCellValue("停用");
				cell1.setCellStyle(style1);
			} else {
				if(1 == examinestate){
					cell1.setCellValue("待审核");
					cell1.setCellStyle(style1);
	            }else if(2 == examinestate){
	            	cell1.setCellValue("拒绝"); 
	            	cell1.setCellStyle(style1);
	            }else if(3 == examinestate){
	            	cell1.setCellValue("使用中");
	            	cell1.setCellStyle(style1);
	            }
			}
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		wb.write(out);
		return out.toByteArray();
	}
	
	
}
