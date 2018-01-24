package com.ccx.system.service.impl;	
	
import com.ccx.system.model.Organization;
import com.ccx.system.model.RoleBg;
import com.ccx.system.service.AbsRoleService;	
import com.ccx.system.dao.AbsRoleMapper;
import com.ccx.util.page.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;	

import java.util.*;	
	
@Service	
public class AbsRoleServiceImpl implements AbsRoleService {	
		
	@Autowired	
    private AbsRoleMapper absRoleMapper;	
		
	/**
	 * 
	 * @Title: AbsRoleServiceImpl   
	 * @Description: 获取用户列表(分页)(这里用一句话描述这个方法的作用)   
	 * @param: @param Page
	 * @param: @return
	 * @throws
	 */
	@Override
	public Page<RoleBg> findAllRole(Page<RoleBg> page) {
		page.setRows(absRoleMapper.findAllRole(page));
		return page;
	}
	
	/**
	 * 
	 * @描述：根据角色名称查询角色list
	 * @创建时间：  2017年8月22日
	 * @作者：武向楠
	 */
	@Override
	public List<RoleBg> getRoleListByName(String roleName){
		return absRoleMapper.getRoleListByName(roleName);
	}
	
	/**
	 * 
	 * @Title: AbsRoleServiceImpl   
	 * @Description: 保存新增角色(这里用一句话描述这个方法的作用)   
	 * @param: @param absRole
	 * @param: @return
	 * @throws
	 */
	@Override
	public String saveRoleAdd(RoleBg absRole){
		String result = "999";
		try {
			int s = absRoleMapper.saveRoleAdd(absRole);
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
	 * @Title: saveRoleEdit  
	 * @author: WXN
	 * @Description: 保存编辑角色(这里用一句话描述这个方法的作用)   
	 * @param: @param absRole
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
   @Override
	public String saveRoleEdit(RoleBg absRole){
		String result = "999";
		try {
			int s = absRoleMapper.saveRoleEdit(absRole);
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
    * @Title: selectUserByRoleId  
    * @author: WXN
    * @Description: 根据角色id获取用户(这里用一句话描述这个方法的作用)   
    * @param: @param id
    * @param: @return      
    * @return: int      
    * @throws
    */
   @Override
	public int selectUserByRoleId(Long id) {
		return absRoleMapper.selectUserByRoleId(id);
	}
   
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
   @Override
	public String deleteRole(RoleBg absRole){
		String result = "999";
		try {
			int s = absRoleMapper.deleteRole(absRole);
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
	 * @Title: findAllRoleList  
	 * @author: WXN
	 * @Description: 获取所有角色(这里用一句话描述这个方法的作用)   
	 * @param: @return      
	 * @return: List<AbsRole>      
	 * @throws
	 */
	@Override
	public List<RoleBg> findAllRoleList(){
		return absRoleMapper.findAllRoleList();
	}
	
	/**
	 * 
	 * @Title: findAllRoleList  
	 * @author: WXN
	 * @Description: 获取所有部门(这里用一句话描述这个方法的作用)   
	 * @param: @return      
	 * @return: List<AbsRole>      
	 * @throws
	 */
	@Override
	public List<Organization> findAllOrgList(){
		return absRoleMapper.findAllOrgList();
	}
	
	
	
	
	//主键获取
	@Override
	public RoleBg getById(Long id) {	
		return absRoleMapper.selectByPrimaryKey(id);	
	}	
		
	//获取无参list
	@Override
	public List<RoleBg> getList() {	
		return null;	
	}	
		
	//获取有参数list
	@Override
	public List<RoleBg> getList(RoleBg model) {	
		return null;	
	}	
		
	//获取带分页list
	@Override
	public List<RoleBg> getPageList(Page<RoleBg> page) {
		return null;	
	}	
		
	//通过条件获取
	@Override
	public RoleBg getByModel(RoleBg model) {	
		return null;	
	}	
	
	//保存对象
	@Override
	public int save(RoleBg model) {	
		return absRoleMapper.insert(model);	
	}	
	
	//更新对象
	@Override
	public int update(RoleBg model) {	
		return absRoleMapper.updateByPrimaryKey(model);	
	}	
		
	//删除对象
	@Override
	public int deleteById(Long id) {	
		return absRoleMapper.deleteByPrimaryKey(id);	
	}	
		
	//其他查询
	@Override
	public Map<String, Object> getOther() {	
		return null;	
	}	
}	
