package com.ccx.enterprise.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ccx.enterprise.dao.ReportDao;
import com.ccx.enterprise.model.Report;
import com.ccx.enterprise.service.ReportService;

/**
 * @author sgs
 * @data   2017年7月3日 
 */
@Service
public class ReportServiceImpl implements ReportService{

	@Autowired
	ReportDao dao ;

	@Override
	public Report findById(Integer id) {
		return dao.findById(id);
	}
	
	@Override
	public List<Report> findByEnterpriseId(Integer enterpriseId) {
		return dao.findByEnterpriseId(enterpriseId);
	}

}
