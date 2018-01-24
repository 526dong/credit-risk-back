package com.ccx.reporttype.service.impl;

import com.ccx.enterprise.model.IndustryShow;
import com.ccx.ratedata.controller.RateDataController;
import com.ccx.reporttype.dao.*;
import com.ccx.reporttype.model.*;
import com.ccx.reporttype.pojo.AbsReportType;
import com.ccx.reporttype.service.ReportTypeService;
import com.ccx.reporttype.service.TypeUpService;
import com.ccx.system.model.UserBg;
import com.ccx.util.ControllerUtil;
import com.ccx.util.ImportExecl;
import com.ccx.util.JsonUtils;
import com.ccx.util.page.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Description:
 * @author:lilong
 * @Date: 2017/9/29
 */
@Service
public class TypeUpServiceImpl implements TypeUpService {
    private  Logger logger = LogManager.getLogger(RateDataController.class);

    @Autowired
    private AbsEnterpriseReportTypeMapper absEnterpriseReportTypeMapper;

/*    @Autowired
    private AbsEnterpriseReportSheetMapper absEnterpriseReportSheetMapper;

    @Autowired
    private AbsEnterpriseReportModelMapper absEnterpriseReportModelMapper;

    @Autowired
    private AbsEnterpriseReportCloumnsFormulaMapper absEnterpriseReportCloumnsFormulaMapper;

    @Autowired
    private AbsEnterpriseIndustryTypeMapper absEnterpriseIndustryTypeMapper;*/


    @Override
    public Integer updateStatus(Integer newTypeId,Integer oldTypeId) {
        Integer flag=0;
        AbsEnterpriseReportType type=absEnterpriseReportTypeMapper.selectByPrimaryKey(oldTypeId);
        AbsEnterpriseReportType n=new AbsEnterpriseReportType();
        n.setId(newTypeId);
        n.setLastVersionId(oldTypeId);
        n.setVersion(type.getVersion()+1);
        absEnterpriseReportTypeMapper.updateByPrimaryKeySelective(n);
        AbsEnterpriseReportType o=new AbsEnterpriseReportType();
        o.setId(oldTypeId);
        o.setStatus(1);
        absEnterpriseReportTypeMapper.updateByPrimaryKeySelective(o);
        //修改模板类型对应行业的历史版本
        absEnterpriseReportTypeMapper.updateTypeAndIndusty(oldTypeId);
        return 1;
    }
}
