package com.ccx.enterprise.model;

import com.ccx.util.DateUtils;
import com.ccx.util.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 企业报表概况
 * @author sgs
 * @date 2017/7/23
 */
public class Report implements Serializable{
	private Integer id;

	/*报表时间:主要指年份*/
	private String reportTime;
	
	/*口径*/
	private Integer cal;
	
	/*类型*/
	private Integer type;

	/*报表类型对应的报表名称*/
	private String reportName;

	/*周期*/
	private String cycle;

	/*是否审计：1-是，0-否*/
	private Integer audit;
	
	/*币种*/
	private String currency;
	
	/*审计单位*/
	private String auditUnit;
	
	/*审计意见*/
	private String auditOpinion;
	
	/*创建人姓名*/
	private String creatorName;
	
	/*创建时间*/
	private Date createDate;
	
	/*更新时间*/
	private Date updateDate;
	
	/*公司id*/
	private Integer companyId;

	/*是否删除标识：0-否，1删除。默认0*/
	private Integer deleteFlag;

	private Integer approvalStatus;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getReportTime() {
		return reportTime;
	}

	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}

	public Integer getCal() {
		return cal;
	}

	public void setCal(Integer cal) {
		this.cal = cal;
	}

	public Integer getType() {
		return type;
	}

	public Integer getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Integer approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getCycle() {
		return cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}

	public Integer getAudit() {
		return audit;
	}

	public void setAudit(Integer audit) {
		this.audit = audit;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAuditUnit() {
		return auditUnit;
	}

	public void setAuditUnit(String auditUnit) {
		this.auditUnit = auditUnit;
	}

	public String getAuditOpinion() {
		return auditOpinion;
	}

	public void setAuditOpinion(String auditOpinion) {
		this.auditOpinion = auditOpinion;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getCreateDate() throws Exception {
		if (!StringUtils.isEmpty(createDate)) {
			return DateUtils.formatDate(createDate);
		}else{
			return null;
		}
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

}
