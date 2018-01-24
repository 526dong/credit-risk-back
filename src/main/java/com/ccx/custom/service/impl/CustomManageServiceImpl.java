package com.ccx.custom.service.impl;	
	
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ccx.custom.dao.CustomManageMapper;
import com.ccx.custom.model.IPBean;
import com.ccx.custom.model.Institution;
import com.ccx.custom.model.Module;
import com.ccx.custom.model.PermissionTreeBeanFg;
import com.ccx.custom.model.RoleFg;
import com.ccx.custom.model.UserFg;
import com.ccx.custom.model.UserNumBean;
import com.ccx.custom.model.UserRoleFg;
import com.ccx.custom.model.UserVoFg;
import com.ccx.custom.service.CustomManageService;
import com.ccx.util.page.Page;

@Service	
public class CustomManageServiceImpl implements CustomManageService{	

	@Autowired
	private CustomManageMapper customManageMapper;
	
	/**
	 * 
	 * @Title: CustomManageServiceImpl   
	 * @Description: 查询所有机构列表(这里用一句话描述这个方法的作用)   
	 * @param: @param page
	 * @param: @return
	 * @throws
	 */
	@Override
	public Page<Institution> findAllIns(Page<Institution> page){
		page.setRows(customManageMapper.findAllIns(page));
		return page;
	}
	
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
	@Override
	public String saveCsutomAdd(Institution institution){
		String result = "999";
		try {
			//新增
			int s = customManageMapper.saveCsutomAdd(institution);
			if(s>0){
				result = "1000";
			}else{
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
	 * @Title: saveCsutomEdit  
	 * @author: WXN
	 * @Description: 编辑机构(这里用一句话描述这个方法的作用)   
	 * @param: @param institution
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	@Override
	public String saveCsutomEdit(Institution institution){
		String result = "999";
		try {
			//新增
			int s = customManageMapper.saveCsutomEdit(institution);
			if(s>0){
				result = "1000";
			}else{
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
	 * @描述：校验机构是否已存在
	 * @创建时间：  2017年8月16日
	 * @作者：武向楠
	 */
	@Override
	public List<Institution> getInstitutionByName(String insName){
		return customManageMapper.getInstitutionByName(insName);
	}
	
	/**
	 * 
	 * @描述：检验组织代码是否唯一
	 * @创建时间：  2017年9月4日
	 * @作者：武向楠
	 */
	@Override
	public List<Institution> checkInsOrganizationCode(Map<String,Object> paramMap){
		return customManageMapper.checkInsOrganizationCode(paramMap);
	}
	
	/**
	 * 
	 * @描述：根据机构id查询机构实体
	 * @创建时间：  2017年8月16日
	 * @作者：武向楠
	 */
	@Override
	public Institution selectInstitutionById(Long id){
		return customManageMapper.selectInstitutionById(id);
	}
	
	/**
	 * 
	 * @Title: CustomManageServiceImpl   
	 * @Description: 机构审核(这里用一句话描述这个方法的作用)   
	 * @param: @param institution
	 * @param: @return
	 * @throws
	 */
	@Override
	public String csutomExamine(Institution institution){
		String result = "999";
		try {
			//新增
			int s = customManageMapper.csutomExamine(institution);
			if(s>0){
				result = "1000";
			}else{
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
	 * @Title: getModuleByInsId  
	 * @author: WXN
	 * @Description: 查询该机构已经分配的模块(这里用一句话描述这个方法的作用)   
	 * @param: @param insId
	 * @param: @return      
	 * @return: List<Module>      
	 * @throws
	 */
	@Override
	public List<Module> getModuleByInsId(Long insId){
		return customManageMapper.getModuleByInsId(insId);
	}
	
	/**
	 * 
	 * @描述：查询该机构已经分配的模块
	 * @创建时间：  2017年8月17日
	 * @作者：武向楠
	 */
	@Override
	public Page<Module> getModuleListByInsId(Page<Module> page){
		page.setRows(customManageMapper.getModuleListByInsId(page));
		return page;
	}
	
	/**
	 * 
	 * @描述：查询所有模块，并且附带该机构模块信息
	 * @创建时间：  2017年8月30日
	 * @作者：武向楠
	 */
	@Override
	public Page<Module> getAllModuleList(Page<Module> page){
		page.setRows(customManageMapper.getAllModuleList(page));
		return page;
	}
	
	/**
	 * 
	 * @描述：查询所有机构列表
	 * @创建时间：  2017年8月24日
	 * @作者：武向楠
	 */
	@Override
	public List<Institution> findAllExportIns(){
		return customManageMapper.findAllExportIns();
	}
	
	/**
	 * 
	 * @Title: getAllModule  
	 * @author: WXN
	 * @Description: 查询该所有模块(这里用一句话描述这个方法的作用)   
	 * @param: @return      
	 * @return: List<Module>      
	 * @throws
	 */
	@Override
	public List<Module> getAllModule(){
		return customManageMapper.getAllModule();
	}
	
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
	@Override
	public String saveModuleAdd(List<Module> moduleList){
		String result = "999";
		try {
			//新增
			int s = customManageMapper.saveModuleAdd(moduleList);
			if(s>0){
				result = "1000";
			}else{
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
	 * @描述：保存新增
	 * @创建时间：  2017年8月30日
	 * @作者：武向楠
	 */
	@Override
	public String saveNewModuleAdd(Module Module){
		String result = "999";
		try {
			//新增
			int s = customManageMapper.saveNewModuleAdd(Module);
			if(s>0){
				result = "1000";
			}else{
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
	 * @Title: saveModuleEdit  
	 * @author: WXN
	 * @Description: 保存模块修改(这里用一句话描述这个方法的作用)   
	 * @param: @param module
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	@Override
	public String saveModuleEdit(Module module){
		String result = "999";
		try {
			//新增
			int s = customManageMapper.saveModuleEdit(module);
			if(s>0){
				result = "1000";
			}else{
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
	 * @Title: updateModuleState  
	 * @author: WXN
	 * @Description: 修改模块状态(这里用一句话描述这个方法的作用)   
	 * @param: @param module
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	@Override
	public String updateModuleState(Module module){
		String result = "999";
		try {
			//新增
			int s = customManageMapper.updateModuleState(module);
			if(s>0){
				result = "1000";
			}else{
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
	 * @Title: findAllInsRoleList  
	 * @author: WXN
	 * @Description: 查询所有机构角色列表(这里用一句话描述这个方法的作用)   
	 * @param: @param page
	 * @param: @return      
	 * @return: Page<RoleFg>      
	 * @throws
	 */
	@Override
	public Page<RoleFg> findAllInsRoleList(Page<RoleFg> page){
		page.setRows(customManageMapper.findAllInsRoleList(page));
		return page;
	}
	
	/**
	 * 
	 * @描述：校验角色是否唯一
	 * @创建时间：  2017年8月18日
	 * @作者：武向楠
	 */
	@Override
	public RoleFg getRoleByNameAndInsId(Map<String, Object> map){
		return customManageMapper.getRoleByNameAndInsId(map);
	}
	
	/**
	 * 
	 * @Title: CustomManageServiceImpl   
	 * @Description: 保存新增机构角色(这里用一句话描述这个方法的作用)   
	 * @param: @param roleFg
	 * @param: @return
	 * @throws
	 */
	@Override
	public String saveInsRoleAdd(RoleFg roleFg){
		String result = "999";
		try {
			//新增
			int s = customManageMapper.saveInsRoleAdd(roleFg);
			if(s>0){
				result = "1000";
			}else{
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
	 * @Title: CustomManageServiceImpl   
	 * @Description: 保存编辑角色(这里用一句话描述这个方法的作用)   
	 * @param: @param roleFg
	 * @param: @return
	 * @throws
	 */
	@Override
	public String saveInsRoleEdit(RoleFg roleFg){
		String result = "999";
		try {
			//新增
			int s = customManageMapper.saveInsRoleEdit(roleFg);
			if(s>0){
				result = "1000";
			}else{
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
	 * @Title: finUserByRoleId  
	 * @author: WXN
	 * @Description: 通过角色id查找用户(这里用一句话描述这个方法的作用)   
	 * @param: @param roleId
	 * @param: @return      
	 * @return: int      
	 * @throws
	 */
	@Override
	public int finUserByRoleId(long roleId){
		return customManageMapper.finUserByRoleId(roleId);
	}
	
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
	@Override
	public String deleteInsRole(RoleFg roleFg){
		String result = "999";
		try {
			//新增
			int s = customManageMapper.deleteInsRole(roleFg);
			if(s>0){
				result = "1000";
			}else{
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
	 * @Title: getInsRoleById  
	 * @author: WXN
	 * @Description: 通过id查找机构角色(这里用一句话描述这个方法的作用)   
	 * @param: @param roleId
	 * @param: @return      
	 * @return: RoleFg      
	 * @throws
	 */
	@Override
	public RoleFg getInsRoleById(long roleId){
		return customManageMapper.getInsRoleById(roleId);
	}
	
	/**
	 * 
	 * @Title: CustomManageServiceImpl   
	 * @Description: 查询该机构下的权限树(这里用一句话描述这个方法的作用)   
	 * @param: @param roleId
	 * @param: @param insId
	 * @param: @return
	 * @throws
	 */
	@Override
	public String findInsPermissionTree(long roleId,long insId){
		//根据机构查询所拥有的模块		
		List<Module> moduleList = customManageMapper.getModuleByInsId(insId);
		List<PermissionTreeBeanFg> list = new ArrayList<PermissionTreeBeanFg>();
		String myselfId = "manage-0";
		List<PermissionTreeBeanFg> list1 = customManageMapper.findInsPermissionTreeMy(roleId,myselfId);
		list.addAll(list1);
		if(null != moduleList && moduleList.size()>0){
			for (int i = 0; i < moduleList.size(); i++) {
				myselfId = moduleList.get(i).getMyselfId();
				list1 = customManageMapper.findInsPermissionTreeMy(roleId,myselfId);
				list.addAll(list1);
			}
		}
		String result = "";
		if(null != list && list.size()>1){
			result = createTreeJson(list);
		}else{
			JSONArray rootArray = new JSONArray();
			result = rootArray.toString();
		}
		return result;
	}
	
	/**
	 * 
	 * @Title: ResourceServiceImpl   
	 * @Description: 保存分配后的权限(这里用一句话描述这个方法的作用)   
	 * @param: @param id
	 * @param: @param perIdList
	 * @param: @return
	 * @throws
	 */
	@Override
	public int addInsRolePermission(String id, List<String> perIdList){
		int flag=1;
		if(customManageMapper.delRolePermissionByRole(Long.parseLong(id))>=0){//保存角色权限之前先删除该角色之前拥有的权限
			for(String perId:perIdList){
				if(null!=perId && !"".equals(perId) && !perId.equals("-1")){
					int perIdd = customManageMapper.getPermissionId(perId);//perId为权限唯一标识，根据标识拿出主键id
					if(customManageMapper.addInsRolePermission(Long.parseLong(id),Long.valueOf(perIdd))<0){
						 flag=0;
					 }
				}
			}	
		}else{
			flag=0;
		}
		return flag;
	}
	
	
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
	@Override
	public UserNumBean getUserNumByInsId(long insId){
		return customManageMapper.getUserNumByInsId(insId);
	}
	
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
	@Override
	public String getHasUserNum(long insId){
		return customManageMapper.getHasUserNum(insId);
	}
	
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
	@Override
	public String saveEditUserNum(UserNumBean userNumBean){
		String result = "999";
		try {
			if(customManageMapper.delUserNum(userNumBean)>=0){//保存账号数量之前先删除该机构之前的账号数量
				//新增
				int s = customManageMapper.saveEditUserNum(userNumBean);
				if(s>0){
					result = "1000";
				}else{
					result = "999";
				}
			}else{
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
	 * @Title: findAllInsUserList  
	 * @author: WXN
	 * @Description: 查询该机构所有用户列表(这里用一句话描述这个方法的作用)   
	 * @param: @param pages
	 * @param: @return      
	 * @return: Page<UserFg>      
	 * @throws
	 */
	@Override
	public Page<UserFg> findAllInsUserList(Page<UserFg> pages){
		pages.setRows(customManageMapper.findAllInsUserList(pages));
		return pages;
	}
	
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
	@Override
	public List<RoleFg> findAllInsRole(long insId){
		return customManageMapper.findAllInsRole(insId);
	}
	
	/**
	 * 
	 * @描述：根据userId获取机构下的用户
	 * @创建时间：  2017年8月21日
	 * @作者：武向楠
	 */
	@Override
	public UserVoFg getInsUserById(long userId){
		return customManageMapper.getInsUserById(userId);
	}
	
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
	@Override
	public void saveAddInsUser(UserFg user){
		customManageMapper.saveAddInsUser(user);
	}
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
	@Override
	public void addInsRoleToInsUser(UserRoleFg userRole){
		customManageMapper.addInsRoleToInsUser(userRole);
	}
	
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
	@Override
	public UserFg getUserByName(Map<String,Object> mapParam){
		return customManageMapper.getUserByName(mapParam);
	}
	
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
	@Override
	public List<UserFg> getUserListByName(Map<String,Object> mapParam){
		return customManageMapper.getUserListByName(mapParam);
	}
	
	/**
	 * 
	 * @Title: updateInsUser  
	 * @author: WXN
	 * @Description: 修改机构用户(这里用一句话描述这个方法的作用)   
	 * @param: @param user
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	@Override
	public String updateInsUser(UserFg user){
		String result = "999";
		try {
			int s = customManageMapper.updateInsUser(user);
			if(s>0){
				result = "1000";
			}else{
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
	 * @Title: CustomManageServiceImpl   
	 * @Description: 查询该机构所有的ip列表(这里用一句话描述这个方法的作用)   
	 * @param: @param pages
	 * @param: @return
	 * @throws
	 */
	@Override
	public Page<IPBean> findAllInsIPList(Page<IPBean> pages){
		pages.setRows(customManageMapper.findAllInsIPList(pages));
		return pages;
	}
	
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
	@Override
	public String updateInsRole(UserRoleFg userRole){
		String result = "999";
		try {
			int s = customManageMapper.updateInsRole(userRole);
			if(s>0){
				result = "1000";
			}else{
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
	 * @Title: saveInsIPAdd  
	 * @author: WXN
	 * @Description: 新增机构ip(这里用一句话描述这个方法的作用)   
	 * @param: @param iPBean
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	@Override
	public String saveInsIPAdd(IPBean iPBean){
		String result = "999";
		try {
			int s = customManageMapper.saveInsIPAdd(iPBean);
			if(s>0){
				result = "1000";
			}else{
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
	 * @Title: saveInsIPEdit  
	 * @author: WXN
	 * @Description: 编辑机构ip(这里用一句话描述这个方法的作用)   
	 * @param: @param iPBean
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	@Override
	public String saveInsIPEdit(IPBean iPBean){
		String result = "999";
		try {
			int s = customManageMapper.saveInsIPEdit(iPBean);
			if(s>0){
				result = "1000";
			}else{
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
	 * @描述：检验ip是否已存在
	 * @创建时间：  2017年8月21日
	 * @作者：武向楠
	 */
	@Override
	public List<IPBean> checkIPAdress(Map<String,Object> map){
		return customManageMapper.checkIPAdress(map);
	}
	
	
	
	
	
	/**
	   * 创建一颗树，以json字符串形式返回
	   * @param list 原始数据列表
	   * @return 树
	   */
	  private String createTreeJson(List<PermissionTreeBeanFg> list) {
	    JSONArray rootArray = new JSONArray();
	    for (int i=0; i<list.size(); i++) {
	    	PermissionTreeBeanFg resource = list.get(i);
	      if (resource.getParentId().equals("0")) {
	        JSONObject rootObj = createBranch(list, resource);
	        rootArray.add(rootObj);
	      }
	    }
	    return rootArray.toString();
	  }
	  
	  /**
	   * 递归创建分支节点Json对象
	   * @param list 创建树的原始数据
	   * @param currentNode 当前节点
	   * @return 当前节点与该节点的子节点json对象
	   */
	  private JSONObject createBranch(List<PermissionTreeBeanFg> list, PermissionTreeBeanFg currentNode) {
	    /*
	     * 将javabean对象解析成为JSON对象
	     */
	    JSONObject currentObj = (JSONObject) JSON.toJSON(currentNode);
	    JSONArray childArray = new JSONArray();
	    /*
	     * 循环遍历原始数据列表，判断列表中某对象的父id值是否等于当前节点的id值，
	     * 如果相等，进入递归创建新节点的子节点，直至无子节点时，返回节点，并将该
	     * 节点放入当前节点的子节点列表中
	     */
	    for (int i=0; i<list.size(); i++) {
	    	PermissionTreeBeanFg newNode = list.get(i);
	      if (newNode.getParentId()!=null && newNode.getParentId().compareTo(currentNode.getId()) == 0) {
	        JSONObject childObj = createBranch(list, newNode);
	        childArray.add(childObj);
	      }
	    }
	    
	    /*
	     * 判断当前子节点数组是否为空，不为空将子节点数组加入children字段中
	     */
	    if (!childArray.isEmpty()) {
	    	currentObj.remove("checked");
	    	currentObj.put("children", childArray);
	    	currentObj.put("state", "closed");
	    }
	    return currentObj;
	  }
	
}	
