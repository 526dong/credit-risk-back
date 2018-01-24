package com.ccx.custom.service;	
	
import java.util.List;
import java.util.Map;

import com.ccx.custom.model.IPBean;
import com.ccx.custom.model.Institution;
import com.ccx.custom.model.Module;
import com.ccx.custom.model.RoleFg;
import com.ccx.custom.model.UserFg;
import com.ccx.custom.model.UserNumBean;
import com.ccx.custom.model.UserRoleFg;
import com.ccx.custom.model.UserVoFg;
import com.ccx.util.page.Page;

	
public interface CustomManageService {	

	/**
	 * 	
	 * @Title: findAllIns  
	 * @author: WXN
	 * @Description: 查询所有机构列表(这里用一句话描述这个方法的作用)   
	 * @param: @param page
	 * @param: @return      
	 * @return: Page<Institution>      
	 * @throws
	 */
	Page<Institution> findAllIns(Page<Institution> page);
	
	/**
	 * 
	 * @Title: saveCsutomAdd  
	 * @author: WXN
	 * @Description: 保存新增机构(这里用一句话描述这个方法的作用)   
	 * @param: @param institution
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	String saveCsutomAdd(Institution institution);
	
	/**
	 * 
	 * @Title: saveCsutomEdit  
	 * @author: WXN
	 * @Description: 编辑机构(这里用一句话描述这个方法的作用)   
	 * @param: @param institution
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	String saveCsutomEdit(Institution institution);
	
	/**
	 * 
	 * @Title: csutomExamine  
	 * @author: WXN
	 * @Description: 机构审核(这里用一句话描述这个方法的作用)   
	 * @param: @param institution
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	String csutomExamine(Institution institution);
	
	/**
	 * 
	 * @描述：校验机构是否已存在
	 * @创建时间：  2017年8月16日
	 * @作者：武向楠
	 * @参数： @param insName
	 * @参数： @return      
	 * @return List<Institution>     
	 * @throws
	 */
	List<Institution> getInstitutionByName(String insName);
	
	/**
	 * 
	 * @描述：检验组织代码是否唯一
	 * @创建时间：  2017年9月4日
	 * @作者：武向楠
	 * @参数： @param paramMap
	 * @参数： @return      
	 * @return List<Institution>     
	 * @throws
	 */
	List<Institution> checkInsOrganizationCode(Map<String,Object> paramMap);
	
	/**
	 * 
	 * @描述：根据机构id查询机构实体
	 * @创建时间：  2017年8月16日
	 * @作者：武向楠
	 * @参数： @param id
	 * @参数： @return      
	 * @return Institution     
	 * @throws
	 */
	Institution selectInstitutionById(Long id);
	
	/**
	 * 
	 * @Title: getModuleByInsId  
	 * @author: WXN
	 * @Description: 查询该机构已经分配的模块(这里用一句话描述这个方法的作用)   
	 * @param: @param insId
	 * @param: @return      
	 * @return: List<Module>      
	 * @throws
	 */
	List<Module> getModuleByInsId(Long insId);
	
	/**
	 * 
	 * @描述：查询该机构已经分配的模块(分页)
	 * @创建时间：  2017年8月17日
	 * @作者：武向楠
	 * @参数： @param page
	 * @参数： @return      
	 * @return Page<Module>     
	 * @throws
	 */
	Page<Module> getModuleListByInsId(Page<Module> page);
	
	/**
	 * 
	 * @描述：查询所有模块，并且附带该机构模块信息
	 * @创建时间：  2017年8月30日
	 * @作者：武向楠
	 * @参数： @param page
	 * @参数： @return      
	 * @return Page<Module>     
	 * @throws
	 */
	Page<Module> getAllModuleList(Page<Module> page);
	
	/**
	 * 
	 * @描述：查询所有机构列表
	 * @创建时间：  2017年8月24日
	 * @作者：武向楠
	 * @参数： @return      
	 * @return List<Institution>     
	 * @throws
	 */
	List<Institution> findAllExportIns();
	
	/**
	 * 
	 * @Title: getAllModule  
	 * @author: WXN
	 * @Description: 查询该所有模块(这里用一句话描述这个方法的作用)   
	 * @param: @return      
	 * @return: List<Module>      
	 * @throws
	 */
	List<Module> getAllModule();
	
