package com.ccx.reporttype.dao;

import com.ccx.reporttype.model.AbsEnterpriseReportModel;

public interface AbsEnterpriseReportModelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AbsEnterpriseReportModel record);

    int insertSelective(AbsEnterpriseReportModel record);

    AbsEnterpriseReportModel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AbsEnterpriseReportModel record);

    int updateByPrimaryKey(AbsEnterpriseReportModel record);
}