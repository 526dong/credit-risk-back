package com.ccx.system.service;

import java.util.List;

import com.ccx.system.model.PermissionBeanBg;

/**
 * 
* @ClassName: ResourceService 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author WXN 
* @date 2017年7月5日 下午12:09:23 
*
 */
public interface ResourceService {

	/**
	 * 
	 * @Title: findAllPermission  
	 * @author: WXN
	 * @Description: 获取权限列表(这里用一句话描述这个方法的作用)   
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	String findAllPermission();
	
	/**
	 * 
	 * @Title: addPermissions  
	 * @author: WXN
	 * @Description: 增加权限(这里用一句话描述这个方法的作用)   
	 * @param: @param permissionBean
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	String addPermissions(PermissionBeanBg permissionBean);
	
	/**
	 * 
	 * @Title: modifypermission  
	 * @author: WXN
	 * @Description: 编辑权限(这里用一句话描述这个方法的作用)   
	 * @param: @param permissionBean
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	String modifypermission(PermissionBeanBg permissionBean);
	
	/**
	 * 
	 * @Title: modifypermissionState  
	 * @author: WXN
	 * @Description: 删除权限（更改权限状态）(这里用一句话描述这个方法的作用)   
	 * @param: @param permissionBean
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	String modifypermissionState(PermissionBeanBg permissionBean);
	
	/**
	 * 
	 * @Title: finRolePermissionTree  
	 * @author: WXN
	 * @Description: 查询权限树(这里用一句话描述这个方法的作用)   
	 * @param: @param id
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	String finRolePermissionTree(Long id);
	
	/**
	 * 
	 * @Title: saveRolePermission  
	 * @author: WXN
	 * @Description: 保存分配后的权限(这里用一句话描述这个方法的作用)   
	 * @param: @param id
	 * @param: @param perIdList
	 * @param: @return      
	 * @return: int      
	 * @throws
	 */
	int saveRolePermission(String id, List<String> perIdList);

	/**
	 * 
	 * @Title: selectResourceById  
	 * @author: WXN
	 * @Description: 根据资源id查询资源实体(这里用一句话描述这个方法的作用)   
	 * @param: @param resId
	 * @param: @return      
	 * @return: PermissionBeanBg      
	 * @throws
	 */
	PermissionBeanBg selectResourceById(Long resId);
	
	/**
	 * 
	 * @Title: getResByUserId  
	 * @author: WXN
	 * @Description: 根据用户id获取资源id(这里用一句话描述这个方法的作用)   
	 * @param: @param id
	 * @param: @return      
	 * @return: List<Long>      
	 * @throws
	 */
	List<Long> getResByUserId(Long id);

}
