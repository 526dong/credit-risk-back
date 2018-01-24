package com.ccx.enterprise.model;

import java.util.Date;

import com.ccx.index.model.AbsIndexModel;
import com.ccx.util.DateUtils;
/**
 * 企业行业-后台系统列表
 * @author xzd
 * @date 2017/7/6
 */
public class IndustryShow{
	public IndustryShow() {
	}

	public IndustryShow(Integer id2, String name1, String name2, Integer modelId0, Integer modelId1, Integer type) {
		this.id2 = id2;
		this.name1 = name1;
		this.name2 = name2;
		this.modelId0 = modelId0;
		this.modelId1 = modelId1;
		this.type = type;
	}

	private Integer id;
	private Integer id1;
	private Integer id2;

	private String code1;//行业编号：一级行业
	private String name1;//行业名称：一级行业

	private String code2;//行业编号：二级行业
	private String name2;//行业名称：二级行业

	private String creatorName;//创建人姓名
	private Date createDate;//创建时间

	private Integer pid;//父节点id

	private String backName;//对应后台行业名称

	private Integer modelId0;
	private Integer modelId1;
	private Integer modelId2;

	private Integer type;

	private String modelName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId1() {
		return id1;
	}

	public void setId1(Integer id1) {
		this.id1 = id1;
	}

	public Integer getId2() {
		return id2;
	}

	public void setId2(Integer id2) {
		this.id2 = id2;
	}

	public String getCode1() {
		return code1;
	}

	public void setCode1(String code1) {
		this.code1 = code1;
	}

	public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	public String getCode2() {
		return code2;
	}

	public void setCode2(String code2) {
		this.code2 = code2;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
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

	public String getBackName() {
		return backName;
	}

	public void setBackName(String backName) {
		this.backName = backName;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
}






