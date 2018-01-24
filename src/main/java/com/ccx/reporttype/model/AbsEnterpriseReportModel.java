package com.ccx.reporttype.model;

public class AbsEnterpriseReportModel {
    private Integer id;

    private Integer reportType;

    private Integer reportSonType;

    private String financialSubject;

    private String columnId;

    private String columnExcel;

    private Integer required;

    private Integer reportSonNo;

    private Integer orderNo;

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

    public Integer getReportSonType() {
        return reportSonType;
    }

    public void setReportSonType(Integer reportSonType) {
        this.reportSonType = reportSonType;
    }

    public String getFinancialSubject() {
        return financialSubject;
    }

    public void setFinancialSubject(String financialSubject) {
        this.financialSubject = financialSubject == null ? null : financialSubject.trim();
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId == null ? null : columnId.trim();
    }

    public String getColumnExcel() {
        return columnExcel;
    }

    public void setColumnExcel(String columnExcel) {
        this.columnExcel = columnExcel == null ? null : columnExcel.trim();
    }

    public Integer getRequired() {
        return required;
    }

    public void setRequired(Integer required) {
        this.required = required;
    }

    public Integer getReportSonNo() {
        return reportSonNo;
    }

    public void setReportSonNo(Integer reportSonNo) {
        this.reportSonNo = reportSonNo;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }
}