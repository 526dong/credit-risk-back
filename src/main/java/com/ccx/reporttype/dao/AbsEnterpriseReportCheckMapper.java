package com.ccx.reporttype.dao;

import com.ccx.reporttype.model.AbsEnterpriseReportCheck;

import java.util.List;

public interface AbsEnterpriseReportCheckMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AbsEnterpriseReportCheck record);

    int insertSelective(AbsEnterpriseReportCheck record);

    AbsEnterpriseReportCheck selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AbsEnterpriseReportCheck record);

    int updateByPrimaryKey(AbsEnterpriseReportCheck record);

    //根据对象查询
    List<AbsEnterpriseReportCheck> selectByModel(AbsEnterpriseReportCheck model);

    //批量插入
    void insertList(List<AbsEnterpriseReportCheck> reportCheckList);

    //删除类型的所有规则
    void deleteByTypeId(Integer typeId);
}