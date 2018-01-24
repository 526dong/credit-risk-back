package com.ccx.businessmng.service.impl;
	
import com.ccx.businessmng.model.AbsEnterpriseReportType;	
import com.ccx.businessmng.service.AbsEnterpriseReportTypeService;	
import com.ccx.businessmng.dao.EnterpriseReportTypeMapper;
import com.ccx.util.page.Page;	
import org.springframework.beans.factory.annotation.Autowired;	
import org.springframework.stereotype.Service;	
import java.util.*;	
	
@Service	
public class AbsEnterpriseReportTypeServiceImpl implements AbsEnterpriseReportTypeService {	
		
	@Autowired	
    private EnterpriseReportTypeMapper absEnterpriseReportTypeMapper;
		
	//主键获取	
	@Override	
	public AbsEnterpriseReportType getById(Integer id) {	
		return absEnterpriseReportTypeMapper.selectByPrimaryKey(id);	
	}

	@Override
	public String getNameById(Integer id) {
		return absEnterpriseReportTypeMapper.getNameById(id);
	}

	//获取无参list
	@Override	
	public List<AbsEnterpriseReportType> getList() {	
		return absEnterpriseReportTypeMapper.getList(null);
	}	
		
	//获取有参数list	
	@Override	
	public List<AbsEnterpriseReportType> getList(AbsEnterpriseReportType model) {	
		return null;	
	}	
		
	//获取带分页list	
	@Override	
	public Page<AbsEnterpriseReportType> getPageList(Page<AbsEnterpriseReportType> page) {	
		return null;	
	}	
		
	//通过条件获取	
	@Override	
	public AbsEnterpriseReportType getByModel(AbsEnterpriseReportType model) {	
		return null;	
	}	
	
	//保存对象	
	@Override	
	public int save(AbsEnterpriseReportType model) {	
		return absEnterpriseReportTypeMapper.insert(model);	
	}	
	
	//更新对象	
	@Override	
	public int update(AbsEnterpriseReportType model) {	
		return absEnterpriseReportTypeMapper.updateByPrimaryKey(model);	
	}	
		
	//删除对象	
	@Override	
	public int deleteById(Integer id) {	
		return absEnterpriseReportTypeMapper.deleteByPrimaryKey(id);	
	}	
		
	//其他查询	
	@Override	
	public Map<String, Object> getOther() {	
		return null;	
	}	
}	
