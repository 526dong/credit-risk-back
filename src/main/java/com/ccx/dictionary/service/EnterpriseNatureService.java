package com.ccx.dictionary.service;

import java.util.List;

import com.ccx.dictionary.model.EnterpriseNature;
import com.ccx.util.page.Page;

/**
 * 企业性质
 * @author xzd
 * @date 2017/7/5
 */
public interface EnterpriseNatureService {
	void insert(EnterpriseNature nature);
	
    void deleteById(Integer id);
    
    void update(EnterpriseNature nature);
    
    Page<EnterpriseNature> findAll(Page<EnterpriseNature> page);
    
    List<EnterpriseNature> findAllNature();
    
    EnterpriseNature findById(Integer id);

    int findByName(String name);
}
