package com.ccx.index.model;

import com.ccx.util.DateUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class AbsIndexFormula {
    private Integer id;

    private String formulaId;

    private String formulaName;

    private String formulaContent;

    private String formulaHtmlContent;

    private String formulaContent1;

    private String formulaHtmlContent1;

    private String formulaContent2;

    private String formulaHtmlContent2;

    private String formulaContent3;

    private String formulaHtmlContent3;

    private Integer year;

    private Integer parentId;

    private Integer parentFlag;

    private String creator;

    private Date createTime;

    private String createTimeStr;

    private Integer yearLen;

    private Integer reportTypeId;

    private List<AbsIndexFormula> formulaList;

    private Integer currentFlag;

    private Integer lastVersionId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFormulaId() {
        return formulaId;
    }

    public void setFormulaId(String formulaId) {
        this.formulaId = formulaId == null ? null : formulaId.trim();
    }

    public String getFormulaName() {
        return formulaName;
    }

    public void setFormulaName(String formulaName) {
        this.formulaName = formulaName == null ? null : formulaName.trim();
    }

    public String getFormulaContent() {
        return formulaContent;
    }

    public void setFormulaContent(String formulaContent) {
        this.formulaContent = formulaContent == null ? null : formulaContent;
    }

    public String getFormulaContent1() {
        return formulaContent1;
    }

    public void setFormulaContent1(String formulaContent1) {
        this.formulaContent1 = formulaContent1;
    }

    public String getFormulaContent2() {
        return formulaContent2;
    }

    public void setFormulaContent2(String formulaContent2) {
        this.formulaContent2 = formulaContent2;
    }

    public String getFormulaContent3() {
        return formulaContent3;
    }

    public void setFormulaContent3(String formulaContent3) {
        this.formulaContent3 = formulaContent3;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getParentFlag() {
        return parentFlag;
    }

    public void setParentFlag(Integer parentFlag) {
        this.parentFlag = parentFlag;
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

    public String getCreateTimeStr() {
        String str = "";
        try {
            str = null==createTime?"": DateUtils.formatDateStr(createTime);
        } catch (ParseException e) {
        }
        return str;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getYearLen() {
        return yearLen;
    }

    public void setYearLen(Integer yearLen) {
        this.yearLen = yearLen;
    }

    public List<AbsIndexFormula> getFormulaList() {
        return formulaList;
    }

    public void setFormulaList(List<AbsIndexFormula> formulaList) {
        this.formulaList = formulaList;
    }

    public String getFormulaHtmlContent() {
        return formulaHtmlContent;
    }

    public void setFormulaHtmlContent(String formulaHtmlContent) {
        this.formulaHtmlContent = formulaHtmlContent;
    }

    public String getFormulaHtmlContent1() {
        return formulaHtmlContent1;
    }

    public void setFormulaHtmlContent1(String formulaHtmlContent1) {
        this.formulaHtmlContent1 = formulaHtmlContent1;
    }

    public String getFormulaHtmlContent2() {
        return formulaHtmlContent2;
    }

    public void setFormulaHtmlContent2(String formulaHtmlContent2) {
        this.formulaHtmlContent2 = formulaHtmlContent2;
    }

    public String getFormulaHtmlContent3() {
        return formulaHtmlContent3;
    }

    public void setFormulaHtmlContent3(String formulaHtmlContent3) {
        this.formulaHtmlContent3 = formulaHtmlContent3;
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