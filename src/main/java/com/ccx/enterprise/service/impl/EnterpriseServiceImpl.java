package com.ccx.enterprise.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccx.dictionary.model.EnterpriseNature;
import com.ccx.enterprise.dao.EnterpriseDao;
import com.ccx.enterprise.model.Enterprise;
import com.ccx.enterprise.model.HistoryApproval;
import com.ccx.enterprise.model.RateResult;
import com.ccx.enterprise.service.EnterpriseService;
import com.ccx.util.page.Page;

/**
 * @author sgs
 * @data   2017年7月3日 
 */
@Service	
public class EnterpriseServiceImpl implements EnterpriseService{
	@Autowired
	EnterpriseDao dao ;

	@Override
	public Enterprise findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public Page<Enterprise> findAll(Page<Enterprise> page) {
		page.setRows(dao.findAll(page));
		return page;
	}

	@Override
	public Page<Map<String, Object>> findAllEnt(Page<Map<String, Object>> page) {
		page.setRows(dao.findAllEnt(page));
		return page;
	}

	@Override
	public Page<Map<String, Object>> findHistoryRate(Page<Map<String, Object>> page) {
		page.setRows(dao.findHistoryRate(page));
		return page;
	}
	
	@Override
	public Map<String, String> selectEnterpriseIndexAndRules(Integer id) {
		return dao.selectEnterpriseIndexAndRules(id);
	}

	@Override
	public EnterpriseNature findNatureById(Integer id) {
		return dao.findNatureById(id);
	}

	@Override
	public List<EnterpriseNature> findAllNature() {
		return dao.findAllNature();
	}

	@Override
	public List<RateResult> findByRatingApplyNum(String ratingApplyNum) {
		return dao.findByRatingApplyNum(ratingApplyNum);
	}

}
