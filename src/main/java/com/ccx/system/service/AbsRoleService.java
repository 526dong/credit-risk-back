package com.ccx.system.service;	
	
import com.ccx.system.model.Organization;
import com.ccx.system.model.RoleBg;
import com.ccx.util.page.Page;

import java.util.*;

	
public interface AbsRoleService {	
		
	/**
	 * 
	 * @Title: findAllRole  
	 * @author: WXN
	 * @Description: 获取用户列表(分页)(这里用一句话描述这个方法的作用)   
	 * @param: @param params
	 * @param: @return      
	 * @return: PageInfo<UserVo>      
	 * @throws
	 */
	Page<RoleBg> findAllRole(Page<RoleBg> Page);
	
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
	List<RoleBg> getRoleListByName(String roleName);
	
	/**
	 * 
	 * @Title: saveRoleAdd  
	 * @author: WXN
	 * @Description: 保存新增角色(这里用一句话描述这个方法的作用)   
	 * @param: @param absRole
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	String saveRoleAdd(RoleBg absRole);
	
	 /**
	  * 
	  * @Title: saveRoleEdit  
	  * @author: WXN
	  * @Description: 保存编辑角色(这里用一句话描述这个方法的作用)   
	  * @param: @param absRole
	  * @param: @return      
	  * @return: String      
	  * @throws
	  */
    String saveRoleEdit(RoleBg absRole);
    
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
    int selectUserByRoleId(Long id);
    
    /**
     * 
     * @Title: deleteRole  
     * @author: WXN
     * @Description: 删除角色(这里用一句话描述这个方法的作用)   
     * @param: @param absRole
     * @param: @return      
     * @return: String      
     * @throws
     */
    String deleteRole(RoleBg absRole);
	
	/**
	 * 
	 * @Title: findAllRoleList  
	 * @author: WXN
	 * @Description: 获取所有角色(这里用一句话描述这个方法的作用)   
	 * @param: @return      
	 * @return: List<AbsRole>      
	 * @throws
	 */
	List<RoleBg> findAllRoleList();	
	
	/**
	 * 
	 * @Title: findAllRoleList  
	 * @author: WXN
	 * @Description: 获取所有部门(这里用一句话描述这个方法的作用)   
	 * @param: @return      
	 * @return: List<AbsRole>      
	 * @throws
	 */
	List<Organization> findAllOrgList();	
	
	
	//主键获取	
	RoleBg getById(Long id);	
		
	//获取无参list	
	List<RoleBg> getList();	
		
	//获取有参数list	
	List<RoleBg> getList(RoleBg model);	
		
	//获取带分页list	
	List<RoleBg> getPageList(Page<RoleBg> page);
		
	//通过条件获取	
	RoleBg getByModel(RoleBg model);	
	
	//保存对象	
	int save(RoleBg model);	
	
	//更新对象	
	int update(RoleBg model);	
		
	//删除对象	
	int deleteById(Long id);	
		
	//其他查询	
	Map<String, Object> getOther();	
}	
