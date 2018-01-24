package com.ccx.reporttype.dao;

import com.ccx.reporttype.model.AbsEnterpriseIndustryType;

public interface AbsEnterpriseIndustryTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AbsEnterpriseIndustryType record);

    int insertSelective(AbsEnterpriseIndustryType record);

    AbsEnterpriseIndustryType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AbsEnterpriseIndustryType record);

    int updateByPrimaryKey(AbsEnterpriseIndustryType record);

    int updateStatus(Integer industryId);
}