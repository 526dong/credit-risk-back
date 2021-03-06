package com.ccx.system.model;

import java.io.Serializable;

public class PermissionBeanBg implements Serializable, Cloneable {

	private static final long serialVersionUID = -757111854498620793L;
	
	private long id;//数据库id
	private String myselfId;//唯一标识
	private String parentId;//父级资源项ID
	private String permission_name;//权限名称
	private int type;//资源类型
	private int level;//层级
	private String pathUrl;//资源路径
	private String iconUrl;//图标路径
	private int sequenceNum;//排序
	private int permission_state;//状态
	private String description;//资源项描述
	private String open_mode;//打开方式
	private String company_id;//公司id
	private String createTime;//创建时间
	private String creater;//创建人
	private String modifier;//修改人
	private String modifyTime;//修改时间
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getMyselfId() {
		return myselfId;
	}
	public void setMyselfId(String myselfId) {
		this.myselfId = myselfId;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getPermission_name() {
		return permission_name;
	}
	public void setPermission_name(String permission_name) {
		this.permission_name = permission_name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getPathUrl() {
		return pathUrl;
	}
	public void setPathUrl(String pathUrl) {
		this.pathUrl = pathUrl;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public int getSequenceNum() {
		return sequenceNum;
	}
	public void setSequenceNum(int sequenceNum) {
		this.sequenceNum = sequenceNum;
	}
	public int getPermission_state() {
		return permission_state;
	}
	public void setPermission_state(int permission_state) {
		this.permission_state = permission_state;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOpen_mode() {
		return open_mode;
	}
	public void setOpen_mode(String open_mode) {
		this.open_mode = open_mode;
	}
	public String getCompany_id() {
		return company_id;
	}
	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	@Override
	public String toString() {
		return "PermissionBean [id=" + id + ", myselfId=" + myselfId + ", parentId=" + parentId + ", permission_name="
				+ permission_name + ", type=" + type + ", level=" + level + ", pathUrl=" + pathUrl + ", iconUrl="
				+ iconUrl + ", sequenceNum=" + sequenceNum + ", permission_state=" + permission_state + ", description="
				+ description + ", open_mode=" + open_mode + ", company_id=" + company_id + ", createTime=" + createTime
				+ ", creater=" + creater + ", modifier=" + modifier + ", modifyTime=" + modifyTime + "]";
	}
	

	
	
	
}
