package com.ccx.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.ccx.hierarchy.service.HierarchyService;
//import com.ccx.manager.CommonGainReport;
import com.ccx.reportmsg.manager.CommonGainReport;
import com.ccx.reportmsg.manager.CommonGainReportValue;
import com.ccx.system.model.PermissionBeanBg;
import com.ccx.system.model.UserBg;
import com.ccx.system.service.LoginService;
import com.ccx.system.service.UserService;
import com.ccx.util.ControllerUtil;
import com.ccx.util.UsedUtil;
import com.ccx.util.test2;

@Controller
public class LoginController {
	
	private Logger log = LogManager.getLogger(LoginController.class);

	@Autowired
	private LoginService loginService;
	@Autowired
	private UserService userService;

//	@Autowired
//	private RedisUtil redis;

	@Autowired
	private CommonGainReport commonGainReport;

    @Autowired
    private CommonGainReportValue commonGainReportValue;
    
    @Autowired
    private HierarchyService hierarchyService;

	/**
	 * 进入后台登录界面
	 * @return
	 */
	@RequestMapping("/login")
	public String adminLogin(){
		//解析违约率并入库
//		hierarchyService.anaysisExcelAndSave(test2.analysisExcel("D://联合理想违约率.xlsx"));
    /*    System.out.println("===============//根据类型查询模板数据包，用List来封装多个子表信息=======================");
        System.out.println(JsonUtils.toJson(commonGainReport.getModles(1)));//查询模板数据表
        System.out.println("===============//根据子表id查询 子表属性，用Map封装，以子表的各种属性作为map的Key=======================");
        System.out.println(JsonUtils.toJson(commonGainReport.getModleFieldBySheetId(1, CommonGainReport.MapKeyEnum.FIELD_NAME)));
        System.out.println("================//根据类型+子表名称查 子表属性，用Map封装，以子表的各种属性作为map的Key======================");
        System.out.println(JsonUtils.toJson(commonGainReport.getModleFieldBySheetId(1,"资产类数据", CommonGainReport.MapKeyEnum.FIELD_EXCLE_NAME)));

        System.out.println("============获取评级数据基础包==================");
        System.out.println(JsonUtils.toJson(commonGainReportValue.getBaseValue(101)));

        System.out.println("============根据reportId+（子表id/子表名称/子表的顺序）获取子表数据基础包map(map的key可以 以多种形式返回)==================");
        System.out.println(JsonUtils.toJson(commonGainReportValue.getSheetValue(101,CommonGainReportValue.SheetKeyEnum.SHEET_NAME,"资产类数据", CommonGainReportValue.MapKeyEnum.FIELD_NAME)));
        System.out.println("============根据reportId+（子表id/子表名称/子表的顺序）获取子表数据基础包map(map的key可以 以多种形式返回)==================");
        System.out.println(JsonUtils.toJson(commonGainReportValue.getSheetValueOther(101,CommonGainReportValue.SheetKeyEnum.SHEET_NAME,"资产类数据", CommonGainReportValue.SpecialKeyEnum.FIELD_NAME)));
        System.out.println(JsonUtils.toJson(commonGainReportValue.getSheetValueOther(101,CommonGainReportValue.SheetKeyEnum.SHEET_ID,"1", CommonGainReportValue.SpecialKeyEnum.FIELD_EXCLE_NAME)));
        System.out.println(JsonUtils.toJson(commonGainReportValue.getSheetValueOther(101,CommonGainReportValue.SheetKeyEnum.SHEET_NAME,"资产类数据", CommonGainReportValue.SpecialKeyEnum.FIELD_ID)));
*/

        log.info("进入登录界面");
		return "login";
	}

	/**
	 *
	 * @描述：检验用户名是否存在以及状态
	 * @创建时间：  2017年8月30日
	 * @作者：武向楠
	 * @参数： @param request
	 * @参数： @return
	 * @return Map<String,Object>
	 * @throws
	 */
    @ResponseBody
    @RequestMapping(value="/checkLoginName", method=RequestMethod.POST)
    public Map<String, Object> checkLoginName(HttpServletRequest request){
    	String account=request.getParameter("account") == null ? "" : request.getParameter("account").trim();
    	Map<String, Object> map=new HashMap<String, Object>();
		if ("".equals(account)) {
			map.put("code", "1111");
			map.put("msg", "请输入用户名!");
		}else{
			UserBg userBeann = userService.getUserByName(account);
			if (null != userBeann && null !=userBeann.getStatus() && null !=userBeann.getIsDel()) {
				//0正常；1删除
				long isDel = userBeann.getIsDel();
				//1:冻结 2：锁定
				long status = userBeann.getStatus();
				if(isDel == 1){
					map.put("code", "1111");
					map.put("msg", "该用户已注销!");
				}else{
					if(status == 1){
						map.put("code", "1111");
						map.put("msg", "该用户已停用!");
					}else if(status == 2){
						map.put("code", "1111");
						map.put("msg", "该用户已锁定!");
					}else{
						map.put("code", "0000");
					}
				}
			}else{
				map.put("code", "1111");
				map.put("msg", "该用户不存在!");
			}
		}
		return map;
	}

