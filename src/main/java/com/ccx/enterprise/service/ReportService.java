package com.ccx.enterprise.service;

import java.util.List;

import com.ccx.enterprise.model.Report;

/**
 * @author sgs
 * @data   2017年6月16日 
 */
public interface ReportService {
    
    Report findById(Integer id);

    List<Report> findByEnterpriseId(Integer enterpriseId);
}
