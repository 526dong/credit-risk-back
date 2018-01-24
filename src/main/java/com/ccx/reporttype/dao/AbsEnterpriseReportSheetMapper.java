package com.ccx.reporttype.dao;

import com.ccx.reporttype.model.AbsEnterpriseReportSheet;

public interface AbsEnterpriseReportSheetMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AbsEnterpriseReportSheet record);

    int insertSelective(AbsEnterpriseReportSheet record);

    AbsEnterpriseReportSheet selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AbsEnterpriseReportSheet record);

    int updateByPrimaryKey(AbsEnterpriseReportSheet record);
}