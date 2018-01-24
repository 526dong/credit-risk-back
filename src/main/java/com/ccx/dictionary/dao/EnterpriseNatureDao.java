package com.ccx.dictionary.dao;

import java.util.List;

import com.ccx.dictionary.model.EnterpriseNature;
import com.ccx.util.page.Page;

/**
 * 企业性质
 * @author xzd
 * @data   2017/7/5 
 */
public interface EnterpriseNatureDao {
    void insert(EnterpriseNature nature);
	
    void deleteById(Integer id);
    
    void update(EnterpriseNature nature);
    
    List<EnterpriseNature> findAll(Page<EnterpriseNature> page);
    
    List<EnterpriseNature> findAllNature();
    
    EnterpriseNature findById(Integer id);

    int findByName(String name);
}