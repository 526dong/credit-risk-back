package com.ccx.dictionary.model;

import java.io.Serializable;
import java.util.Date;

import com.ccx.util.DateUtils;

/**
 * 企业性质
 * @author xzd
 * @date 2017/7/4
 */
public class EnterpriseNature implements Serializable{
	private Integer id;
	
	private String code;//性质编号
	private String name;//性质名称
	
	private String creatorName;//创建人姓名 
	private Date createDate;//创建时间

	private Integer deleteFlag;//是否删除：0-否，1-是。默认0

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getCreateDate() throws Exception {
		if (createDate != null) {
     		return DateUtils.formatDate(createDate);
 		}else{
 			return null;
 		}
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

}
