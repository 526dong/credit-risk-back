package com.ccx.index.service.impl;	
	
import com.ccx.index.dao.AbsIndexMapper;
import com.ccx.index.model.AbsIndex;
import com.ccx.index.model.AbsIndexFormula;
import com.ccx.index.service.AbsIndexFormulaService;	
import com.ccx.index.dao.AbsIndexFormulaMapper;
import com.ccx.util.ControllerUtil;
import com.ccx.util.UsedUtil;
import com.ccx.util.page.Page;
import org.springframework.beans.factory.annotation.Autowired;	
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
	
@Service	
public class AbsIndexFormulaServiceImpl implements AbsIndexFormulaService {	
		
	@Autowired	
    private AbsIndexFormulaMapper absIndexFormulaMapper;

	@Autowired
	private AbsIndexMapper indexMapper;
		
	//主键获取	
	@Override	
	public AbsIndexFormula getById(Integer id) {	
		return absIndexFormulaMapper.selectByPrimaryKey(id);	
	}	
		
	//获取无参list	
	@Override	
	public List<AbsIndexFormula> getList() {	
		return null;	
	}	
		
	//获取有参数list	
	@Override	
	public List<AbsIndexFormula> getList(AbsIndexFormula model) {	
		return null;	
	}	
		
	//获取带分页list	
	@Override	
	public Page<AbsIndexFormula> getPageList(Page<AbsIndexFormula> page) {
		page.setRows(absIndexFormulaMapper.getPageList(page));
		return page;
	}

	////获取带分页withoutlist
	@Override
	public Page<AbsIndexFormula> getPageWithoutFormulaList(Page<AbsIndexFormula> page) {
		page.setRows(absIndexFormulaMapper.getPageWithoutFormulaList(page));
		return page;
	}
		
	//通过条件获取	
	@Override	
	public AbsIndexFormula getByModel(AbsIndexFormula model) {	
		return null;	
	}	
	
	//保存对象	
	@Override	
	public int save(AbsIndexFormula model) {	
		return absIndexFormulaMapper.insert(model);	
	}	
	
	//更新对象	
	@Override	
	public int update(AbsIndexFormula model) {	
		return absIndexFormulaMapper.updateByPrimaryKey(model);	
	}	
		
	//删除对象	
	@Override	
	public int deleteById(Integer id) {
		//删除指标中公式引用
		AbsIndex index = indexMapper.getByFormulaId(id);
		if (null != index) {
			index.setFormulaName(null);
			index.setFormulaId(null);
			indexMapper.updateByPrimaryKey(index);
		}
		//删除子公式
		absIndexFormulaMapper.deleteByParentId(id);
		//删除公式
		return absIndexFormulaMapper.deleteByPrimaryKey(id);	
	}	
		
	//其他查询	
	@Override	
	public Map<String, Object> getOther() {	
		return null;	
	}

	//名字模糊查询
	@Override
	public List<AbsIndexFormula> getbyLikeName(String formulaName, Integer reportType) {
		return absIndexFormulaMapper.getByLikeName("%"+formulaName+"%", reportType);
	}

