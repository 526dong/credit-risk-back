package com.ccx.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ccx.system.model.PermissionBeanBg;
import com.ccx.system.model.RoleBg;
import com.ccx.system.model.UserBg;
import com.ccx.system.model.UserRoleBg;
import com.ccx.system.model.UserVoBg;
import com.ccx.util.page.Page;


/**
 * 
* @ClassName: UserMapper 
* @Description: User 表数据库控制层接口(这里用一句话描述这个类的作用) 
* @author WXN 
* @date 2017年7月5日 下午1:57:04 
*
 */
public interface UserMapper{

	/**
	 * 
	 * @Title: findAllUserList  
	 * @author: WXN
	 * @Description: 获取用户列表(这里用一句话描述这个方法的作用)   
	 * @param: @param page
	 * @param: @return      
	 * @return: List<UserVo>      
	 * @throws
	 */
	List<UserVoBg> findAllUserList(Page<UserVoBg> page);
	
	/**
	 * 
	 * @Title: doAddUser  
	 * @author: WXN
	 * @Description: 保存用户(这里用一句话描述这个方法的作用)   
	 * @param: @param user      
	 * @return: void      
	 * @throws
	 */
	void doAddUser(UserBg user);
	
	/**
	 * 
	 * @Title: doAddUser  
	 * @author: WXN
	 * @Description: 保存用户角色(这里用一句话描述这个方法的作用)   
	 * @param: @param user      
	 * @return: void      
	 * @throws
	 */
	void addRoleToUser(UserRoleBg userRole);
	
	/**
	 * 
	 * @描述：通过用户id获取用户实体
	 * @创建时间：  2017年8月22日
	 * @作者：武向楠
	 * @参数： @param userId
	 * @参数： @return      
	 * @return UserVoBg     
	 * @throws
	 */
	UserVoBg getUserVoByUserId(@Param("userId") long userId);
	
	/**
	 * 
	 * @Title: updateUser  
	 * @author: WXN
	 * @Description: 编辑用户(这里用一句话描述这个方法的作用)   
	 * @param: @param user      
	 * @return: int      
	 * @throws
	 */
	int updateUser(UserBg user);
	
	/**
	 * 
	 * @Title: updateRoleToUser  
	 * @author: WXN
	 * @Description: 编辑角色(这里用一句话描述这个方法的作用)   
	 * @param: @param userRole      
	 * @return: void      
	 * @throws
	 */
	void updateRoleToUser(UserRoleBg userRole);
	
	/**
	 * 
	 * @Title: updateUserPwd  
	 * @author: WXN
	 * @Description: 重置密码(这里用一句话描述这个方法的作用)   
	 * @param: @param user
	 * @param: @return      
	 * @return: int      
	 * @throws
	 */
	int updateUserPwd(UserBg user);
	
	/**
	 * 锁定用户状态
	 * @param id
	 */
	void lockUser(Long id);
	
	/**
	 * 
	 * @描述：根据用户登录名获取用户list
	 * @创建时间：  2017年8月22日
	 * @作者：武向楠
	 * @参数： @param loginName
	 * @参数： @return      
	 * @return List<UserBg>     
	 * @throws
	 */
	List<UserBg> getUserListByName(@Param("loginName") String loginName);
	
	
	//根据登录名获取用户
	UserBg getUserByName(String name);
	//根据用户ID查询权限（permission）
	List<PermissionBeanBg> findUserMenuPermission(Long id);
	//根据用户ID查询功能按钮权限（permission）
	List<PermissionBeanBg> findUserFunPermission(Long id);
	
	/**
	 * 
	 * @Title: getLeftNavigation  
	 * @author: WXN
	 * @Description: 根据模块获取左侧导航栏 
	 * @param: @param params
	 * @param: @return      
	 * @return: List<PermissionBean>      
	 * @throws
	 */
	List<PermissionBeanBg> getLeftNavigation(Map<String,Object> params);
	
	//根据用户id获取角色列表
	List<RoleBg> getRoleListByUserId(Long id);
	//根据用户id获取角色
	RoleBg getRoleByUserId(Long id);
	
	
}
