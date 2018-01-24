package com.ccx.system.model;

import java.io.Serializable;
import java.util.Date;

public class UserBg implements Serializable{
    /** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/
	private static final long serialVersionUID = 1L;

	private Long id;

    private String loginName;

    private String name;

    private String password;

    private String phone;

    private String email;

    private Integer userType;

    private Integer institutionId;

    private Integer status;

    private Integer isDel;

    private String creater;

    private Date createTime;
    
    private Integer loginNum;
    
    private Date loginTime;
    
    private Integer warnFlag;

    private Date warnTime;

    
	public Integer getLoginNum() {
		return loginNum;
	}

	public void setLoginNum(Integer loginNum) {
		this.loginNum = loginNum;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(Integer institutionId) {
		this.institutionId = institutionId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getWarnFlag() {
		return warnFlag;
	}

	public void setWarnFlag(Integer warnFlag) {
		this.warnFlag = warnFlag;
	}

	public Date getWarnTime() {
		return warnTime;
	}

	public void setWarnTime(Date warnTime) {
		this.warnTime = warnTime;
	}

	@Override
	public String toString() {
		return "UserBg [id=" + id + ", loginName=" + loginName + ", name="
				+ name + ", password=" + password + ", phone=" + phone
				+ ", email=" + email + ", userType=" + userType
				+ ", institutionId=" + institutionId + ", status=" + status
				+ ", isDel=" + isDel + ", creater=" + creater + ", createTime="
				+ createTime + ", loginNum=" + loginNum + ", loginTime="
				+ loginTime + ", warnFlag=" + warnFlag + ", warnTime="
				+ warnTime + "]";
	}

	

	



    
}