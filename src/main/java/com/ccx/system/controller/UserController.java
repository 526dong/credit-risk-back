package com.ccx.system.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccx.base.BasicController;
import com.ccx.system.model.RoleBg;
import com.ccx.system.model.UserBg;
import com.ccx.system.model.UserRoleBg;
import com.ccx.system.model.UserVoBg;
import com.ccx.system.service.AbsRoleService;
import com.ccx.system.service.UserService;
import com.ccx.util.ControllerUtil;
import com.ccx.util.MD5;
import com.ccx.util.UsedUtil;
import com.ccx.util.page.Page;


/**
 * 
* @ClassName: UserController 
* @Description: 用户管理(这里用一句话描述这个类的作用) 
* @author WXN 
* @date 2017年7月5日 下午1:39:47 
*
 */
@Controller
@RequestMapping("/user")
public class UserController extends BasicController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private AbsRoleService absRoleService;

	@Autowired
	ApplicationContext context;

	/**
	 * 
	 * @Title: manager  
	 * @author: WXN
	 * @Description: 跳转用户管理页(这里用一句话描述这个方法的作用)   
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	@RequestMapping(value = "/manager", method = RequestMethod.GET)
    public String manager(HttpServletRequest request) {
    	// 角色名称列表
		List<RoleBg> roleBgList = absRoleService.findAllRoleList();
		request.setAttribute("roleBgList", roleBgList);
        return "systemMng/user/userList";
    }
    
    /**
     * 
     * @Title: findAll  
     * @author: WXN
     * @Description: 获取用户列表(这里用一句话描述这个方法的作用)   
     * @param: @param map
     * @param: @return      
     * @return: Page<UserVo>      
     * @throws
     */
    @RequestMapping(value="/findAll", method=RequestMethod.POST) 
   	@ResponseBody
   	public Page<UserVoBg> findAll(HttpServletRequest request) {
    	//获取请求参数
    	Map<String,Object> map = ControllerUtil.request2Map(request);
    	logger.info("参数=========================>"+map);
    	Page<UserVoBg> pages = new Page<UserVoBg>();
    	Map<String,Object> params = new HashMap<String,Object>();
		params.put("keyWord", map.get("keyWord"));
		params.put("roleId", map.get("roleId"));
		params.put("userState", map.get("userState"));
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
		pages = userService.findAllUserList(pages);
		return pages;
	}
    
    /**
     * 
     * @描述：跳转新增系统用户页面
     * @创建时间：  2017年8月22日
     * @作者：武向楠
     * @参数： @param request
     * @参数： @return      
     * @return String     
     * @throws
     */
    @RequestMapping(value = "/toAddSysUserPage", method = RequestMethod.GET)
    public String toAddSysUserPage(HttpServletRequest request) {
    	// 角色名称列表
		List<RoleBg> roleBgList = absRoleService.findAllRoleList();
		request.setAttribute("roleBgList", roleBgList);
        return "systemMng/user/userAdd";
    }
    
    /**
     * 
     * @Title: saveAddSysUser  
     * @author: WXN
     * @Description: 新增用户(这里用一句话描述这个方法的作用)   
     * @param: @param map
     * @param: @return      
     * @return: String    
     * @throws
     */
    @RequestMapping(value="/saveAddSysUser", method=RequestMethod.POST) 
   	@ResponseBody
   	public String saveAddSysUser(@RequestBody Map<String,Object> map,HttpServletRequest request) {
    	//获取当前登录用户信息
    	UserBg userr = (UserBg)ControllerUtil.getSessionUser(request);
    	UserBg user = new UserBg();
    	if(null != userr){
    		user.setCreater(userr.getLoginName());
    	}
    	UserRoleBg userRole = new UserRoleBg();
		// 获取当前时间
		Date createTime = new Date();
		String password = MD5.encryption((String)map.get("password"));
		Long roleId = Long.valueOf((String)map.get("roleId"));
		// 向角色表中添加创建时间
		user.setCreateTime(createTime);
		user.setLoginName((String)map.get("loginName"));
		user.setName((String)map.get("name"));
		user.setPhone((String)map.get("phone"));
		user.setEmail((String)map.get("email"));
		user.setPassword(password);
		user.setUserType(1);
		user.setStatus(0);
		user.setIsDel(0);
		
		String result = "999";
		try {
			userService.doAddUser(user);
			// 在用户角色中间表中插入对应的id
			userRole.setUserId(user.getId());
			userRole.setRoleId(roleId);
			userService.addRoleToUser(userRole);
			logger.info("用户创建成功！");
			result = "1000";
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("用户创建失败！");
			result = "999";
		}
		return result;
    }
    
    /**
     * 
     * @描述：查看用户详情
     * @创建时间：  2017年8月22日
     * @作者：武向楠
     * @参数： @param request
     * @参数： @return      
     * @return String     
     * @throws
     */
    @RequestMapping(value = "/toLookDetailSysUserPage", method = RequestMethod.GET)
    public String toLookDetailSysUserPage(HttpServletRequest request,HttpServletResponse response) {
    	//通过用户id获取用户实体
    	Long userId = (long) Integer.parseInt(request.getParameter("userId"));
    	UserVoBg userVoBg = userService.getUserVoByUserId(userId);
		request.setAttribute("userVoBg", userVoBg);
		request.setAttribute("userId", userId);
        return "systemMng/user/userDetail";
    }
    
    /**
     * 
     * @描述：跳转修改用户信息页面
     * @创建时间：  2017年8月22日
     * @作者：武向楠
     * @参数： @param request
     * @参数： @param response
     * @参数： @return      
     * @return String     
     * @throws
     */
    @RequestMapping(value = "/toEditSysUserPage", method = RequestMethod.GET)
    public String toEditSysUserPage(HttpServletRequest request,HttpServletResponse response) {
    	//通过用户id获取用户实体
    	Long userId = (long) Integer.parseInt(request.getParameter("userId"));
    	UserVoBg userVoBg = userService.getUserVoByUserId(userId);
    	// 角色名称列表
		List<RoleBg> roleBgList = absRoleService.findAllRoleList();
		request.setAttribute("roleBgList", roleBgList);
		request.setAttribute("userVoBg", userVoBg);
		request.setAttribute("userId", userId);
        return "systemMng/user/userUpdate";
    }
    
    /**
     * 
     * @Title: saveUpdateSysUser  
     * @author: WXN
     * @Description: 编辑用户(这里用一句话描述这个方法的作用)   
     * @param: @param map
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/saveUpdateSysUser", method=RequestMethod.POST) 
   	@ResponseBody
   	public String saveUpdateSysUser(@RequestBody Map<String,Object> map) {
    	UserBg user = new UserBg();
    	UserRoleBg userRole = new UserRoleBg();
    	
		Long roleId = Long.valueOf((String)map.get("roleId"));
		Long userId = Long.valueOf((String)map.get("userId"));
		
		user.setId(userId);
		//user.setLoginName((String)map.get("loginName"));
		user.setName((String)map.get("name"));
		user.setPhone((String)map.get("phone"));
		user.setEmail((String)map.get("email"));
		
		String result = "999";
		try {
			userService.updateUser(user);
			// 在用户角色中间表中插入对应的id
			userRole.setUserId(user.getId());
			userRole.setRoleId(roleId);
			userService.updateRoleToUser(userRole);
			logger.debug("用户修改成功！");
			result = "1000";
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户修改失败！");
			result = "999";
		}
		return result;
    }
    
    /**
     * 
     * @Title: isOnlyLoginName  
     * @author: WXN
     * @Description: 编辑用户(这里用一句话描述这个方法的作用)   
     * @param: @param map
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/isOnlyLoginName", method=RequestMethod.POST) 
   	@ResponseBody
   	public String isOnlyLoginName(@RequestBody Map<String,Object> map) {
    	String loginName = (String)map.get("loginName");
    	if(UsedUtil.isNotNull(loginName)){
    		loginName = loginName.trim();
    	}
    	String result = "999";
    	List<UserBg> userList = userService.getUserListByName(loginName);
    	if(null != userList && userList.size()>0){
    		result = "999";
    	}else{
    		result = "1000";
    	}
		return result;
    }
    
    /**
     * 
     * @Title: updateUserPwd  
     * @author: WXN
     * @Description: 重置密码(这里用一句话描述这个方法的作用)   
     * @param: @param user
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/updateUserPwd", method=RequestMethod.POST) 
   	@ResponseBody
   	public String updateUserPwd(HttpServletRequest request) {
    	Long userId = Long.parseLong(request.getParameter("userId"));
		String password = (String)request.getParameter("newPassword");
		password = MD5.encryption(password);
    	UserBg user = new UserBg();
    	String result = "999";
		try {
			user.setId(userId);
			user.setPassword(password);
			result = userService.updateUserPwd(user);
		} catch (Exception e) {
			result = "999";
		}
		return result;
    }
    
    /**
     * 
     * @Title: stopUseSysUser  
     * @author: WXN
     * @Description: 停用用户   
     * @param: @param user
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/stopUseSysUser", method=RequestMethod.POST) 
   	@ResponseBody
   	public String stopUseSysUser(HttpServletRequest request) {
    	UserBg user = new UserBg();
    	long id= Long.parseLong(request.getParameter("id"));
    	user.setId(id);
    	user.setStatus(1);//冻结状态
		String result = userService.updateUser(user);
		return result;
    }
    
    /**
     * 
     * @Title: startUseSysUser  
     * @author: WXN
     * @Description: 启用用户 
     * @param: @param user
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/startUseSysUser", method=RequestMethod.POST) 
   	@ResponseBody
   	public String startUseSysUser(HttpServletRequest request) {
    	UserBg user = new UserBg();
    	long id= Long.parseLong(request.getParameter("id"));
    	user.setId(id);
    	user.setStatus(0);//启用状态
		String result = userService.updateUser(user);
		return result;
    }
    
    /**
     * 
     * @描述：解锁用户
     * @创建时间：  2017年8月22日
     * @作者：武向楠
     * @参数： @param request
     * @参数： @return      
     * @return String     
     * @throws
     */
    @RequestMapping(value="/unLockSysUser",method=RequestMethod.POST)
    @ResponseBody
    public String unLockSysUser(HttpServletRequest request){
    	UserBg user = new UserBg();
    	long id= Long.parseLong(request.getParameter("id"));
    	user.setId(id);
    	user.setLoginNum(0);//登录次数重置为0
    	user.setStatus(0);//锁定状态修改为正常
    	user.setLoginTime(new Date());//更新登录时间
    	String result = userService.updateUser(user);
		return result;
    }
    
    /**
     * 
     * @Title: deleteSysUser  
     * @author: WXN
     * @Description: 注销用户(这里用一句话描述这个方法的作用)   
     * @param: @param user
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/deleteSysUser", method=RequestMethod.POST) 
   	@ResponseBody
   	public String deleteSysUser(HttpServletRequest request) {
    	UserBg user = new UserBg();
    	long id= Long.parseLong(request.getParameter("id"));
    	user.setId(id);
    	user.setIsDel(1);//伪删除
    	String result = userService.updateUser(user);
		return result;
    }
    
}
