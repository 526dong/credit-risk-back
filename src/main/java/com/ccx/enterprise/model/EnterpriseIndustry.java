package com.ccx.enterprise.model;

import java.io.Serializable;
import java.util.Date;
/**
 * 企业行业
 * @author sgs
 * @date 2017/7/23
 */
public class EnterpriseIndustry {

	private Integer id;

	private String code;

	private String name;

	private String creatorName;

	private Date createDate;

	private Integer pid;

	private Integer modelId0;

	private Integer modelId1;

	private Integer modelId2;

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
		this.code = code == null ? null : code.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName == null ? null : creatorName.trim();
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Integer getModelId0() {
		return modelId0;
	}

	public void setModelId0(Integer modelId0) {
		this.modelId0 = modelId0;
	}

	public Integer getModelId1() {
		return modelId1;
	}

	public void setModelId1(Integer modelId1) {
		this.modelId1 = modelId1;
	}

	public Integer getModelId2() {
		return modelId2;
	}

	public void setModelId2(Integer modelId2) {
		this.modelId2 = modelId2;
	}
}
