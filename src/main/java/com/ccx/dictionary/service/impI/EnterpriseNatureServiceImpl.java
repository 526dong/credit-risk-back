package com.ccx.dictionary.service.impI;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccx.dictionary.dao.EnterpriseNatureDao;
import com.ccx.dictionary.model.EnterpriseNature;
import com.ccx.dictionary.service.EnterpriseNatureService;
import com.ccx.util.page.Page;

/**
 * 企业性质
 * @author xzd
 * @data   2017/7/5 
 */
@Service
public class EnterpriseNatureServiceImpl implements EnterpriseNatureService{
	@Autowired
	EnterpriseNatureDao dao ;

	@Override
	public void insert(EnterpriseNature nature) {
		dao.insert(nature);
	}

	@Override
	public void deleteById(Integer id) {
		dao.deleteById(id);
	}

	@Override
	public void update(EnterpriseNature nature) {
		dao.update(nature);
	}

	@Override
	public List<EnterpriseNature> findAllNature() {
		return dao.findAllNature();
	}
	
	@Override
	public Page<EnterpriseNature> findAll(Page<EnterpriseNature> page) {
		page.setRows(dao.findAll(page));
		return page;
	}

	@Override
	public EnterpriseNature findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public int findByName(String name) {
		return dao.findByName(name);
	}

}