	/**
	 * 
	 * @Title: saveModuleAdd  
	 * @author: WXN
	 * @Description: 保存新增(这里用一句话描述这个方法的作用)   
	 * @param: @param module
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	String saveModuleAdd(List<Module> moduleList);
	
	/**
	 * 
	 * @Title: saveNewModuleAdd  
	 * @author: WXN
	 * @Description: 保存新增(这里用一句话描述这个方法的作用)   
	 * @param: @param module
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	String saveNewModuleAdd(Module Module);
	
	/**
	 * 
	 * @Title: saveModuleEdit  
	 * @author: WXN
	 * @Description: 保存模块修改(这里用一句话描述这个方法的作用)   
	 * @param: @param module
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	String saveModuleEdit(Module module);
	
	/**
	 * 
	 * @Title: updateModuleState  
	 * @author: WXN
	 * @Description: 修改模块状态(这里用一句话描述这个方法的作用)   
	 * @param: @param module
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	String updateModuleState(Module module);
	
	/**
	 * 
	 * @Title: findAllInsRoleList  
	 * @author: WXN
	 * @Description: 查询所有机构列表(这里用一句话描述这个方法的作用)   
	 * @param: @param page
	 * @param: @return      
	 * @return: Page<RoleFg>      
	 * @throws
	 */
	Page<RoleFg> findAllInsRoleList(Page<RoleFg> page);
	
	/**
	 * 
	 * @描述：校验角色是否唯一
	 * @创建时间：  2017年8月18日
	 * @作者：武向楠
	 * @参数： @param map
	 * @参数： @return      
	 * @return RoleFg     
	 * @throws
	 */
	RoleFg getRoleByNameAndInsId(Map<String, Object> map);
	
	/**
	 * 
	 * @Title: saveInsRoleAdd  
	 * @author: WXN
	 * @Description: 保存新增机构角色(这里用一句话描述这个方法的作用)   
	 * @param: @param roleFg
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	String saveInsRoleAdd(RoleFg roleFg);
	
	/**
	 * 
	 * @Title: saveInsRoleEdit  
	 * @author: WXN
	 * @Description: 保存编辑角色(这里用一句话描述这个方法的作用)   
	 * @param: @param roleFg
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	String saveInsRoleEdit(RoleFg roleFg);
	
	/**
	 * 
	 * @Title: finUserByRoleId  
	 * @author: WXN
	 * @Description: 通过角色id查找用户(这里用一句话描述这个方法的作用)   
	 * @param: @param roleId
	 * @param: @return      
	 * @return: int      
	 * @throws
	 */
	int finUserByRoleId(long roleId);
	
	/**
	 * 
	 * @Title: deleteInsRole  
	 * @author: WXN
	 * @Description: 删除角色(这里用一句话描述这个方法的作用)   
	 * @param: @param roleFg
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	String deleteInsRole(RoleFg roleFg);
	
	/**
	 * 
	 * @Title: getInsRoleById  
	 * @author: WXN
	 * @Description: 通过id查找机构角色(这里用一句话描述这个方法的作用)   
	 * @param: @param roleId
	 * @param: @return      
	 * @return: RoleFg      
	 * @throws
	 */
	RoleFg getInsRoleById(long roleId);
	
	/**
	 * 
	 * @Title: findInsPermissionTree  
	 * @author: WXN
	 * @Description: 查询该机构下的权限树(这里用一句话描述这个方法的作用)   
	 * @param: @param id
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	String findInsPermissionTree(long roleId,long insId);
	
	/**
	 * 
	 * @Title: addInsRolePermission  
	 * @author: WXN
	 * @Description: 保存分配后的权限(这里用一句话描述这个方法的作用)   
	 * @param: @param id
	 * @param: @param perIdList
	 * @param: @return      
	 * @return: int      
	 * @throws
	 */
	int addInsRolePermission(String id, List<String> perIdList);
	
	/**
	 * 
	 * @Title: getUserNumByInsId  
	 * @author: WXN
	 * @Description: 通过机构id获取该机构下最多可设置的账号数量(这里用一句话描述这个方法的作用)   
	 * @param: @param id
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	UserNumBean getUserNumByInsId(long insId);
	
	/**
	 * 
	 * @Title: getHasUserNum  
	 * @author: WXN
	 * @Description: 获取该机构下已经创建的账号数量(这里用一句话描述这个方法的作用)   
	 * @param: @param insId
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	String getHasUserNum(long insId);
	
	/**
	 * 
	 * @Title: saveEditUserNum  
	 * @author: WXN
	 * @Description: 修改该机构下设置账号的最大数量(这里用一句话描述这个方法的作用)   
	 * @param: @param mapPram
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	String saveEditUserNum(UserNumBean userNumBean);
	
	/**
	 * 
	 * @Title: findAllInsUserList  
	 * @author: WXN
	 * @Description: 查询该机构所有用户列表(这里用一句话描述这个方法的作用)   
	 * @param: @param pages
	 * @param: @return      
	 * @return: Page<UserFg>      
	 * @throws
	 */
	Page<UserFg> findAllInsUserList(Page<UserFg> pages);
	
