package com.ccx.system.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ccx.system.model.PermissionBeanBg;

public interface LoginService {
	/**
	 * 登录合法性校验
	 * @param paramMap  参数
	 * @param HttpServletRequest  request
	 */
	Map<String, Object> checkLogin(Map<String, Object> paramMap, HttpServletRequest request);
	
	/**
	 * 
	 * @Title: getLeftNavigation  
	 * @author: WXN
	 * @Description: 根据模块获取左侧导航栏 
	 * @param: @param params
	 * @param: @return      
	 * @return: List<PermissionBeanBg>      
	 * @throws
	 */
	List<PermissionBeanBg> getLeftNavigation(Map<String,Object> params);
	
	/**
	 * 
	 * @描述：获取消息提醒信息
	 * @创建时间：  2017年8月31日
	 * @作者：武向楠
	 * @参数： @return      
	 * @return List<String>     
	 * @throws
	 */
	List<String> getWarnMsg();
}