package com.ccx.util;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @ClassName:  PropertiesUtil   
 * @Description:TODO(properties文件操作工具类)   
 * @author: ll
 * @date:   2017年7月24日 下午1:15:38
 */
public class PropertiesUtil {
	
	
	private static Map<String,ResourceBundle> pro_map=new ConcurrentHashMap<>();

	/**
	 * 获取指定配置文件的指定key值
	 * 
	 * @param fileName
	 *            配置文件名称
	 * @param key
	 * @return
	 * @author ll
	 */
	public static String getData(String fileName, String key) {
		if( fileName.indexOf(".")>0){
			fileName=fileName.split("\\.")[0];
		}
		ResourceBundle resource = null;
		if (pro_map.get(fileName) == null) {
			synchronized (fileName) {
				if (pro_map.get(fileName) == null) {
					resource = ResourceBundle.getBundle(fileName);
				}
			}
		} else {
			resource = pro_map.get(fileName);
		}

		if (resource == null) {
			return null;
		}
		return resource.getString(key);

	}
	/**
	 * 获取指定配置文件全部的信息
	 * 
	 * @param fileName
	 *            配置文件名称
	 * @return Map
	 * @author ll
	 */
	public static Map<String, String> getData (String fileName) {
		if( fileName.indexOf(".")>0){
			fileName=fileName.split("\\.")[0];
		}
		HashMap<String, String> content = new HashMap<>();
		ResourceBundle resource = null;
		if (pro_map.get(fileName) == null) {
			synchronized (fileName) {
				if (pro_map.get(fileName) == null) {
					resource = ResourceBundle.getBundle(fileName);
				}
			}
		} else {
			resource = pro_map.get(fileName);
		}

		if (resource == null) {
			return null;
		}
			Set<String> keySet = resource.keySet();
			for (String key : keySet) {
				try {
					content.put(key.trim(), resource.getString(key).trim());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		return content;
	}
}