	//保存or新增
	@Override
	public void saveOrUpdate(HttpServletRequest request, AbsIndexFormula formulaParent) {
		if (-1 == formulaParent.getId().intValue()) {
			//新增
			formulaParent.setId(null);
			formulaParent.setParentFlag(1);
			formulaParent.setCreateTime(new Date());
			formulaParent.setCreator(ControllerUtil.getSessionUser(request).getLoginName());
			absIndexFormulaMapper.insert(formulaParent);

			if (UsedUtil.isNotNull(formulaParent.getFormulaContent1()) && UsedUtil.isNotNull(formulaParent.getFormulaHtmlContent1())) {
				this.myCreateNewFormula(formulaParent.getFormulaName(), formulaParent.getFormulaContent1(), formulaParent.getFormulaHtmlContent1(),1, formulaParent.getId());
			}
			if (UsedUtil.isNotNull(formulaParent.getFormulaContent2()) && UsedUtil.isNotNull(formulaParent.getFormulaHtmlContent2())) {
				this.myCreateNewFormula(formulaParent.getFormulaName(), formulaParent.getFormulaContent2(), formulaParent.getFormulaHtmlContent2(),2, formulaParent.getId());

			}
			if (UsedUtil.isNotNull(formulaParent.getFormulaContent3()) && UsedUtil.isNotNull(formulaParent.getFormulaHtmlContent3())) {
				this.myCreateNewFormula(formulaParent.getFormulaName(), formulaParent.getFormulaContent3(), formulaParent.getFormulaHtmlContent3(),3, formulaParent.getId());

			}
		} else {
			//更新
			absIndexFormulaMapper.updateByPrimaryKeySelective(formulaParent);
			/*//同时更新指标中公式的名字
			AbsIndex index = indexMapper.getByFormulaId(formulaParent.getId());
			if (null != index) {
				index.setFormulaName(formulaParent.getFormulaName());
				indexMapper.updateByPrimaryKey(index);
			}*/
			if (UsedUtil.isNotNull(formulaParent.getFormulaContent1()) && UsedUtil.isNotNull(formulaParent.getFormulaHtmlContent1())) {
				AbsIndexFormula formula = absIndexFormulaMapper.getbyParentIdAndYear(formulaParent.getId(), 1);
				if (null != formula) {
					formula.setFormulaName(formulaParent.getFormulaName());
					formula.setFormulaContent(formulaParent.getFormulaContent1());
					formula.setFormulaHtmlContent(formulaParent.getFormulaHtmlContent1());
					absIndexFormulaMapper.updateByPrimaryKey(formula);
				} else {
					this.myCreateNewFormula(formulaParent.getFormulaName(), formulaParent.getFormulaContent1(), formulaParent.getFormulaHtmlContent1(),1, formulaParent.getId());
				}

			}
			if (UsedUtil.isNotNull(formulaParent.getFormulaContent2()) && UsedUtil.isNotNull(formulaParent.getFormulaHtmlContent2())) {
				AbsIndexFormula formula = absIndexFormulaMapper.getbyParentIdAndYear(formulaParent.getId(), 2);
				if (null != formula) {
					formula.setFormulaContent(formulaParent.getFormulaContent2());
					formula.setFormulaHtmlContent(formulaParent.getFormulaHtmlContent2());
					absIndexFormulaMapper.updateByPrimaryKey(formula);
				} else {
					this.myCreateNewFormula(formulaParent.getFormulaName(), formulaParent.getFormulaContent2(), formulaParent.getFormulaHtmlContent2(),2, formulaParent.getId());
				}
			}
			if (UsedUtil.isNotNull(formulaParent.getFormulaContent3()) && UsedUtil.isNotNull(formulaParent.getFormulaHtmlContent3())) {
				AbsIndexFormula formula = absIndexFormulaMapper.getbyParentIdAndYear(formulaParent.getId(), 3);
				if (null != formula) {
					formula.setFormulaContent(formulaParent.getFormulaContent3());
					formula.setFormulaHtmlContent(formulaParent.getFormulaHtmlContent3());
					absIndexFormulaMapper.updateByPrimaryKey(formula);
				} else {
					this.myCreateNewFormula(formulaParent.getFormulaName(), formulaParent.getFormulaContent3(), formulaParent.getFormulaHtmlContent3(),3, formulaParent.getId());
				}
			}
		}
	}

	//查找公式在指标的引用
    @Override
    public List<AbsIndex> findIndexUsage(Integer formulId) {
        return indexMapper.findIndexUsage(formulId);
    }

    //删除公式同时删除指标中对公式的引用
	@Override
	public void deleteFormulaAndIndexUsage(Integer id) {
		AbsIndexFormula formula = new AbsIndexFormula();
		formula.setId(id);
		absIndexFormulaMapper.expireFormula(formula);
		indexMapper.updateFormulaUsage(id);
	}

	//创建新的子公式(不使用事物的命名)
	private void myCreateNewFormula(String name, String content, String html, int year, int parentId) {
		AbsIndexFormula formula = new AbsIndexFormula();
		formula.setFormulaName(name);
		formula.setFormulaContent(content);
		formula.setFormulaHtmlContent(html);
		formula.setYear(year);
		formula.setParentFlag(0);
		formula.setParentId(parentId);
		absIndexFormulaMapper.insert(formula);
	}

	//查询名字是否重复
	@Override
	public List<Integer> getIdByFormulaName(String name) {
		return absIndexFormulaMapper.selectIdByName(name);
	}

}	
