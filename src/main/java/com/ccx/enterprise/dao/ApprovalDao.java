package com.ccx.enterprise.dao;

import com.ccx.dictionary.model.EnterpriseNature;
import com.ccx.enterprise.model.Enterprise;
import com.ccx.enterprise.model.HistoryApproval;
import com.ccx.enterprise.model.RateResult;
import com.ccx.util.page.Page;

import java.util.List;
import java.util.Map;

public interface ApprovalDao {
    //查询审批记录
    HistoryApproval selectByAppNo(String appNO);

    HistoryApproval selectById(Integer id);
}