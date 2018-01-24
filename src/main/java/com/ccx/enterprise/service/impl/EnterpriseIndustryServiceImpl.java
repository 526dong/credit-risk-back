package com.ccx.enterprise.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccx.enterprise.dao.EnterpriseIndustryDao;
import com.ccx.enterprise.model.EnterpriseIndustry;
import com.ccx.enterprise.model.IndustryShow;
import com.ccx.enterprise.service.EnterpriseIndustryService;
import com.ccx.util.page.Page;

/**
 * @author sgs
 * @data   2017年7月3日 
 */
@Service
public class EnterpriseIndustryServiceImpl implements EnterpriseIndustryService{
	@Autowired
	EnterpriseIndustryDao dao ;

	@Override
	public void insert(EnterpriseIndustry industry) {
		dao.insert(industry);
	}

	@Override
	public void deleteById(Integer id) {
		dao.deleteByPrimaryKey(id);
	}

	@Override
	public void update(EnterpriseIndustry industry) {
		dao.updateByPrimaryKey(industry);
	}

	@Override
	public void batchUpdate(List<EnterpriseIndustry> industryList) {
		dao.batchUpdate(industryList);
	}

	@Override
	public EnterpriseIndustry findIndustryById(Integer id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<EnterpriseIndustry> findAllIndustryByPid(Integer id) {
		return dao.findAllIndustryByPid(id);
	}

	@Override
	public Page<IndustryShow> findAllIndustry(Page<IndustryShow> page) {
		page.setRows(dao.findAllIndustry(page));
		return page;
	}

	//分页
	@Override
	public Page<IndustryShow> getPageList(Page<IndustryShow> page) {
		page.setRows(dao.getPageList(page));
		return page;
	}

	//通过二级行业ids和modelId查一二级行业
	@Override
	public List<IndustryShow> getOneTwoListByIdsAndModelId(List<Integer> ids, Integer modelId) {
		return dao.getOneTwoListByIdsAndModelId(ids, modelId);
	}

	//通过modelId查二级行业id
    @Override
    public List<EnterpriseIndustry> getListByModelId(Integer modelId) {
        return dao.getListByModelId(modelId);
    }

    //通过modelId查行业
	@Override
	public List<EnterpriseIndustry> getListByModelIdAndType(Integer modelId, Integer type) {
		return dao.getListByModelIdAndType(modelId, type);
	}
	
	//通过企业id和企业规模查评分卡id
	@Override
	public Integer getModelIdByIdAndEntType(int industry2Id, int entType) {
		return dao.getModelIdByIdAndEntType(industry2Id, entType);
	}

}
