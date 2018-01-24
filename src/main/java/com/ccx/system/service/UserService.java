package com.ccx.system.service;


import java.util.List;

import com.ccx.system.model.RoleBg;
import com.ccx.system.model.UserBg;
import com.ccx.system.model.UserRoleBg;
import com.ccx.system.model.UserVoBg;
import com.ccx.util.page.Page;

/**
 * 
* @ClassName: UserService 
* @Description: User 表数据服务层接口(这里用一句话描述这个类的作用) 
* @author WXN 
* @date 2017年7月5日 下午1:30:49 
*
 */
public interface UserService{

	

	/**
	 * 
	 * @Title: findAll  
	 * @author: WXN
	 * @Description: 获取用户列表(这里用一句话描述这个方法的作用)   
	 * @param: @param params
	 * @param: @return      
	 * @return: PageInfo<UserVo>      
	 * @throws
	 */
	Page<UserVoBg> findAllUserList(Page<UserVoBg> Page);
	
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
	UserVoBg getUserVoByUserId(long userId);
	
	/**
	 * 
	 * @Title: updateUser  
	 * @author: WXN
	 * @Description: 编辑用户(这里用一句话描述这个方法的作用)   
	 * @param: @param user      
	 * @return: String      
	 * @throws
	 */
	String updateUser(UserBg user);
	
	/**
	 * 
	 * @Title: updateUser  
	 * @author: WXN
	 * @Description: 编辑角色(这里用一句话描述这个方法的作用)   
	 * @param: @param user      
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
	 * @return: String      
	 * @throws
	 */
	String updateUserPwd(UserBg user);
	
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
	List<UserBg> getUserListByName(String loginName);
	
   
   /**
    * 
    * @描述：根据用户id获取角色列表
    * @创建时间：  2017年8月22日
    * @作者：武向楠
    * @参数： @param id
    * @参数： @return      
    * @return List<RoleBg>     
    * @throws
    */
   List<RoleBg> getRoleListByUserId(Long id);
   /**
    * 
    * @描述：根据用户id获取角色
    * @创建时间：  2017年8月22日
    * @作者：武向楠
    * @参数： @param id
    * @参数： @return      
    * @return RoleBg     
    * @throws
    */
   RoleBg getRoleByUserId(Long id);
   /**
    * 
    * @描述：根据登录名获取用户实体
    * @创建时间：  2017年8月22日
    * @作者：武向楠
    * @参数： @param name
    * @参数： @return      
    * @return UserBg     
    * @throws
    */
   UserBg getUserByName(String name);
}
