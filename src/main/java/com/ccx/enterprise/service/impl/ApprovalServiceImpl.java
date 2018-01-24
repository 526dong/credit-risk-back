package com.ccx.enterprise.service.impl;

import com.ccx.enterprise.dao.ApprovalDao;
import com.ccx.enterprise.dao.EnterpriseIndustryDao;
import com.ccx.enterprise.model.EnterpriseIndustry;
import com.ccx.enterprise.model.HistoryApproval;
import com.ccx.enterprise.model.IndustryShow;
import com.ccx.enterprise.service.ApprovalService;
import com.ccx.enterprise.service.EnterpriseIndustryService;
import com.ccx.util.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sgs
 * @data   2017年7月3日 
 */
@Service
public class ApprovalServiceImpl implements ApprovalService{
	@Autowired
	ApprovalDao dao ;

	@Override
	public HistoryApproval selectByAppNo(String appNO) {
		return dao.selectByAppNo(appNO);
	}

	@Override
	public HistoryApproval selectById(Integer id) {
		return dao.selectById(id);
	}

}
