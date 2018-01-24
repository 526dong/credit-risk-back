package com.ccx.enterprise.model;

import java.io.Serializable;
/**
 * 地区表后台系统列表
 * @author xzd
 * @date 2017/7/6
 */
public class RegionList implements Serializable{
	private static final long serialVersionUID = 6508826973308837134L;

	private Integer id;//代码
	
	private String province;//省名称
	private String city;//市名称
	private String county;//县名称
	
	private Integer pid;//父节点id
	
	public RegionList() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		return "RegionList [id=" + id + ", province=" + province + ", city=" + city + ", county=" + county + ", pid="
				+ pid + "]";
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public Integer getPid() {
		return pid;
	}
	
	public void setPid(Integer pid) {
		this.pid = pid;
	}
}
