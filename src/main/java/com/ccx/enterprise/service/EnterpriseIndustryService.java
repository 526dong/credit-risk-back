package com.ccx.enterprise.service;

import java.util.List;

import com.ccx.enterprise.model.EnterpriseIndustry;
import com.ccx.enterprise.model.IndustryShow;
import com.ccx.util.page.Page;

/**
 * 企业行业
 * @author sgs
 * @date 2017/7/2
 */
public interface EnterpriseIndustryService {
	void insert(EnterpriseIndustry industry);

	void deleteById(Integer id);

	void update(EnterpriseIndustry industry);

	void batchUpdate(List<EnterpriseIndustry> industryList);

	EnterpriseIndustry findIndustryById(Integer id);

	List<EnterpriseIndustry> findAllIndustryByPid(Integer id);

	Page<IndustryShow> findAllIndustry(Page<IndustryShow> page);

	//分页带有企业规模
	Page<IndustryShow> getPageList(Page<IndustryShow> page);

	//通过二级行业ids和modelId查一二级行业
	List<IndustryShow> getOneTwoListByIdsAndModelId(List<Integer> ids, Integer modelId);

	//通过modelId查二级行业id
	List<EnterpriseIndustry> getListByModelId(Integer modelId);

	//通过modelId查行业
	List<EnterpriseIndustry> getListByModelIdAndType(Integer modelId, Integer type);
	
	//通过企业id和企业规模查评分卡id
	Integer getModelIdByIdAndEntType(int industry2Id, int entType);
}
