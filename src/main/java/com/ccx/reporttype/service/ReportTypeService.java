package com.ccx.reporttype.service;

import com.ccx.enterprise.model.IndustryShow;
import com.ccx.reporttype.model.AbsEnterpriseReportType;
import com.ccx.reporttype.pojo.AbsReportType;
import com.ccx.util.page.Page;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @author:lilong
 * @Date: 2017/9/29
 */
public interface ReportTypeService {

    Map<String, Object> saveOrUpdateType(MultipartFile excelFile, HttpServletRequest request,String name,Integer oldTypeId);

    Page<AbsReportType> getPageList(Page<AbsReportType> page);

    List<Map<String,Object>> getTypes(Integer typeId);

    //查询所有行业包含行业匹配的类型信息
    Page<IndustryShow> getPageList2(Page<IndustryShow> page);

    //查询类型信息
    AbsEnterpriseReportType getTypeByid(Integer typeId);

    //保存行业与类型的关系
    Map<String, Object> saveTypeAndIns(String ids,Integer typeId);

    //启用、禁用、删除
    Map<String, Object> updateStatus(Integer typeId,String status);

    /**
     * @Description: 校验新增报表类型时的类型名称唯一性
     * @Author: wxn
     * @Date: 2018/1/18 15:21:13
     * @Param:
     * @Return
     */
    String checkFileName(Map<String,Object> map);

}
