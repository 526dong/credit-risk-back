package com.ccx.reporttype.dao;

import com.ccx.enterprise.model.IndustryShow;
import com.ccx.reporttype.model.AbsEnterpriseReportType;
import com.ccx.reporttype.pojo.AbsReportType;
import com.ccx.util.page.Page;

import java.util.List;
import java.util.Map;

public interface AbsEnterpriseReportTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AbsEnterpriseReportType record);

    int insertSelective(AbsEnterpriseReportType record);

    AbsEnterpriseReportType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AbsEnterpriseReportType record);

    int updateByPrimaryKey(AbsEnterpriseReportType record);

    List<AbsReportType> getList(Page<AbsReportType> page);

    List<Map<String,Object>> getTypes(Integer typeId);


    List<Map<String,Object>> getTypesAndIns(Integer typeId);

    //分页
    List<IndustryShow> getPageList(Page<IndustryShow> page);

    //分页
    List<IndustryShow> getPageList2(Page<IndustryShow> page);

    //获取当前类型是否用过
    Map<String ,Object> selectCheckIfUse(Integer typeId);

    //删除类型,执行相关存储过程
    Map<String,Object>  deleteType(Integer typeId);

    /**
     * @Description: 校验新增报表类型时的类型名称唯一性
     * @Author: wxn
     * @Date: 2018/1/18 15:21:13
     * @Param:
     * @Return
     */
    List<Map<String,Object>> checkFileName(Map<String,Object> map);

}