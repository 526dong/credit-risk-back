package com.ccx.enterprise.dao;

import java.util.List;

import com.ccx.enterprise.model.EnterpriseIndustry;
import com.ccx.enterprise.model.IndustryShow;
import com.ccx.util.page.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 企业行业
 * @author sgs
 * @date 2017/7/2
 */
public interface EnterpriseIndustryDao {
	int deleteByPrimaryKey(Integer id);

	int insert(EnterpriseIndustry record);

	int insertSelective(EnterpriseIndustry record);

	EnterpriseIndustry selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(EnterpriseIndustry record);

	int updateByPrimaryKey(EnterpriseIndustry record);

	void batchUpdate(List<EnterpriseIndustry> industryList);

	List<IndustryShow> findAllIndustry(Page<IndustryShow> page);

	List<EnterpriseIndustry> findAllIndustryByPid(Integer id);

	//分页
	List<IndustryShow> getPageList(Page<IndustryShow> page);

	//通过modelId查二级行业id
	List<EnterpriseIndustry> getListByModelId(Integer modelId);

	//通过二级行业ids和modelId查一二级行业
	List<IndustryShow> getOneTwoListByIdsAndModelId(@Param("list") List<Integer> ids, @Param("modelId") Integer modelId);

	//通过modelid和企业性质查二级行业名称
	List<EnterpriseIndustry> getListByModelIdAndType(@Param("modelId") Integer modelId, @Param("type") Integer type);

	//删除所有行业绑定的modelidBY企业性质
	void updateModelIdByType(@Param("modelId") Integer id , @Param("newModelId") Integer newId,  @Param("type") int type);
	
	//通过企业id和企业规模查评分卡id
	Integer getModelIdByIdAndEntType(@Param("industry2Id") int industry2Id, @Param("entType") int entType);

	//删除模型和企业关联
    void deleteModelRe0(Integer moId);

    void deleteModelRe1(Integer moId);
}
