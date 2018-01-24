package com.ccx.reporttype.service.impl;	
	
import com.ccx.reporttype.model.AbsEnterpriseReportCheck;	
import com.ccx.reporttype.service.AbsEnterpriseReportCheckService;	
import com.ccx.reporttype.dao.AbsEnterpriseReportCheckMapper;
import com.ccx.util.MyRuntimeException;
import com.ccx.util.UsedUtil;
import com.ccx.util.page.Page;
import org.springframework.beans.factory.annotation.Autowired;	
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.*;
	
@Service	
public class AbsEnterpriseReportCheckServiceImpl implements AbsEnterpriseReportCheckService {	
		
	@Autowired	
    private AbsEnterpriseReportCheckMapper absEnterpriseReportCheckMapper;	
		
	//主键获取	
	@Override	
	public AbsEnterpriseReportCheck getById(Integer id) {	
		return absEnterpriseReportCheckMapper.selectByPrimaryKey(id);	
	}	
		
	//获取无参list	
	@Override	
	public List<AbsEnterpriseReportCheck> getList() {	
		return null;	
	}	
		
	//获取有参数list	
	@Override	
	public List<AbsEnterpriseReportCheck> getList(AbsEnterpriseReportCheck model) {	
		return absEnterpriseReportCheckMapper.selectByModel(model);
	}	
		
	//获取带分页list	
	@Override	
	public Page<AbsEnterpriseReportCheck> getPageList(Page<AbsEnterpriseReportCheck> page) {	
		return null;	
	}	
		
	//通过条件获取	
	@Override	
	public AbsEnterpriseReportCheck getByModel(AbsEnterpriseReportCheck model) {	
		return null;	
	}	
	
	//保存对象	
	@Override	
	public int save(AbsEnterpriseReportCheck model) {	
		return absEnterpriseReportCheckMapper.insert(model);	
	}	
	
	//更新对象	
	@Override	
	public int update(AbsEnterpriseReportCheck model) {	
		return absEnterpriseReportCheckMapper.updateByPrimaryKey(model);	
	}	
		
	//删除对象	
	@Override	
	public int deleteById(Integer id) {	
		return absEnterpriseReportCheckMapper.deleteByPrimaryKey(id);	
	}	
		
	//其他查询	
	@Override	
	public Map<String, Object> getOther() {	
		return null;	
	}

	//保存规则
    @Override
    public void saveSetRule(Integer typeId, String formulaHtmlContent, String formulaContent) {
		Date date = new Date();
        String formulaVal = null;
		List<AbsEnterpriseReportCheck> reportCheckList = new ArrayList<>();

		if (UsedUtil.isNotNull(formulaHtmlContent) || UsedUtil.isNotNull(formulaContent)) {
			String[] htmlArry = formulaHtmlContent.split(",");
			String[] textArry = formulaContent.split(",");

			if (htmlArry.length != textArry.length) {
				throw new MyRuntimeException("你提交的数据有问题");
			}

			for (int i = 0; i < htmlArry.length; i++) {
				String html = htmlArry[i];

				if (UsedUtil.isNotNull(html)) {
					String text = textArry[i];

					if (!UsedUtil.isNotNull(text)) {
						throw new MyRuntimeException("你提交的数据有问题");
					}


                    //验证规则
                    String[] formulaValArr = new String(text).split("=");
                    if (formulaValArr.length != 2) {
                        throw new MyRuntimeException("你提交的数据条件有问题，请保证'='条件成立");
                    }
                    formulaVal =  formulaValArr[0];
                    String [] formulaValArrColumn = split(formulaVal);
                    for (String column : formulaValArrColumn) {
                        formulaVal = formulaVal.replace(column, "1");

                    }

					//保存规则
					AbsEnterpriseReportCheck reportCheck = new AbsEnterpriseReportCheck();
					reportCheck.setReportType(typeId);
					reportCheck.setFormulaChinese(html);
					reportCheck.setFormula(text);
					reportCheck.setCreateDate(date);
					reportCheckList.add(reportCheck);
				}
			}

		}

		//开始校验
        if (null != formulaVal) {
            ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");

            try {
                jse.eval(formulaVal);
            } catch (Exception e) {
                throw new MyRuntimeException("你提交的规则不正确，请仔细检查");
            }
        }

        if (reportCheckList.size() > 0) {
			absEnterpriseReportCheckMapper.deleteByTypeId(typeId);
			absEnterpriseReportCheckMapper.insertList(reportCheckList);
		}
	}

	private static String[] split(String formulaContent){
		List<String > res=new ArrayList<>();
		String[] strs = formulaContent.split("(\\s,\\s)|(\\s\\+\\s)|(\\s-\\s)|(\\s/\\s)|(\\s\\*\\s)|(\\s\\(\\s)|(\\s\\)\\s)");
		for(String str :strs){
			if(",".equals(str)||"(".equals(str)||")".equals(str)||"+".equals(str)||"-".equals(str)||"*".equals(str)||"/".equals(str)){
				continue;
			}
			res.add(str);
		}
		String[] d=new String[res.size()];
		return res.toArray(d);
	}
}	
