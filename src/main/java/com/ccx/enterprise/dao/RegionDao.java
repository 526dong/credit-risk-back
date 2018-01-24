package com.ccx.enterprise.dao;

import java.util.List;

import com.ccx.enterprise.model.Region;
import com.ccx.enterprise.model.RegionList;
import com.ccx.util.page.Page;

/**
 * 初始化地区-省市县
 * @author sgs
 * @date 2017/7/2
 */
public interface RegionDao {
	void inset(Region region);
	
	void deleteById(Integer id);
	
	void update(Region region);
	
	List<Region> findAllRegionByPid(Integer id);
	
	Region findRegionById(Integer id);
	
	List<RegionList> findAllRegion(Page<RegionList> page);
	
}
