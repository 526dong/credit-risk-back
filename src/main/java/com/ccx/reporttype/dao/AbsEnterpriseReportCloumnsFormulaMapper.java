package com.ccx.reporttype.dao;

import com.ccx.reporttype.model.AbsEnterpriseReportCloumnsFormula;

public interface AbsEnterpriseReportCloumnsFormulaMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AbsEnterpriseReportCloumnsFormula record);

    int insertSelective(AbsEnterpriseReportCloumnsFormula record);

    AbsEnterpriseReportCloumnsFormula selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AbsEnterpriseReportCloumnsFormula record);

    int updateByPrimaryKey(AbsEnterpriseReportCloumnsFormula record);
}