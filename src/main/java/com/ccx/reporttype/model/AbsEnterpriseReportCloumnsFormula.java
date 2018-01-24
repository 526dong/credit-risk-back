package com.ccx.reporttype.model;

public class AbsEnterpriseReportCloumnsFormula {
    private Integer id;

    private Integer modelId;

    private Integer reportType;

    private Integer reportSonType;

    private String beginFormula;

    private String endFormula;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public Integer getReportType() {
        return reportType;
    }

    public void setReportType(Integer reportType) {
        this.reportType = reportType;
    }

    public Integer getReportSonType() {
        return reportSonType;
    }

    public void setReportSonType(Integer reportSonType) {
        this.reportSonType = reportSonType;
    }

    public String getBeginFormula() {
        return beginFormula;
    }

    public void setBeginFormula(String beginFormula) {
        this.beginFormula = beginFormula == null ? null : beginFormula.trim();
    }

    public String getEndFormula() {
        return endFormula;
    }

    public void setEndFormula(String endFormula) {
        this.endFormula = endFormula == null ? null : endFormula.trim();
    }
}