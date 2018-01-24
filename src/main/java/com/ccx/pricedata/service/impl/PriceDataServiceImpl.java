package com.ccx.pricedata.service.impl;	
	
import com.ccx.util.EnterpriseDataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccx.pricedata.dao.PriceDataMapper;
import com.ccx.pricedata.model.PriceData;
import com.ccx.pricedata.service.PriceDataService;
import com.ccx.util.page.Page;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

@Service
/**
 * @Description 定价数据管理
 * @author Created by xzd on 2017/11/9.
 */
public class PriceDataServiceImpl implements PriceDataService {	
	@Autowired
    private PriceDataMapper priceDataMapper;
	/**
	 * 反射map get value
	 */
	public static Map<Integer, Object> priceDataGetMethod = new HashMap<>();
	/**
	 * 反射map set value
	 */
	public static Map<Integer, Object> priceDataSetMethod = new HashMap<>();

	/**
	 * 构造函数初始化加载反射会加快50倍
	 * @throws ClassNotFoundException
	 */
	public PriceDataServiceImpl() throws Exception {
		//reflect class
		Class<?> clazz = Class.forName("com.ccx.pricedata.model.PriceData");

		//price data map
		Map<Integer, String> priceDataMap = EnterpriseDataUtils.getPriceDataMap();

		//遍历map的正确打开方式
		Iterator it = priceDataMap.entrySet().iterator();

		while(it.hasNext()) {
			Map.Entry<Integer, String[]> map = (Map.Entry<Integer, String[]>) it.next();

			//get method
			Method getMethod = clazz.getDeclaredMethod("get" + map.getValue());
			priceDataGetMethod.put(map.getKey(), getMethod);
			//set method
			Method setMethod = clazz.getDeclaredMethod("set" + map.getValue(), String.class);
			priceDataSetMethod.put(map.getKey(), setMethod);
		}
	}

	//主键获取
	@Override	
	public PriceData getById(Integer id) {
		//定价数据
		PriceData priceData = new PriceData();

		try {
			//对定价数据中的利率等数据进行格式化处理
			priceData = dealPriceData(priceDataMapper.selectByPrimaryKey(id), false);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return priceData;

	}	
		
	//获取带分页list	
	@Override	
	public Page<PriceData> getPageList(Page<PriceData> page) {
		//list data
		List<PriceData> list = priceDataMapper.findAll(page);
		//deal list data
		List<PriceData> dataList = new ArrayList<>();

		try {
			for (PriceData price:list) {
				//对定价数据中的利率等数据进行格式化处理
				dataList.add(dealPriceData(price, false));
			}
			page.setRows(dataList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return page;
	}	
	
	//更新对象	
	@Override	
	public int update(PriceData model) {
		//定价数据
		PriceData priceData = new PriceData();

		try {
			//对定价数据中的利率等数据进行格式化处理
			priceData = dealPriceData(model, true);
			priceData.setCreatorName(model.getCreatorName());
			priceData.setCreateTime(new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return priceDataMapper.updateByPrimaryKey(priceData);
	}

	/**
	 * 对定价数据中的利率等数据进行格式化处理
	 * @author xzd
	 * @param priceData
	 * @return priceData
	 * @throws Exception reflect
	 */
	public PriceData dealPriceData(PriceData priceData, boolean isDoUpdate) throws Exception{
		//评级结果
		String rateResult = priceData.getRateResultName();
		if (rateResult.contains("C")) {
			//字符串类型
			return priceData;
		}
		//数字类型-进行小数格式化处理
		DecimalFormat df = new DecimalFormat("#.##");
		DecimalFormat updateFormat = new DecimalFormat("#.####");
		//price data map
		Map<Integer, String> priceDataMap = EnterpriseDataUtils.getPriceDataMap();
		try {
			for (int i = 0;i < priceDataMap.size();i++) {
                //get method
                Method getMethod = (Method) priceDataGetMethod.get(i);
                //set method
                Method setMethod = (Method) priceDataSetMethod.get(i);
                //deal get value to set value
                if (isDoUpdate) {
                    setMethod.invoke(priceData, updateFormat.format(new BigDecimal(Double.valueOf(String.valueOf(getMethod.invoke(priceData))) / 100)));
                } else {
                    setMethod.invoke(priceData, df.format(new BigDecimal(Double.valueOf(String.valueOf(getMethod.invoke(priceData))) * 100)));
                }
            }
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return priceData;
	}

}	
