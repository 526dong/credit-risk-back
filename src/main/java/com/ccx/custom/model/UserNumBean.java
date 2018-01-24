package com.ccx.custom.model;

import java.io.Serializable;
import java.util.Date;


/**
 * 
* @ClassName: RoleBg 
* @Description: 用户数量表(这里用一句话描述这个类的作用) 
* @author WXN 
* @date 2017年7月13日 上午9:59:53 
*
 */
public class UserNumBean implements Serializable{
    /** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/
	private static final long serialVersionUID = 1L;

	private Long id;

    private Integer institutionId;
    
    private Integer limitNum;

    private String creater;

    private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(Integer institutionId) {
		this.institutionId = institutionId;
	}

	public Integer getLimitNum() {
		return limitNum;
	}

	public void setLimitNum(Integer limitNum) {
		this.limitNum = limitNum;
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

	@Override
	public String toString() {
		return "UserNumBean [id=" + id + ", institutionId=" + institutionId + ", limitNum=" + limitNum + ", creater="
				+ creater + ", createTime=" + createTime + "]";
	}

}