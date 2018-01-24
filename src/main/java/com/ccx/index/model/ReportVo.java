package com.ccx.index.model;

/**
 * Created by zhaotm on 2017/7/14.
 */
public class ReportVo {

    String name;

    String column;

    public ReportVo() {

    }

    public ReportVo(String name, String column) {
        this.name = name;
        this.column = column;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }
}
