package com.ccx.dictionary.model;

import java.util.Date;

import com.ccx.util.DateUtils;

public class RateResult {
    private Integer id;

    private String name;

    private String creatorName;

    private Date createTime;

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
}