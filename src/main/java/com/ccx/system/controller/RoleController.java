package com.ccx.system.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.ccx.base.BasicController;
import com.ccx.shiro.ReloadAuthorizing;
import com.ccx.system.model.Organization;
import com.ccx.system.model.RoleBg;
import com.ccx.system.model.UserBg;
import com.ccx.system.service.AbsRoleService;
import com.ccx.system.service.ResourceService;
import com.ccx.util.ControllerUtil;
import com.ccx.util.UsedUtil;
import com.ccx.util.page.Page;


/**
 * 
* @ClassName: RoleController 
* @Description: 角色管理(这里用一句话描述这个类的作用) 
* @author WXN 
* @date 2017年7月5日 下午3:03:32 
*
 */
@Controller
@RequestMapping("/role")
public class RoleController extends BasicController{
	private static final Logger logger = LoggerFactory.getLogger(RoleController.class);
	
	@Autowired
	private AbsRoleService absRoleService;
	@Autowired
	private ResourceService resourceService;
	
	/**
	 * 
	 * @Title: manager  
	 * @author: WXN
	 * @Description: 跳转角色管理页(这里用一句话描述这个方法的作用)   
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
    @GetMapping("/manager")
    public String manager() {
        return "systemMng/role/roleList";
    }
    
    /**
     * 
     * @Title: findAllSysRole  
     * @author: WXN
     * @Description: 获取用户列表(分页)(这里用一句话描述这个方法的作用)   
     * @param: @param map
     * @param: @return      
     * @return: Page<UserVo>      
     * @throws
     */
    @RequestMapping(value="/findAllSysRole", method=RequestMethod.POST) 
   	@ResponseBody
   	public Page<RoleBg> findAllSysRole(HttpServletRequest request) {
    	//获取请求参数
    	Map<String,Object> map = ControllerUtil.request2Map(request);
    	Page<RoleBg> pages = new Page<RoleBg>();
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
		pages = absRoleService.findAllRole(pages);
		return pages;
	}
    
    /**
     * 
     * @描述：跳转新增角色页面
     * @创建时间：  2017年8月22日
     * @作者：武向楠
     * @参数： @param request
     * @参数： @return      
     * @return String     
     * @throws
     */
    @RequestMapping(value="toSysRoleAddPage",method=RequestMethod.GET)
    public String toSysRoleAddPage(HttpServletRequest request){
    	return "systemMng/role/roleAdd";
    }
    
    /**
     * 
     * @描述：校验角色唯一
     * @创建时间：  2017年8月22日
     * @作者：武向楠
     * @参数： @param map
     * @参数： @return      
     * @return String     
     * @throws
     */
    @RequestMapping(value="/checkSysRoleName", method=RequestMethod.POST) 
   	@ResponseBody
   	public String checkSysRoleName(@RequestBody Map<String,Object> map) {
    	String roleName = (String)map.get("roleName");
    	if(UsedUtil.isNotNull(roleName)){
    		roleName = roleName.trim();
    	}
    	String result = "999";
    	List<RoleBg> roleList = absRoleService.getRoleListByName(roleName);
    	if(null != roleList && roleList.size()>0){
    		result = "999";
    	}else{
    		result = "1000";
    	}
		return result;
    }
    
    /**
     * 
     * @Title: saveSysRoleAdd  
     * @author: WXN
     * @Description: 保存新增角色(这里用一句话描述这个方法的作用)   
     * @param: @param map
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/saveSysRoleAdd")
    @ResponseBody
   	public String saveSysRoleAdd(@ModelAttribute RoleBg role,HttpServletRequest request,
			HttpServletResponse response){
    	//获取当前登录用户信息
    	UserBg userr = (UserBg)ControllerUtil.getSessionUser(request);
    	if(null != userr){
    		role.setCreater(userr.getLoginName());
    	}
    	role.setStatus(0);
    	logger.debug("保存新增角色实体类=========="+role.toString());
    	String result = absRoleService.saveRoleAdd(role);
		return result;
    }
    
    /**
     * 
     * @描述：跳转新增修改页面
     * @创建时间：  2017年8月22日
     * @作者：武向楠
     * @参数： @param request
     * @参数： @return      
     * @return String     
     * @throws
     */
    @RequestMapping(value="toSysRoleEditPage",method=RequestMethod.GET)
    public String toSysRoleEditPage(HttpServletRequest request){
    	String roleId = request.getParameter("roleId");
    	//根据角色id查询角色实体
    	RoleBg roleBg = absRoleService.getById(Long.parseLong(roleId));
    	request.setAttribute("roleBg", roleBg);
    	request.setAttribute("roleId", roleId);
    	return "systemMng/role/roleUpdate";
    }
    
    
    /**
     * 
     * @Title: saveSysRoleEdit  
     * @author: WXN
     * @Description: 保存编辑角色(这里用一句话描述这个方法的作用)   
     * @param: @param absRole
     * @param: @param request
     * @param: @param response
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/saveSysRoleEdit")
    @ResponseBody
   	public String saveSysRoleEdit(@ModelAttribute RoleBg Role,HttpServletRequest request,
			HttpServletResponse response){
    	logger.debug("保存编辑角色实体类=========="+Role.toString());
    	String result = absRoleService.saveRoleEdit(Role);
		return result;
    }
    
    /**
     * 
     * @Title: toAllotSysRoleResource  
     * @author: WXN
     * @Description: 根据角色ID查询权限(这里用一句话描述这个方法的作用)   
     * @param: @param request
     * @param: @return      
     * @return: String      
     * @throws
     */
   	@RequestMapping(value="toAllotSysRoleResource" ,method=RequestMethod.GET)
   	public String toAllotSysRoleResource(HttpServletRequest request){
   		String roleId = request.getParameter("roleId");
    	//根据角色id查询角色实体
    	RoleBg roleBg = absRoleService.getById(Long.parseLong(roleId));
		request.setAttribute("roleId", roleId);
		request.setAttribute("roleBg", roleBg);
		return "systemMng/role/permissionAssign";
    }
   	
