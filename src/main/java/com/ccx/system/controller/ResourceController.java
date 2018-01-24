package com.ccx.system.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.ccx.base.BasicController;
import com.ccx.system.model.UserBg;
import com.ccx.system.model.PermissionBeanBg;
import com.ccx.system.service.ResourceService;


/**
 * 
* @ClassName: ResourceController 
* @Description: 资源管理:resource(这里用一句话描述这个类的作用) 
* @author WXN 
* @date 2017年7月5日 下午1:13:17 
*
 */
@Controller
@RequestMapping("/resource")
public class ResourceController extends BasicController{
	private static final Logger logger = LoggerFactory.getLogger(ResourceController.class);
	@Autowired
	private ResourceService resourceApi;

	
	/**
     * 资源管理页
     *
     * @return
     */
    @GetMapping("/manager")
    public String manager() {
        return "systemMng/resource/permissionList";
    }
    
    
    /**
     * 
     * @Title: permissionListPage  
     * @author: WXN
     * @Description: 获取权限列表(这里用一句话描述这个方法的作用)   
     * @param: @param request
     * @param: @param response
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/permissionListPage",method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	public @ResponseBody String permissionListPage(HttpServletRequest request,
			HttpServletResponse response){
		String s =resourceApi.findAllPermission();
		return s;
	}
    
    /**
     * 
     * @Title: addpermission  
     * @author: WXN
     * @Description: 添加权限(这里用一句话描述这个方法的作用)   
     * @param: @param permissionBean
     * @param: @param request
     * @param: @param response
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/addpermission")
	public @ResponseBody String addpermission(@ModelAttribute PermissionBeanBg permissionBean,HttpServletRequest request,
			HttpServletResponse response){
    	UserBg user = (UserBg)request.getSession().getAttribute("zxuser");
		//添加创建人
		if (null != user) {
			permissionBean.setCreater(user.getLoginName());
		}
		//添加创建时间
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String creattime = df.format(new Date());// new Date()为获取当前系统时间
		permissionBean.setCreateTime(creattime);
		//添加权限状态
		permissionBean.setPermission_state(1);
		
		String s = resourceApi.addPermissions(permissionBean);
		return JSON.toJSONString(s);
	}
    
    /**
     * 
     * @Title: saveEditPermission  
     * @author: WXN
     * @Description: 编辑权限(这里用一句话描述这个方法的作用)   
     * @param: @param permissionBean
     * @param: @param request
     * @param: @param response
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/saveEditPermission")
	public @ResponseBody String saveEditPermission(@ModelAttribute PermissionBeanBg permissionBean,HttpServletRequest request,
			HttpServletResponse response){
    	UserBg user = (UserBg)request.getSession().getAttribute("zxuser");
		//添加修改人
		if (null != user) {
			permissionBean.setModifier(user.getLoginName());
		}
		//添加修改时间
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String creattime = df.format(new Date());// new Date()为获取当前系统时间
		permissionBean.setModifyTime(creattime);
		permissionBean.setPermission_state(1);
		System.out.println(permissionBean);
		String s = resourceApi.modifypermission(permissionBean);
		return JSON.toJSONString(s);
	}
    
    /**
     * 
     * @Title: modifypermissionState  
     * @author: WXN
     * @Description: 删除权限（更改权限状态）(这里用一句话描述这个方法的作用)   
     * @param: @param permissionBean
     * @param: @param request
     * @param: @param response
     * @param: @return      
     * @return: String      
     * @throws
     */
    @RequestMapping(value="/modifypermissionState")
	public @ResponseBody String modifypermissionState(@ModelAttribute PermissionBeanBg permissionBean,HttpServletRequest request,
			HttpServletResponse response){
    	UserBg user = (UserBg)request.getSession().getAttribute("zxuser");
		//添加修改人
		if (null != user) {
			permissionBean.setModifier(user.getLoginName());
		}
		//添加修改时间
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String creattime = df.format(new Date());// new Date()为获取当前系统时间
		permissionBean.setModifyTime(creattime);
		String s = resourceApi.modifypermissionState(permissionBean);
		return JSON.toJSONString(s);
	}
    
}
