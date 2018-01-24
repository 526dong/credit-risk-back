package com.ccx.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccx.system.dao.UserMapper;
import com.ccx.system.model.RoleBg;
import com.ccx.system.model.UserBg;
import com.ccx.system.model.UserRoleBg;
import com.ccx.system.model.UserVoBg;
import com.ccx.system.service.UserService;
import com.ccx.util.page.Page;

@Service()
public class UserServiceImpl implements UserService{

	@Autowired
    private UserMapper userMapper;

	/**
	 * 
	 * @Title: UserServiceImpl   
	 * @Description: 获取用户列表(这里用一句话描述这个方法的作用)   
	 * @param: @param Page
	 * @param: @return
	 * @throws
	 */
	@Override
	public Page<UserVoBg> findAllUserList(Page<UserVoBg> page) {
		page.setRows(userMapper.findAllUserList(page));
		return page;
	}
	
	/**
	 * 
	 * @Title: UserServiceImpl   
	 * @Description: 保存用户(这里用一句话描述这个方法的作用)   
	 * @param: @param user
	 * @throws
	 */
	@Override
	public void doAddUser(UserBg user){
		userMapper.doAddUser(user);
	}
	
	/**
	 * 
	 * @Title: doAddUser  
	 * @author: WXN
	 * @Description: 保存用户角色(这里用一句话描述这个方法的作用)   
	 * @param: @param user      
	 * @return: void      
	 * @throws
	 */
	@Override
	public void addRoleToUser(UserRoleBg userRole){
		userMapper.addRoleToUser(userRole);
	}
	
	/**
	 * 
	 * @描述：通过用户id获取用户实体
	 * @创建时间：  2017年8月22日
	 * @作者：武向楠
	 */
	@Override
	public UserVoBg getUserVoByUserId(long userId){
		return userMapper.getUserVoByUserId(userId);
	}
	
	/**
	 * 
	 * @Title: UserServiceImpl   
	 * @Description: 编辑用户(这里用一句话描述这个方法的作用)   
	 * @param: @param user
	 * @throws
	 */
	@Override
	public String updateUser(UserBg user){
		String result = "999";
		try {
			int s = userMapper.updateUser(user);
			if (s>0) {
				result = "1000";
			} else {
				result = "999";
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "999";
		}
		return result;
	}
	
	/**
	 * 
	 * @Title: UserServiceImpl   
	 * @Description: 编辑角色(这里用一句话描述这个方法的作用)   
	 * @param: @param userRole
	 * @throws
	 */
	@Override
	public void updateRoleToUser(UserRoleBg userRole){
		userMapper.updateRoleToUser(userRole);
	}
	
	/**
	 * 
	 * @Title: UserServiceImpl   
	 * @Description: 重置密码(这里用一句话描述这个方法的作用)   
	 * @param: @param user
	 * @throws
	 */
	@Override
	public String updateUserPwd(UserBg user){
		String result = "999";
		try {
			int s = userMapper.updateUserPwd(user);
			if (s>0) {
				result = "1000";
			} else {
				result = "999";
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "999";
		}
		return result;
	}
	
	/**
	 * 
	 * @描述：根据登录名获取用户
	 * @创建时间：  2017年8月22日
	 * @作者：武向楠
	 */
	@Override
	public UserBg getUserByName(String name) {
		return userMapper.getUserByName(name);
	}
	
	/**
	 * 
	 * @描述：根据用户登录名获取用户list
	 * @创建时间：  2017年8月22日
	 * @作者：武向楠
	 */
	@Override
	public List<UserBg> getUserListByName(String loginName){
		return userMapper.getUserListByName(loginName);
	}
	
	/**
	 * 
	 * @描述：根据用户id获取角色列表
	 * @创建时间：  2017年8月22日
	 * @作者：武向楠
	 */
	@Override
	public List<RoleBg> getRoleListByUserId(Long id) {
		return userMapper.getRoleListByUserId(id);
	}
	
	/**
	 * 
	 * @描述：根据用户id获取角色
	 * @创建时间：  2017年8月22日
	 * @作者：武向楠
	 */
	@Override
	public RoleBg getRoleByUserId(Long id) {
		return userMapper.getRoleByUserId(id);
	}
	
}
 