   	/**
   	 * 
   	 * @Title: findPermissionTree  
   	 * @author: WXN
   	 * @Description: 查询权限树(这里用一句话描述这个方法的作用)   
   	 * @param: @param request
   	 * @param: @param response
   	 * @param: @return      
   	 * @return: String      
   	 * @throws
   	 */
   	@RequestMapping(value="/findPermissionTree",produces = "application/json; charset=utf-8")
	@ResponseBody
	public String findPermissionTree(HttpServletRequest request,HttpServletResponse response){
		String roleId = request.getParameter("roleId");
		logger.info("roleId:" + roleId );
		String tree = null;
		if(roleId!="" &&roleId!=null && roleId.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")){
			request.setAttribute("tree", tree);
			tree = resourceService.finRolePermissionTree(Long.parseLong(roleId));
			return tree;
		}else{
			return null;
		}
	}
   	
   	/**
   	 * 
   	 * @Title: addRPermission  
   	 * @author: WXN
   	 * @Description: 保存分配后的权限(这里用一句话描述这个方法的作用)   
   	 * @param: @param request
   	 * @param: @param response
   	 * @param: @return      
   	 * @return: String      
   	 * @throws
   	 */
   	@RequestMapping(value="/addRPermission",method=RequestMethod.POST)
	public @ResponseBody String addRPermission(@RequestBody Map<String,Object> map){
		String permission_array = (String)map.get("permission_array");
		logger.debug("获取得到的checkbox值：" + permission_array);
		String rid = (String)map.get("rid");
		String  result_mess="999";
		List<String> perIdList = new ArrayList<String>();
		if (rid == null || rid.trim().equals("") || !rid.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")) {
			result_mess = "888";//传参错误
		}else{
			if(null!=permission_array && !"".equals(permission_array)){
				logger.debug("该角色拥有的权限ID："+permission_array);
				String [] array=permission_array.split(",");
				Collections.addAll(perIdList, array);
				//去掉重复的ID
				perIdList=removeListDuplicateObject(perIdList);
				logger.debug("去重后该角色拥有的权限ID："+perIdList.toString());
				int flag = resourceService.saveRolePermission(rid,perIdList);
				if(flag>0){
					result_mess="0000";//success
					//获取当前登录用户信息
			    	UserBg user = (UserBg)ControllerUtil.getSessionUser(request);
					ReloadAuthorizing.reloadAuthorizing(user.getLoginName());
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
     * 
     * @Title: deleteSysRole  
     * @author: WXN
     * @Description: 删除角色(这里用一句话描述这个方法的作用)   
     * @param: @param map
     * @param: @return      
     * @return: Map<String,String>      
     * @throws
     */
    @RequestMapping(value="/deleteSysRole", method=RequestMethod.POST)
    @ResponseBody
    public String deleteSysRole(@RequestBody Map<String,Object> map){
    	logger.debug("参数=========================>"+map);
    	String result = "999"; 
    	long roleId= Long.parseLong((String)map.get("roleId"));
    	int count =absRoleService.selectUserByRoleId(roleId);
    	if (count>0) {
    		result = "888";
		}else{
			RoleBg absRole=new RoleBg();
			absRole.setId(roleId);
			absRole.setStatus(1);
			try {
				result = absRoleService.deleteRole(absRole);
			} catch (Exception e) {
				e.printStackTrace();
				result = "999"; 
			}
		}
		return result;
    }
    
	/**
	 * 
	 * @Title: findAllRoleList  
	 * @author: WXN
	 * @Description: 获取所有角色(这里用一句话描述这个方法的作用)   
	 * @param: @param request
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	@RequestMapping(value = "/findAllRoleList", method = RequestMethod.POST ,produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String findAllRoleList(HttpServletRequest request) {
		// 角色名称列表
		List<RoleBg> list = absRoleService.findAllRoleList();
		return JSON.toJSONString(list);
	}
	
	/**
	 * 
	 * @Title: findAllOrgList  
	 * @author: WXN
	 * @Description: 获取所有部门(这里用一句话描述这个方法的作用)   
	 * @param: @param request
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	@RequestMapping(value = "/findAllOrgList", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String findAllOrgList(HttpServletRequest request) {
		//角色名称列表
		List<Organization> orList = absRoleService.findAllOrgList();
		return JSON.toJSONString(orList);
	}

	
}
