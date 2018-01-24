package com.ccx.enterprise.service;

import java.util.List;
import java.util.Map;

import com.ccx.dictionary.model.EnterpriseNature;
import com.ccx.enterprise.model.Enterprise;
import com.ccx.enterprise.model.HistoryApproval;
import com.ccx.enterprise.model.RateResult;
import com.ccx.util.page.Page;

public interface EnterpriseService {
    Enterprise findById(Integer id);
    
    EnterpriseNature findNatureById(Integer id);

    List<EnterpriseNature> findAllNature();
    
    Page<Enterprise> findAll(Page<Enterprise> page);

    Page<Map<String, Object>> findAllEnt(Page<Map<String, Object>> page);

    Page<Map<String, Object>> findHistoryRate(Page<Map<String, Object>> page);
    
    Map<String, String> selectEnterpriseIndexAndRules(Integer id);
    
    List<RateResult> findByRatingApplyNum(String ratingApplyNum);	
}