	/**
	 * 
	 * @Title: findAllInsRole  
	 * @author: WXN
	 * @Description: 获取所有该机构下的角色(这里用一句话描述这个方法的作用)   
	 * @param: @param insId
	 * @param: @return      
	 * @return: List<RoleFg>      
	 * @throws
	 */
	List<RoleFg> findAllInsRole(long insId);
	
	/**
	 * 
	 * @描述：根据userId获取机构下的用户
	 * @创建时间：  2017年8月21日
	 * @作者：武向楠
	 * @参数： @param userId
	 * @参数： @return      
	 * @return UserFg     
	 * @throws
	 */
	UserVoFg getInsUserById(long userId);
	
	/**
	 * 
	 * @Title: saveAddInsUser  
	 * @author: WXN
	 * @Description: 新增机构用户(这里用一句话描述这个方法的作用)   
	 * @param: @param user
	 * @param: @return      
	 * @return: int      
	 * @throws
	 */
	void saveAddInsUser(UserFg user);
	/**
	 * 
	 * @Title: saveAddInsUser  
	 * @author: WXN
	 * @Description: 新增机构用户角色关联(这里用一句话描述这个方法的作用)   
	 * @param: @param user
	 * @param: @return      
	 * @return: int      
	 * @throws
	 */
	void addInsRoleToInsUser(UserRoleFg userRole);
	
	/**
	 * 
	 * @Title: getUserByName  
	 * @author: WXN
	 * @Description: 查询该机构下登录名是否已存在(这里用一句话描述这个方法的作用)   
	 * @param: @param loginName
	 * @param: @return      
	 * @return: UserFg      
	 * @throws
	 */
	UserFg getUserByName(Map<String,Object> mapParam);
	
	/**
	 * 
	 * @Title: getUserByName  
	 * @author: WXN
	 * @Description: 查询该机构下登录名是否已存在(这里用一句话描述这个方法的作用)   
	 * @param: @param loginName
	 * @param: @return      
	 * @return: UserFg      
	 * @throws
	 */
	List<UserFg> getUserListByName(Map<String,Object> mapParam);
	
	/**
	 * 
	 * @Title: updateInsUser  
	 * @author: WXN
	 * @Description: 修改机构用户状态(这里用一句话描述这个方法的作用)   
	 * @param: @param user
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	String updateInsUser(UserFg user);
	
	/**
	 * 
	 * @描述：修改机构角色
	 * @创建时间：  2017年8月21日
	 * @作者：武向楠
	 * @参数： @param userRole
	 * @参数： @return      
	 * @return String     
	 * @throws
	 */
	String updateInsRole(UserRoleFg userRole);
	
	/**
	 * 
	 * @Title: findAllInsIPList  
	 * @author: WXN
	 * @Description: 查询该机构所有的ip列表(这里用一句话描述这个方法的作用)   
	 * @param: @param pages
	 * @param: @return      
	 * @return: Page<IPBean>      
	 * @throws
	 */
	Page<IPBean> findAllInsIPList(Page<IPBean> pages);
	
	/**
	 * 
	 * @Title: saveInsIPAdd  
	 * @author: WXN
	 * @Description: 新增机构ip(这里用一句话描述这个方法的作用)   
	 * @param: @param iPBean
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	String saveInsIPAdd(IPBean iPBean);
	
	/**
	 * 
	 * @Title: saveInsIPEdit  
	 * @author: WXN
	 * @Description: 编辑机构ip(这里用一句话描述这个方法的作用)   
	 * @param: @param iPBean
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	String saveInsIPEdit(IPBean iPBean);
	
	/**
	 * 
	 * @描述：检验ip是否已存在
	 * @创建时间：  2017年8月21日
	 * @作者：武向楠
	 * @参数： @return      
	 * @return List<IPBean>     
	 * @throws
	 */
	List<IPBean> checkIPAdress(Map<String,Object> map);
	
	
	
}	
