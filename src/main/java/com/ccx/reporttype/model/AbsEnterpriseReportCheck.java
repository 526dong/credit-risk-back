package com.ccx.reporttype.model;

import java.util.Date;

public class AbsEnterpriseReportCheck {
    private Integer id;

    private Integer reportType;

    private String formula;

    private String formulaChinese;

    private Date createDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReportType() {
        return reportType;
    }

    public void setReportType(Integer reportType) {
        this.reportType = reportType;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula == null ? null : formula.trim();
    }

    public String getFormulaChinese() {
        return formulaChinese;
    }

    public void setFormulaChinese(String formulaChinese) {
        this.formulaChinese = formulaChinese == null ? null : formulaChinese.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}