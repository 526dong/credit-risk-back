package com.ccx.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ccx.system.dao.ResourceMapper;
import com.ccx.system.model.PermissionBeanBg;
import com.ccx.system.model.PermissionTreeBeanBg;
import com.ccx.system.service.ResourceService;

@Service()
public class ResourceServiceImpl implements ResourceService {

	
	@Autowired
    private ResourceMapper resourceMapper;
	


	
	/**
	 * 
	 * @Title: findAllPermission  
	 * @author: WXN
	 * @Description: 获取权限列表(这里用一句话描述这个方法的作用)   
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	@Override
	public String findAllPermission(){
		//找到该系统的所有资源权限。
		List<PermissionBeanBg> list = resourceMapper.findAllPermission();
		return createPTreeJson(list);
	}
	
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
	@Override
	public String addPermissions(PermissionBeanBg permissionBean){
		System.err.println("前台获取实体=====》》》》》》》》》》"+permissionBean.toString());
		String result = "1000";
		try {
			//获得父级节点
			List<PermissionBeanBg> permission1  = resourceMapper.getPermissionbyMyselfID(permissionBean.getParentId());
			permissionBean.setLevel(permission1.get(0).getLevel()+1);
			//查询以sysPermissionBean.getParentId()为父级的权限条数，即同级权限数量
			List<PermissionBeanBg> permission2  = resourceMapper.getPermissionbyParentId(permissionBean.getParentId());
			//设置序列
			//设置权限编码
			if(null != permission2 && permission2.size()>0){
				permissionBean.setSequenceNum((permission2.size()+1));
				String myselfId = permission2.get(permission2.size()-1).getMyselfId();
				String myselfId1 = myselfId.substring(myselfId.indexOf("-")+1, myselfId.length());
				int myselfId2 = Integer.parseInt(myselfId1)+1;
				permissionBean.setMyselfId("manage-"+String.valueOf(myselfId2));
			}else{
				permissionBean.setSequenceNum(1);
				String myselfId = permissionBean.getParentId();
				if(myselfId.equals("manage-0")){
					myselfId = myselfId.substring(0,myselfId.indexOf("-")+1);
					permissionBean.setMyselfId(String.valueOf(myselfId)+"10");
				}else{
					permissionBean.setMyselfId(String.valueOf(myselfId)+"01");
				}
			}
			System.err.println("新增实体=====》》》》》》》》》》"+permissionBean.toString());
			//新增资源
			int s = resourceMapper.addPermissions(permissionBean);
			if(s>0){
				result = "1000";
			}else{
				result = "9999";
			}
		} catch (Exception e) {
			e.printStackTrace(); 
			 result = "9999";
		}
		return result;
	}
	
	/**
	 * 
	 * @Title: ResourceServiceImpl   
	 * @Description: 编辑权限(这里用一句话描述这个方法的作用)   
	 * @param: @param permissionBean
	 * @param: @return
	 * @throws
	 */
	@Override
	public String modifypermission(PermissionBeanBg permissionBean){
		String result = "1000";
		try {
			//获得父级节点
			List<PermissionBeanBg> permission1  = resourceMapper.getPermissionbyMyselfID(permissionBean.getParentId());
			permissionBean.setLevel(permission1.get(0).getLevel()+1);
			//查询以sysPermissionBean.getParentId()为父级的权限条数，即同级权限数量
			List<PermissionBeanBg> permission2  = resourceMapper.getPermissionbyParentId(permissionBean.getParentId());
			//设置序列
			boolean flag = false;
			int intFlag = 0;
			if(null != permission2 && permission2.size()>0){
				for (int i = 0; i < permission2.size(); i++) {
					String id = String.valueOf(permission2.get(i).getId());
					String ids = String.valueOf(permissionBean.getId());
					if(id.equals(ids)){
						flag = true;
						intFlag = i;
					}
				}
				if(flag == false){
					permissionBean.setSequenceNum(permission2.size()+1);
					String myselfId = permission2.get(permission2.size()-1).getMyselfId();
					String myselfId1 = myselfId.substring(myselfId.indexOf("-")+1, myselfId.length());
					int myselfId2 = Integer.parseInt(myselfId1)+1;
					permissionBean.setMyselfId("manage-"+String.valueOf(myselfId2));
				}else{
					permissionBean.setSequenceNum(permission2.get(intFlag).getSequenceNum());
					permissionBean.setMyselfId(permission2.get(intFlag).getMyselfId());
				}
			}else{
				permissionBean.setSequenceNum(1);
				String myselfId = permissionBean.getParentId();
				if(myselfId.equals("antifraud-0")){
					myselfId = myselfId.substring(0,myselfId.indexOf("-")+1);
					permissionBean.setMyselfId(String.valueOf(myselfId)+"10");
				}else{
					permissionBean.setMyselfId(String.valueOf(myselfId)+"01");
				}
			}
			
			//保存编辑后的资源
			int s = resourceMapper.modifypermission(permissionBean);
			if(s>0){
				result = "1000";
			}else{
				result = "9999";
			}
		} catch (Exception e) {
			e.printStackTrace();
			 result = "9999";
		}
		return result;
	}
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
	@Override
	public String modifypermissionState(PermissionBeanBg permissionBean){
		String result = "1000";
		try {
			int s = resourceMapper.modifypermissionState(permissionBean);
			if(s>0){
				result = "1000";
			}else{
				result = "9999";
			}
		} catch (Exception e) {
			e.printStackTrace();
			 result = "9999";
		}
		return result;
	}
	
	
	/**
	 * 
	 * @Title: ResourceServiceImpl   
	 * @Description: 查询权限树(这里用一句话描述这个方法的作用)   
	 * @param: @param id
	 * @param: @return
	 * @throws
	 */
	@Override
	public String finRolePermissionTree(Long id){
		List<PermissionTreeBeanBg> list = resourceMapper.finRolePermissionTree(id);
		return createTreeJson(list);
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
	public int saveRolePermission(String id, List<String> perIdList){
		int flag=1;
		if(resourceMapper.delRolePermissionByRole(Long.parseLong(id))>=0){//保存角色权限之前先删除该角色之前拥有的权限
			for(String perId:perIdList){
				if(null!=perId && !"".equals(perId) && !perId.equals("-1")){
					int perIdd = resourceMapper.getPermissionId(perId);//perId为权限唯一标识，根据标识拿出主键id
					if(resourceMapper.saveRolePermission(Long.parseLong(id),Long.valueOf(perIdd))<0){
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
	 * @Title: selectResourceById  
	 * @author: WXN
	 * @Description: 根据资源id查询资源实体(这里用一句话描述这个方法的作用)   
	 * @param: @param resId
	 * @param: @return      
	 * @return: PermissionBeanBg      
	 * @throws
	 */
	@Override
	public PermissionBeanBg selectResourceById(Long resId){
		return resourceMapper.selectResourceById(resId);
	}
	
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
	@Override
	public List<Long> getResByUserId(Long id){
		return resourceMapper.getResByUserId(id);
	}
	
	
	
	 /**
	   * 创建一颗树，以json字符串形式返回
	   * @param list 原始数据列表
	   * @return 树
	   */
	  private String createPTreeJson(List<PermissionBeanBg> list) {
	    JSONArray rootArray = new JSONArray();
	    for (int i=0; i<list.size(); i++) {
	    	PermissionBeanBg resource = list.get(i);
	      if (resource.getParentId().equals("0")) {
	        JSONObject rootObj = createPBranch(list, resource);
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
	  private JSONObject createPBranch(List<PermissionBeanBg> list, PermissionBeanBg currentNode) {
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
	    	PermissionBeanBg newNode = list.get(i);
	      if (newNode.getParentId()!=null && newNode.getParentId().compareTo(currentNode.getMyselfId()) == 0) {
	        JSONObject childObj = createPBranch(list, newNode);
	        childArray.add(childObj);
	      }
	    }
	    
	    /*
	     * 判断当前子节点数组是否为空，不为空将子节点数组加入children字段中
	     */
	    if (!childArray.isEmpty()) {
	      currentObj.put("children", childArray);
	      currentObj.put("state", "closed");
	    }
	    
	    return currentObj;
	  }
	  
	  /**
	   * 创建一颗树，以json字符串形式返回
	   * @param list 原始数据列表
	   * @return 树
	   */
	  private String createTreeJson(List<PermissionTreeBeanBg> list) {
	    JSONArray rootArray = new JSONArray();
	    for (int i=0; i<list.size(); i++) {
	    	PermissionTreeBeanBg resource = list.get(i);
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
	  private JSONObject createBranch(List<PermissionTreeBeanBg> list, PermissionTreeBeanBg currentNode) {
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
	    	PermissionTreeBeanBg newNode = list.get(i);
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
