package com.ccx.enterprise.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import com.ccx.enterprise.model.Report;

/**
 * @author sgs
 * @data   2017年7月3日 
 */
public interface ReportDao {
    
    Report findById (Integer id);
    
    List<Report> findByEnterpriseId(Integer enterpriseId);
}