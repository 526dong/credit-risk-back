package com.ccx.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;

import com.ccx.system.model.UserBg;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


/**
 * 
* @ClassName: ControllerUtil 
* @Description: 获取用户信息和请求参数(这里用一句话描述这个类的作用) 
* @author WXN 
* @date 2017年7月4日 上午11:09:20 
*
 */
public class ControllerUtil {
	
	/**
	 * 获取登陆的用户信息
	 * @param req HttpServletRequest
	 * @return user 
	 */ 
	public static UserBg getSessionUser(HttpServletRequest req){
		HttpSession session = req.getSession();
		UserBg user = (UserBg)session.getAttribute("risk_crm_user");
    	return user;
	}
	
	/**
	 * 获取登陆用户名
	 * @return
	 */
	public static String getSessionUser(){
		String username = (String)SecurityUtils.getSubject().getPrincipal();
    	return username;
	}
	
	/**
	 * 获取请求参数
	 * @param request
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static  Map<String,Object> request2Map(HttpServletRequest request){
		Enumeration emu = request.getParameterNames();
		Map<String,Object> requestMap = new HashMap<String,Object>();
		while(emu.hasMoreElements()){
			String name = (String)emu.nextElement();
			String value = request.getParameter(name);
			if(null!=value&&value.length()>0){
				requestMap.put(name, value.trim());
			}			
		}		
		return requestMap;
	}

}
