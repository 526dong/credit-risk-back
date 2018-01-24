package com.ccx.custom.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.ccx.util.DateUtils;


public class UserVoFg implements Serializable {

	private static final long serialVersionUID = -4705690842473949705L;

	private Long id;

	private String loginName;

	private String name;
	
	private String password;

	private String roleName;

	private String phone;

	private String email;
	
	private Integer userType;
	
	private String creater;

	private Date createTime;

	private Integer status;
	
	private Integer institutionId;
	
	private Integer roleId;

	private List<RoleFg> list;

	
	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(Integer institutionId) {
		this.institutionId = institutionId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCreateTime() throws Exception {
		if (createTime != null) {
			return DateUtils.formatDateStr(createTime);
		} else {
			return null;
		}
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<RoleFg> getList() {
		return list;
	}

	public void setList(List<RoleFg> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "UserVoBg [id=" + id + ", loginName=" + loginName + ", name=" + name + ", password=" + password
				+ ", roleName=" + roleName + ", phone=" + phone + ", email=" + email + ", creater=" + creater
				+ ", createTime=" + createTime + ", status=" + status + ", institutionId=" + institutionId + ", roleId="
				+ roleId + ", list=" + list + "]";
	}



	

	
	

}
