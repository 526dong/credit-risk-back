package com.ccx.hierarchy.model;

import java.util.Date;

import com.ccx.util.DateUtils;

public class InsdustryBean {
    private int id;

    private String insdustryCode;

    private String insdustryName;

    private int isDel;

    private String creatorName;

    private Date createTime;
    
    private int matchFirstInsdustryId;
    
    private String matchFirstInsdustryName;
    
    private int matchSecondInsdustryId;
    
    private String matchSecondInsdustryName;
    
    private int checkedFlag;
    
    

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getInsdustryCode() {
		return insdustryCode;
	}

	public void setInsdustryCode(String insdustryCode) {
		this.insdustryCode = insdustryCode;
	}

	public String getInsdustryName() {
		return insdustryName;
	}

	public void setInsdustryName(String insdustryName) {
		this.insdustryName = insdustryName;
	}

	public int getIsDel() {
		return isDel;
	}

	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Date getCreateTime() {
		return createTime;
	}
	
	public String getCreateTimeStr() throws Exception {
		if (createTime != null) {
     		return DateUtils.formatDateStr(createTime);
 		}else{
 			return null;
 		}
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getMatchFirstInsdustryId() {
		return matchFirstInsdustryId;
	}

	public void setMatchFirstInsdustryId(int matchFirstInsdustryId) {
		this.matchFirstInsdustryId = matchFirstInsdustryId;
	}

	public String getMatchFirstInsdustryName() {
		return matchFirstInsdustryName;
	}

	public void setMatchFirstInsdustryName(String matchFirstInsdustryName) {
		this.matchFirstInsdustryName = matchFirstInsdustryName;
	}

	public int getMatchSecondInsdustryId() {
		return matchSecondInsdustryId;
	}

	public void setMatchSecondInsdustryId(int matchSecondInsdustryId) {
		this.matchSecondInsdustryId = matchSecondInsdustryId;
	}

	public String getMatchSecondInsdustryName() {
		return matchSecondInsdustryName;
	}

	public void setMatchSecondInsdustryName(String matchSecondInsdustryName) {
		this.matchSecondInsdustryName = matchSecondInsdustryName;
	}

	public int getCheckedFlag() {
		return checkedFlag;
	}

	public void setCheckedFlag(int checkedFlag) {
		this.checkedFlag = checkedFlag;
	}

	

	

   
}