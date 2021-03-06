package com.ccx.enterprise.dao;

import java.util.List;
import java.util.Map;

import com.ccx.dictionary.model.EnterpriseNature;
import com.ccx.enterprise.model.Enterprise;
import com.ccx.enterprise.model.HistoryApproval;
import com.ccx.enterprise.model.RateResult;
import com.ccx.util.page.Page;

public interface EnterpriseDao {
    Enterprise findById(Integer id);
    
    EnterpriseNature findNatureById(Integer id);

    List<EnterpriseNature> findAllNature();
    
    List<Enterprise> findAll(Page<Enterprise> page);

    List<Map<String, Object>> findAllEnt(Page<Map<String, Object>> page);

    List<Map<String, Object>> findHistoryRate(Page<Map<String, Object>> page);
    
    //企业的指标id和规则id集合
    Map<String, String> selectEnterpriseIndexAndRules(Integer id);
    
    List<RateResult> findByRatingApplyNum(String ratingApplyNum);
}