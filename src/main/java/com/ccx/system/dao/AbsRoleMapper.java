package com.ccx.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ccx.system.model.Organization;
import com.ccx.system.model.RoleBg;
import com.ccx.util.page.Page;

public interface AbsRoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RoleBg record);

    int insertSelective(RoleBg record);

    RoleBg selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RoleBg record);

    int updateByPrimaryKey(RoleBg record);
    
    /**
	 * 
	 * @Title: findAllRole  
	 * @author: WXN
	 * @Description: 获取用户列表(分页)(这里用一句话描述这个方法的作用)   
	 * @param: @param page
	 * @param: @return      
	 * @return: List<UserVo>      
	 * @throws
	 */
	List<RoleBg> findAllRole(Page<RoleBg> page);
	
	/**
	 * 
	 * @描述：根据角色名称查询角色list
	 * @创建时间：  2017年8月22日
	 * @作者：武向楠
	 * @参数： @param roleName
	 * @参数： @return      
	 * @return List<RoleBg>     
	 * @throws
	 */
	List<RoleBg> getRoleListByName(@Param("roleName") String roleName);
	
	/**
	 * 
	 * @Title: saveRoleAdd  
	 * @author: WXN
	 * @Description: 保存新增角色(这里用一句话描述这个方法的作用)   
	 * @param: @param Role
	 * @param: @return      
	 * @return: int      
	 * @throws
	 */
	int saveRoleAdd(RoleBg Role);
	
	/**
	 * 
	 * @Title: saveRoleEdit  
	 * @author: WXN
	 * @Description: 保存编辑角色(这里用一句话描述这个方法的作用)   
	 * @param: @param Role
	 * @param: @return      
	 * @return: int      
	 * @throws
	 */
	int saveRoleEdit(RoleBg Role);
	
	/**
    * 
    * @Title: selectUserByRoleId  
    * @author: WXN
    * @Description: 根据角色id获取用户(这里用一句话描述这个方法的作用)   
    * @param: @param id
    * @param: @return      
    * @return: int      
    * @throws
    */
   int selectUserByRoleId(Long id) ;
   
   /**
    * 
    * @Title: selectUserByRoleId  
    * @author: WXN
    * @Description: 删除角色(这里用一句话描述这个方法的作用)   
    * @param: @param id
    * @param: @return      
    * @return: int      
    * @throws
    */
   int deleteRole(RoleBg Role);
    
    /**
	 * 
	 * @Title: findAllRoleList  
	 * @author: WXN
	 * @Description: 获取所有角色(这里用一句话描述这个方法的作用)   
	 * @param: @return      
	 * @return: List<Role>      
	 * @throws
	 */
	List<RoleBg> findAllRoleList();
	
	/**
	 * 
	 * @Title: findAllRoleList  
	 * @author: WXN
	 * @Description: 获取所有部门(这里用一句话描述这个方法的作用)   
	 * @param: @return      
	 * @return: List<Role>      
	 * @throws
	 */
	List<Organization> findAllOrgList();
    
}