package com.ccx.custom.model;

import java.io.Serializable;
import java.util.Date;

import com.ccx.util.DateUtils;

/**
 * 
* @ClassName: RoleBg 
* @Description: 前台角色表(这里用一句话描述这个类的作用) 
* @author WXN 
* @date 2017年7月13日 上午9:59:53 
*
 */
public class RoleFg implements Serializable{
    /** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/
	private static final long serialVersionUID = 1L;

	private Long id;

    private String name;

    private String description;

    private Integer status;

    private Integer companyId;

    private String creater;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater == null ? null : creater.trim();
    }

    public String getCreateTime() throws Exception {
		if (createTime != null) {
     		return DateUtils.formatDateStr(createTime);
 		}else{
 			return null;
 		}
	}

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + ", description=" + description + ", status=" + status
				+ ", companyId=" + companyId + ", creater=" + creater + ", createTime=" + createTime + "]";
	}
}