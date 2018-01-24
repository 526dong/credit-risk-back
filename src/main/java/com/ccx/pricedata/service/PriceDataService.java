package com.ccx.pricedata.service;	
	
import com.ccx.pricedata.model.PriceData;
import com.ccx.util.page.Page;	
	
public interface PriceDataService {	
		
	//主键获取	
	PriceData getById(Integer id);	
		
	//获取带分页list	
	Page<PriceData> getPageList(Page<PriceData> page);	
		
	//更新对象	
	int update(PriceData model);	
		
}	
