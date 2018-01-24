package com.ccx.ratedata.model;

import com.ccx.util.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 评级数据管理-含有hashcode
 * @author xzd
 * @date 2017/8/31
 */
public class RateDataHasHashCode implements Serializable{
	private Integer id;

	/*企业名称*/
    private String name;

    /*信用代码标识：0-统一信用代码，1-组织机构代码，2-事证号，3-其他*/
    private Integer creditCodeType;
    
    /*统一信用代码*/
    private String creditCode;

    /*组织机构代码*/
    private String organizationCode;

    /*事证号*/
    private String certificateCode;

    /*字典-评级机构id*/
    private Integer rateInstitution;
    
    /*字典-评级机构名称*/
    private String rateInstitutionName;

    /*未在字典中的评级机构名称*/
    private String rateInsNameOut;

    /*字典-评级结果id*/
    private Integer rateResult;
    
    /*字典-评级结果名称*/
    private String rateResultName;
    
    /*评级时间*/
    private String rateTime;

    /*创建人*/
    private String creatorName;

    /*创建时间*/
    private Date createTime;

    /*机构id*/
    private Integer institutionId;

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

    public Integer getCreditCodeType() {
		return creditCodeType;
	}

	public void setCreditCodeType(Integer creditCodeType) {
		this.creditCodeType = creditCodeType;
	}

	public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode == null ? null : creditCode.trim();
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public String getCertificateCode() {
        return certificateCode;
    }

    public void setCertificateCode(String certificateCode) {
        this.certificateCode = certificateCode;
    }

    public void setOrganizationCode(String organizationCode) {

        this.organizationCode = organizationCode;
    }

    public Integer getRateInstitution() {
        return rateInstitution;
    }

    public void setRateInstitution(Integer rateInstitution) {
        this.rateInstitution = rateInstitution;
    }
    
	public String getRateInstitutionName() {
		return rateInstitutionName;
	}

	public void setRateInstitutionName(String rateInstitutionName) {
		this.rateInstitutionName = rateInstitutionName;
	}

    public String getRateInsNameOut() {
        return rateInsNameOut;
    }

    public void setRateInsNameOut(String rateInsNameOut) {
        this.rateInsNameOut = rateInsNameOut;
    }

	public Integer getRateResult() {
        return rateResult;
    }

    public void setRateResult(Integer rateResult) {
        this.rateResult = rateResult;
    }
    
	public String getRateResultName() {
		return rateResultName;
	}

	public void setRateResultName(String rateResultName) {
		this.rateResultName = rateResultName;
	}

    /*public String getRateTime() throws Exception {
        if (rateTime != null) {
            return DateUtils.formatDate(rateTime);
        }else{
            return null;
        }
    }*/

    public String getRateTime() {
        return rateTime;
    }

    public void setRateTime(String rateTime) {
        this.rateTime = rateTime;
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

    public Integer getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Integer institutionId) {
        this.institutionId = institutionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RateDataHasHashCode rateData = (RateDataHasHashCode) o;

        if (!name.equals(rateData.name)) return false;
        if (!rateInstitutionName.equals(rateData.rateInstitutionName)) return false;
        if (!rateResultName.equals(rateData.rateResultName)) return false;
        return rateTime.equals(rateData.rateTime);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + rateInstitutionName.hashCode();
        result = 31 * result + rateResultName.hashCode();
        result = 31 * result + rateTime.hashCode();
        return result;
    }
}