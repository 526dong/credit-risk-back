package com.ccx.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ccx.dictionary.model.RateInstitution;
import com.ccx.dictionary.model.RateResult;
import com.ccx.dictionary.service.RateInstitutionService;
import com.ccx.dictionary.service.RateResultService;

/**
 * 评级数据util
 * @author xzd
 * @date 2017/7/17
 */
public class RateDataUtil {
	/*评级机构map*/
	private static Map<String, Integer> mapRateInstitution = null; 
	
	/*评级结果map*/
	private static Map<String, Integer> mapRateResult = null; 
	
	/**
	 * 评级机构map
	 */
	public synchronized static Map<String, Integer> getRateInstitutionMap(RateInstitutionService rateInstitutionService){
		if (mapRateInstitution == null) {
			mapRateInstitution = new HashMap<>();
			
			List<RateInstitution> list = rateInstitutionService.getList();
			
			if (list != null && list.size()>0) {
				for (RateInstitution rateInstitution : list) {
					mapRateInstitution.put(rateInstitution.getName(), rateInstitution.getId());
				}
				
				return mapRateInstitution;
			}
		}
		return mapRateInstitution;
	}
	
	/**
	 * 清空评级机构map
	 */
	public synchronized static void deleteRateInstitutionMap(){
		mapRateInstitution = null;
	}
	
	/**
	 * 评级结果map
	 */
	public synchronized static Map<String, Integer> getRateResultMap(RateResultService rateResultService){
		if (mapRateResult == null) {
			mapRateResult = new HashMap<>();
			
			List<RateResult> list = rateResultService.getList();
			
			if (list != null && list.size()>0) {
				for (RateResult rateResult : list) {
					mapRateResult.put(rateResult.getName(), rateResult.getId());
				}
			}
			
			return mapRateResult;
		}
		return mapRateResult;
	}
	
	/**
	 * 清空评级结果map
	 */
	public synchronized static void deleteRateResultMap(){
		mapRateResult = null;
	}
	
}
