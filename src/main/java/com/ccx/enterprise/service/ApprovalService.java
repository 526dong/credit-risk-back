package com.ccx.enterprise.service;

import com.ccx.enterprise.model.EnterpriseIndustry;
import com.ccx.enterprise.model.HistoryApproval;
import com.ccx.enterprise.model.IndustryShow;
import com.ccx.util.page.Page;

import java.util.List;

/**
 * 评级申请
 * @author sgs
 * @date 2017/7/2
 */
public interface ApprovalService {
	//查询审批记录
	HistoryApproval selectByAppNo(String appNO);

	HistoryApproval selectById(Integer id);
}
