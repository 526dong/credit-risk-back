package com.ccx.custom.model;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import com.ccx.util.DateUtils;
import com.ccx.util.UsedUtil;

public class IPBean implements Serializable{
    /** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/
	private static final long serialVersionUID = 1L;

	private Integer id;

    private String ipAddress;

    private String createPlantform;

    private Integer state;

    private Integer ipUpperLimit;

    private Integer institutionId;

    private String creator;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress == null ? null : ipAddress.trim();
    }

    public String getCreatePlantform() {
        return createPlantform;
    }

    public void setCreatePlantform(String createPlantform) {
        this.createPlantform = createPlantform == null ? null : createPlantform.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getIpUpperLimit() {
        return ipUpperLimit;
    }

    public void setIpUpperLimit(Integer ipUpperLimit) {
        this.ipUpperLimit = ipUpperLimit;
    }

    public Integer getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Integer institutionId) {
        this.institutionId = institutionId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }
    
    public String getCreateTimeStr() throws ParseException {
        if (UsedUtil.isNotNull(createTime)) {
    		return DateUtils.formatDateStr(createTime);
		}else{
			return null;
		}
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}