	/**
	 * 后台登录验证
	 * @param
	 */
	@RequestMapping(value="/checkLogin",method=RequestMethod.POST)
	@ResponseBody
	 public Map<String, Object> checkLogin(HttpServletRequest request){
		//获取请求参数
		Map<String,Object> paramMap = ControllerUtil.request2Map(request);
		return loginService.checkLogin(paramMap,request);
	}
	
	/**
	 * 获取消息提醒信息
	 * @param
	 */
	@RequestMapping(value="/getWarnMsg",method=RequestMethod.POST)
	@ResponseBody
	 public Map<String, Object> getWarnMsg(HttpServletRequest request){
		//获取请求参数
		Map<String,Object> map = new HashMap<String,Object>();
		List<String> warnMsgList = loginService.getWarnMsg();
		map.put("warnMsgList", warnMsgList);
		return map;
	}
	
	/**
	 * 获取验证码
	 * 
	 * @return {Result}
	 */
	@RequestMapping(value="/getVerifyCode",method=RequestMethod.POST)
	@ResponseBody
	public String getVerifyCode(HttpServletRequest request) {
		String verifyCode = request.getParameter("verifyCode");//前台输入的验证码
		String verCode = (String) request.getSession().getAttribute("verCode");//系统生成的验证码
		String result = "notNull";
		if(null == verifyCode || "" == verifyCode){
			result = "notNull";
		}else {
			verifyCode = verifyCode.toLowerCase();
			if(verifyCode == verCode || verifyCode.equals(verCode) ){
				result = "pass";
			}else if(verifyCode != verCode && !verifyCode.equals(verCode) ){
				result = "notSame";
			}
		}
		return result;
	}

	/**
	 * 退出后台主页，返回到登录界面
	 * 
	 * @return {Result}
	 */
	@PostMapping("/logout")
	@ResponseBody
	public Map<String,String> logout(HttpServletRequest request) {
		Map<String,String> map=new HashMap<>();
		log.info("登出");
		try {
			Subject subject = SecurityUtils.getSubject();
			request.getSession().removeAttribute("menuIndex1");//左侧菜单定位
			request.getSession().removeAttribute("menuIndex2");//左侧菜单定位
			request.getSession().removeAttribute("risk_crm_user");//用户信息
			subject.logout();
			map.put("rsCode","0000");
		} catch (Exception e) {
			map.put("rsCode","9999");
			e.printStackTrace();
		}
		return  map;
	}
	
	
	/**
	 * 未授权
	 * 
	 * @return {String}
	 */
	@GetMapping("/unauth")
	public String unauth() {
		if (SecurityUtils.getSubject().isAuthenticated() == false) {
			return "redirect:login";
		}
		return "unauth";
	}
	
	/**
	 * 
	 * @Title: getLeftNavigation  
	 * @author: WXN
	 * @Description: 根据模块获取左侧导航栏 
	 * @param: @param request
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	@ResponseBody
    @RequestMapping(value="/getLeftNavigation", method=RequestMethod.POST,produces = "application/json; charset=utf-8")  
    public String getLeftNavigation(HttpServletRequest request){
    	//获取请求参数
    	Map<String,Object> map = ControllerUtil.request2Map(request);
    	String userId = (String)map.get("userId");
    	String myselfId = (String)map.get("myselfId");
    	if(UsedUtil.isNotNull(userId)&&UsedUtil.isNotNull(myselfId)){
    		Map<String,Object> params = new HashMap<String,Object>();
    		params.put("userId", Integer.parseInt(userId.trim()));
    		params.put("myselfId", myselfId.trim());
    		//查询左侧导航栏 
    		List<PermissionBeanBg> result = loginService.getLeftNavigation(params);
        	return JSON.toJSONString(result);
    	}
		return null;
    }
	
}
