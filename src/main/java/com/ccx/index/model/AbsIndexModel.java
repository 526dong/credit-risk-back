package com.ccx.index.model;

import com.ccx.util.DateUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class AbsIndexModel {
    private Integer id;

    private String code;

    private String idCode;

    private Integer type;

    private Integer industryId;

    private String name;

    private Integer state;
	
    private String creator;

    private Date createTime;

    private String createTimeStr;

    private String industryNameAndType;

    private Integer reportTypeId;

    private Integer currentFlag;

    private Integer lastVersionId;

    private List<AbsIndexModelRule>  ruleList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return  this.code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getIdCode() {
        return String.format("%04d", id);
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIndustryId() {
        return industryId;
    }

    public void setIndustryId(Integer industryId) {
        this.industryId = industryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeStr() {
        String time = null;
        try {
            time = DateUtils.formatDateStr(createTime);
        } catch (Exception e) {
            time =  "null";
        }
        return time;
    }

    public String getIndustryNameAndType() {
        return industryNameAndType;
    }

    public void setIndustryNameAndType(String industryNameAndType) {
        this.industryNameAndType = industryNameAndType;
    }

    public List<AbsIndexModelRule> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<AbsIndexModelRule> ruleList) {
        this.ruleList = ruleList;
    }

    public Integer getReportTypeId() {
        return reportTypeId;
    }

    public void setReportTypeId(Integer reportTypeId) {
        this.reportTypeId = reportTypeId;
    }

    public Integer getCurrentFlag() {
        return currentFlag;
    }

    public void setCurrentFlag(Integer currentFlag) {
        this.currentFlag = currentFlag;
    }

    public Integer getLastVersionId() {
        return lastVersionId;
    }

    public void setLastVersionId(Integer lastVersionId) {
        this.lastVersionId = lastVersionId;
    }
}