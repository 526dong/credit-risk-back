package com.ccx.enterprise.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccx.enterprise.dao.RegionDao;
import com.ccx.enterprise.model.Region;
import com.ccx.enterprise.model.RegionList;
import com.ccx.enterprise.service.RegionService;
import com.ccx.util.page.Page;

/**
 * @author sgs
 * @data   2017年7月3日 
 */
@Service
public class RegionServiceImpl implements RegionService{
	@Autowired
	RegionDao dao ;

	@Override
	public void inset(Region region) {
		// TODO Auto-generated method stub
		dao.inset(region);
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		dao.deleteById(id);
	}

	@Override
	public void update(Region region) {
		// TODO Auto-generated method stub
		dao.update(region);
	}
	
	@Override
	public Region findRegionById(Integer id) {
		// TODO Auto-generated method stub
		return dao.findRegionById(id);
	}
	
	@Override
	public List<Region> findAllRegionByPid(Integer id) {
		// TODO Auto-generated method stub
		return dao.findAllRegionByPid(id);
	}

	@Override
	public Page<RegionList> findAllRegion(Page<RegionList> page) {
		// TODO Auto-generated method stub
		page.setRows(dao.findAllRegion(page));
		return page;
	}

}
