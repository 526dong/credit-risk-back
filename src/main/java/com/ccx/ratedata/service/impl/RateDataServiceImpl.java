package com.ccx.ratedata.service.impl;	
	
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccx.ratedata.dao.RateDataMapper;
import com.ccx.ratedata.model.RateData;
import com.ccx.ratedata.service.RateDataService;
import com.ccx.util.page.Page;	
	
/**
 * 评级数据管理
 * @author xzd
 * @date 2017/7/13
 */
@Service	
public class RateDataServiceImpl implements RateDataService {	
		
	@Autowired	
    private RateDataMapper rateDataMapper;	
		
	//主键获取	
	@Override	
	public RateData getById(Integer id) {	
		return rateDataMapper.selectByPrimaryKey(id);	
	}

	@Override
	public int getByName(String name) {
		return rateDataMapper.findByName(name);
	}

	@Override
	public int findByCreditCode(String creditCode) {
		return rateDataMapper.findByCreditCode(creditCode);
	}

	@Override
	public int findByOrganizationCode(String organizationCode) {
		return rateDataMapper.findByOrganizationCode(organizationCode);
	}

	@Override
	public int findByCertificateCode(String certificateCode) {
		return rateDataMapper.findByCertificateCode(certificateCode);
	}

	//获取无参list	
	@Override	
	public List<RateData> getList() {	
		return rateDataMapper.findAllRateData();
	}	
		
	//获取有参数list	
	@Override	
	public List<RateData> getList(RateData model) {	
		return null;	
	}	
		
	//获取带分页list	
	@Override	
	public Page<RateData> getPageList(Page<RateData> page) {
		page.setRows(rateDataMapper.findAll(page));
		return page;	
	}	
		
	//通过条件获取	
	@Override	
	public RateData getByModel(RateData model) {	
		return null;	
	}	
	
	//保存对象	
	@Override	
	public int save(RateData model) {	
		return rateDataMapper.insert(model);	
	}	
	
	//批量-保存对象	
	@Override
	public int batchInsert(List<RateData> list) {
		return rateDataMapper.batchInsert(list);
	}
	
	//更新对象	
	@Override	
	public int update(RateData model) {	
		return rateDataMapper.updateByPrimaryKey(model);	
	}	
		
	//删除对象	
	@Override	
	public int deleteById(Integer id) {	
		return rateDataMapper.deleteByPrimaryKey(id);	
	}	
		
	//其他查询	
	@Override	
	public Map<String, Object> getOther() {	
		return null;	
	}

}	
