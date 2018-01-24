package com.ccx.businessmng.dao;

import com.ccx.businessmng.model.AbsEnterpriseReportType;

import java.util.HashMap;
import java.util.List;

public interface EnterpriseReportTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AbsEnterpriseReportType record);

    int insertSelective(AbsEnterpriseReportType record);

    AbsEnterpriseReportType selectByPrimaryKey(Integer id);

    //通过主键获取名称
    String getNameById(Integer id);

    int updateByPrimaryKeySelective(AbsEnterpriseReportType record);

    int updateByPrimaryKey(AbsEnterpriseReportType record);

    //list无分页
    List<AbsEnterpriseReportType> getList(AbsEnterpriseReportType model);
}