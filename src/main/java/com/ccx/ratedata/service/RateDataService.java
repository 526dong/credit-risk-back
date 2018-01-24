package com.ccx.ratedata.service;	
	
import java.util.List;
import java.util.Map;

import com.ccx.ratedata.model.RateData;
import com.ccx.util.page.Page;	

/**
 * 评级数据管理
 * @author xzd
 * @date 2017/7/13
 */
public interface RateDataService {	
		
	//主键获取	
	RateData getById(Integer id);

	//企业名称获取
	int getByName(String name);

	//校验唯一-统一信用代码
	int findByCreditCode(String creditCode);

	//校验唯一-组织机构代码
	int findByOrganizationCode(String organizationCode);

	//校验唯一-事证号
	int findByCertificateCode(String certificateCode);
		
	//获取无参list	
	List<RateData> getList();	
		
	//获取有参数list	
	List<RateData> getList(RateData model);	
		
	//获取带分页list	
	Page<RateData> getPageList(Page<RateData> page);	
		
	//通过条件获取	
	RateData getByModel(RateData model);	
	
	//保存对象	
	int save(RateData model);	
	
	//批量-保存对象	
	int batchInsert(List<RateData> list);
	
	//更新对象	
	int update(RateData model);	
		
	//删除对象	
	int deleteById(Integer id);	
		
	//其他查询	
	Map<String, Object> getOther();	
}	
