package com.ccx.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ccx.system.model.PermissionBeanBg;
import com.ccx.system.model.PermissionTreeBeanBg;


/**
 * 
* @ClassName: ResourceMapper 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author WXN 
* @date 2017年7月5日 下午12:13:47 
*
 */
public interface ResourceMapper{

	//获取资源列表
	List<PermissionBeanBg> findAllRes();

	//删除资源
	void deleteResourceById(@Param("id") Long id);

	//查询资源详细信息
	PermissionBeanBg selectByPrimaryKey(@Param("id") Long id);

	//保存资源的修改信息
	void updateTO(PermissionBeanBg resource);

	//保存新增资源信息
	void doAddRes(PermissionBeanBg resource);
	
	/**
	 * 
	 * @Title: findAllPermission  
	 * @author: WXN
	 * @Description: 获取权限列表(这里用一句话描述这个方法的作用)   
	 * @param: @return      
	 * @return: List<PermissionBean>      
	 * @throws
	 */
	List<PermissionBeanBg> findAllPermission();
	
	/**
	 * 
	 * @Title: getPermissionbyMyselfID  
	 * @author: WXN
	 * @Description: 查询(这里用一句话描述这个方法的作用)   
	 * @param: @param myselfID
	 * @param: @return      
	 * @return: List<PermissionBean>      
	 * @throws
	 */
	List<PermissionBeanBg> getPermissionbyMyselfID(String myselfID);
	
	/**
	 * 
	 * @Title: addPermissions  
	 * @author: WXN
	 * @Description: 新增权限(这里用一句话描述这个方法的作用)   
	 * @param: @param permissionBean
	 * @param: @return      
	 * @return: int      
	 * @throws
	 */
	int addPermissions(PermissionBeanBg permissionBean);
	
	/**
	 * 
	 * @Title: findPermissionSequencreNum  
	 * @author: WXN
	 * @Description: 获取权限列表（通过父级id）(这里用一句话描述这个方法的作用)   
	 * @param: @param permissionBean
	 * @param: @return      
	 * @return: Integer      
	 * @throws
	 */
	List<PermissionBeanBg> getPermissionbyParentId(String parentId);
	
	/**
	 * 
	 * @Title: addPermissions  
	 * @author: WXN
	 * @Description: 新增权限(这里用一句话描述这个方法的作用)   
	 * @param: @param permissionBean
	 * @param: @return      
	 * @return: int      
	 * @throws
	 */
	int modifypermission(PermissionBeanBg permissionBean);
	
	
	/**
	 * 
	 * @Title: modifypermissionState  
	 * @author: WXN
	 * @Description: 删除权限（更改权限状态）(这里用一句话描述这个方法的作用)   
	 * @param: @param permissionBean
	 * @param: @return      
	 * @return: int      
	 * @throws
	 */
	int modifypermissionState(PermissionBeanBg permissionBean);
	
	/**
	 * 
	 * @Title: finRolePermissionTree  
	 * @author: WXN
	 * @Description: 查询权限树(这里用一句话描述这个方法的作用)   
	 * @param: @param id
	 * @param: @return      
	 * @return: List<PermissionBean>      
	 * @throws
	 */
	List<PermissionTreeBeanBg> finRolePermissionTree(@Param("id") Long id);
	
	/**
	 * 
	 * @Title: finRolePermissionTree  
	 * @author: WXN
	 * @Description: 保存分配的权限(这里用一句话描述这个方法的作用)   
	 * @param: @param id
	 * @param: @return      
	 * @return: List<PermissionBean>      
	 * @throws
	 */
	//保存权限之前先删除
	public int delRolePermissionByRole(Long id);
	public int saveRolePermission(@Param("id")Long id, @Param("perId")Long perId);
	public int getPermissionId(String perId);
	

	//获取资源分页模型
	List<PermissionBeanBg> findAll(Map<String, Object> params);

	/**
	 * 根据parentId查询子资源
	 * @param parentId
	 * @return
	 */
	List<PermissionBeanBg> selectResByPid(String parentId);

	/**
	 * 根据id查询资源
	 * @param resId
	 * @return
	 */
	PermissionBeanBg selectResourceById(Long resId);

	//根据myselfId查询资源id
	Long selectByMyself(String myselfId);
	
	//根据用户id获取资源id
	List<Long> getResByUserId(Long id);

}
