package com.ccx.reporttype.service;

import com.ccx.enterprise.model.IndustryShow;
import com.ccx.reporttype.model.AbsEnterpriseReportType;
import com.ccx.reporttype.pojo.AbsReportType;
import com.ccx.util.page.Page;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @author:lilong
 * @Date: 2017/9/29
 */
public interface TypeUpService {

    Integer updateStatus(Integer newTypeId,Integer oldTypeId);
}
