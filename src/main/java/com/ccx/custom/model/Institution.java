package com.ccx.custom.model;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import com.ccx.util.DateUtils;
import com.ccx.util.UsedUtil;

/**
 * 机构表
 */
public class Institution implements Serializable{
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String organizationNature;

    private Integer organizationCodeFlag;

    private String organizationCode;

    private String organizationScale;

    private String industryFirst;

    private String industrySecond;

    private String address;

    private String officeAddress;

    private Date foundtime;

    private String legalName;

    private String officePhoneAreacode;

    private String officePhone;

    private String email;

    private String fax;

    private String webSite;

    private String contactName;

    private String contactDep;

    private String contactJob;

    private String contactPhoneAreacode;

    private String contactPhone;

    private String contactPhoneNum;

    private String contactEmail;

    private String contactWechat;

    private String contactQQ;

    private String examineReason;

    private Integer examinestate;

    private Integer state;

    private String creater;

    private Date createtime;

    private String modifier;

    private Date modifytime;

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

    public String getOrganizationNature() {
        return organizationNature;
    }

    public void setOrganizationNature(String organizationNature) {
        this.organizationNature = organizationNature == null ? null : organizationNature.trim();
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode == null ? null : organizationCode.trim();
    }

    public String getOrganizationScale() {
        return organizationScale;
    }

    public void setOrganizationScale(String organizationScale) {
        this.organizationScale = organizationScale == null ? null : organizationScale.trim();
    }

    public String getIndustryFirst() {
        return industryFirst;
    }

    public void setIndustryFirst(String industryFirst) {
        this.industryFirst = industryFirst == null ? null : industryFirst.trim();
    }

    public String getIndustrySecond() {
        return industrySecond;
    }

    public void setIndustrySecond(String industrySecond) {
        this.industrySecond = industrySecond == null ? null : industrySecond.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress == null ? null : officeAddress.trim();
    }

    public String getFoundtime() throws ParseException {
        if (UsedUtil.isNotNull(foundtime)) {
    		return DateUtils.formatDateStr(foundtime);
		}else{
			return null;
		}
    }

    public void setFoundtime(Date foundtime) {
        this.foundtime = foundtime;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName == null ? null : legalName.trim();
    }

    public String getOfficePhoneAreacode() {
        return officePhoneAreacode;
    }

    public void setOfficePhoneAreacode(String officePhoneAreacode) {
        this.officePhoneAreacode = officePhoneAreacode == null ? null : officePhoneAreacode.trim();
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone == null ? null : officePhone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax == null ? null : fax.trim();
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite == null ? null : webSite.trim();
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName == null ? null : contactName.trim();
    }

    public String getContactDep() {
        return contactDep;
    }

    public void setContactDep(String contactDep) {
        this.contactDep = contactDep == null ? null : contactDep.trim();
    }

    public String getContactJob() {
        return contactJob;
    }

    public void setContactJob(String contactJob) {
        this.contactJob = contactJob == null ? null : contactJob.trim();
    }

    public String getContactPhoneAreacode() {
        return contactPhoneAreacode;
    }

    public void setContactPhoneAreacode(String contactPhoneAreacode) {
        this.contactPhoneAreacode = contactPhoneAreacode == null ? null : contactPhoneAreacode.trim();
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone == null ? null : contactPhone.trim();
    }

    public String getContactPhoneNum() {
        return contactPhoneNum;
    }

    public void setContactPhoneNum(String contactPhoneNum) {
        this.contactPhoneNum = contactPhoneNum == null ? null : contactPhoneNum.trim();
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail == null ? null : contactEmail.trim();
    }

    public String getContactWechat() {
        return contactWechat;
    }

    public void setContactWechat(String contactWechat) {
        this.contactWechat = contactWechat == null ? null : contactWechat.trim();
    }

    public String getContactQQ() {
        return contactQQ;
    }

    public void setContactQQ(String contactQQ) {
        this.contactQQ = contactQQ;
    }

    public String getExamineReason() {
        return examineReason;
    }

    public void setExamineReason(String examineReason) {
        this.examineReason = examineReason == null ? null : examineReason.trim();
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater == null ? null : creater.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }
    
    public String getCreatetimeStr() throws ParseException {
        if (UsedUtil.isNotNull(createtime)) {
    		return DateUtils.formatDateStr(createtime);
		}else{
			return null;
		}
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier == null ? null : modifier.trim();
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }

    public Integer getOrganizationCodeFlag() {
        return organizationCodeFlag;
    }

    public void setOrganizationCodeFlag(Integer organizationCodeFlag) {
        this.organizationCodeFlag = organizationCodeFlag;
    }

    public Integer getExaminestate() {
        return examinestate;
    }

    public void setExaminestate(Integer examinestate) {
        this.examinestate = examinestate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}