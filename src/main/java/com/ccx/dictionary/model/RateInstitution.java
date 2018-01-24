package com.ccx.dictionary.model;

import java.util.Date;

import com.ccx.util.DateUtils;

/**
 * 评级机构-字典
 * @author xzd
 * @date 2017/7/13
 */
public class RateInstitution {
    private Integer id;

    /*机构名称*/
    private String name;

    /*统一信用代码*/
    private String code;

    /*创建人*/
    private String creatorName;

    /*创建时间*/
    private Date createTime;

    /*是否删除：0-否，1-是。默认0*/
    private Integer deleteFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName == null ? null : creatorName.trim();
    }

    public String getCreateTime() throws Exception {
    	if (createTime != null) {
     		return DateUtils.formatDate(createTime);
 		}else{
 			return null;
 		}
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}