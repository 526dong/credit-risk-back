package com.ccx.reporttype.model;

public class AbsEnterpriseReportSheet {
    private Integer id;

    private String name;

    private String columnId;

    private Integer reportType;

    private Integer reportSonNo;

    private String columnsFirstName;

    private Integer sheetOrder;

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

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId == null ? null : columnId.trim();
    }

    public Integer getReportType() {
        return reportType;
    }

    public void setReportType(Integer reportType) {
        this.reportType = reportType;
    }

    public Integer getReportSonNo() {
        return reportSonNo;
    }

    public void setReportSonNo(Integer reportSonNo) {
        this.reportSonNo = reportSonNo;
    }

    public String getColumnsFirstName() {
        return columnsFirstName;
    }

    public void setColumnsFirstName(String columnsFirstName) {
        this.columnsFirstName = columnsFirstName == null ? null : columnsFirstName.trim();
    }

    public Integer getSheetOrder() {
        return sheetOrder;
    }

    public void setSheetOrder(Integer sheetOrder) {
        this.sheetOrder = sheetOrder;
    }
}