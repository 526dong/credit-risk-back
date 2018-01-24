package com.ccx.reporttype.pojo;

import com.ccx.util.DateUtils;

/**
 * @Description:
 * @author:lilong
 * @Date: 2017/10/11
 */
public class AbsReportType {
    //序号
    private Integer id;
    //类型名称
    private String name;
    //核验规则
    private Integer ifcheck;
    //匹配行业
    private String tnames;
    //状态
    private Integer status;
    //创建人
    private String cname;
    //创建时间
    private String ctime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIfcheck() {
        return ifcheck;
    }

    public void setIfcheck(Integer ifcheck) {
        this.ifcheck = ifcheck;
    }

    public String getTnames() {
        return tnames;
    }

    public void setTnames(String tnames) {
        this.tnames = tnames;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCtime() {
        return ctime;
    }
    
    public String getCtimeStr() throws Exception {
		if (ctime != null) {
     		return DateUtils.formatDateStr(DateUtils.parseStr2Date(ctime));
 		}else{
 			return null;
 		}
	}

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }
}
