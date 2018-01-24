package com.ccx.index.service.impl;	
	
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ccx.enterprise.dao.EnterpriseIndustryDao;
import com.ccx.index.dao.AbsIndexFormulaMapper;
import com.ccx.index.dao.AbsIndexModelMapper;
import com.ccx.index.dao.AbsIndexModelRuleMapper;
import com.ccx.index.dao.AbsModelElementMapper;
import com.ccx.index.model.AbsIndexFormula;
import com.ccx.index.model.AbsIndexModel;
import com.ccx.index.model.AbsIndexModelRule;
import com.ccx.index.model.AbsModelElement;
import com.ccx.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccx.index.dao.AbsIndexMapper;
import com.ccx.index.dao.AbsIndexRuleMapper;
import com.ccx.index.model.AbsIndex;
import com.ccx.index.model.AbsIndexRule;
import com.ccx.index.service.AbsIndexService;
import com.ccx.util.MyRuntimeException;
import com.ccx.util.UsedUtil;
import com.ccx.util.page.Page;

/**
 * @author zhaotm
 */
@Service
public class AbsIndexServiceImpl implements AbsIndexService {

	@Autowired
    private AbsIndexMapper absIndexMapper;

	@Autowired
	private AbsIndexRuleMapper indexRuleMapper;

	@Autowired
	private AbsIndexModelMapper modelMapper;

	@Autowired
	private AbsModelElementMapper modelElementMapper;

	@Autowired
	private AbsIndexModelRuleMapper modelRuleMapper;

	@Autowired
	private AbsIndexFormulaMapper indexFormulaMapper;

	@Autowired
	private EnterpriseIndustryDao industryDao;

	//主键获取
	@Override
	public AbsIndex getById(Integer id) {
		return absIndexMapper.selectByPrimaryKey(id);
	}

	//获取无参list
	@Override
	public List<AbsIndex> getList() {
		return null;
	}

	//获取有参数list
	@Override
	public List<AbsIndex> getList(AbsIndex model) {
		return null;
	}

	//获取带分页list
	@Override
	public Page<AbsIndex> getPageList(Page<AbsIndex> page) {
		page.setRows(absIndexMapper.getPageList(page));
		return page;
	}

	//通过条件获取
	@Override
	public AbsIndex getByModel(AbsIndex model) {
		return null;
	}

	//保存对象
	@Override
	public int save(AbsIndex model) {
		return absIndexMapper.insert(model);
	}

	//更新对象
	@Override
	public int update(AbsIndex model) {
		return absIndexMapper.updateByPrimaryKey(model);
	}

	//删除对象
	@Override
	public int deleteById(Integer id) {
		return absIndexMapper.deleteByPrimaryKey(id);
	}

	//其他查询
	@Override
	public Map<String, Object> getOther() {
		return null;
	}

	//保存指标和rule
	@Override
	public void saveOrUpdatIndexAndRule(String codes,
								 String ruleIds,
								 String delIds,
								 String valueMins,
								 String values,
								 String valueMaxs,
								 String scores,
								 String degrees,
								 Integer eleId,
								 AbsIndex index) {

		//保存更新指标
		if (-1 == index.getId().intValue()) {
			//insert
			index.setId(null);
			index.setElementId(eleId);
			if (-1 == index.getFormulaId()) {
				index.setFormulaId(null);
			}
			absIndexMapper.insert(index);
		} else {
			//update
			if (null == index.getFormulaId() || -1 == index.getFormulaId()) {
				index.setFormulaId(null);
			}
			absIndexMapper.updateByPrimaryKeySelective(index);
		}

		String[] code = codes.split(",");
		String[] ruleId = ruleIds.split(",");
		String[] valueMax = valueMaxs.split(",");
		String[] value = values.split(",");
		String[] valueMin = valueMins.split(",");
		String[] score = scores.split(",");
		String[] degree = degrees.split(",");
		List<AbsIndexRule> indexRuleList = new ArrayList<>();

		for (int i=0; i<code.length; i++) {
			AbsIndexRule indexRule = new AbsIndexRule();
			try {
				indexRule.setIndexId(index.getId());
				indexRule.setId(Integer.parseInt(ruleId[i]));
				indexRule.setCode(code[i]);
				if ("0".equals(index.getRegularIndexFlag())) {
					if (StringUtils.isEmpty(valueMin[i])) {
						indexRule.setValueMin(null);
					} else {
						indexRule.setValueMin(Double.parseDouble(valueMin[i]));
					}

					if (code.length == valueMax.length) {
						if("".equals(valueMax[i].trim())){
							indexRule.setValueMax(null);
						}else{
							indexRule.setValueMax(Double.parseDouble(valueMax[i]));

						}
					} else {
						if (code.length-1 == i) {
							indexRule.setValueMax(null);
						} else {
							indexRule.setValueMax(Double.parseDouble(valueMax[i]));
						}
					}

					//只有1行
					if (1 == code.length) {
						if("".equals(valueMax[i].trim())&&"".equals(valueMin[i].trim())){
							indexRule.setValueMax(null);
							indexRule.setValueMin(null);
						}
						else if (!"".equals(valueMax[i].trim())&&"".equals(valueMin[i].trim())){
							indexRule.setValueMax(Double.parseDouble(valueMax[i]));
							indexRule.setValueMin(null);
						}else if("".equals(valueMax[i].trim())&&!"".equals(valueMin[i].trim())){
							indexRule.setValueMax(null);
							indexRule.setValueMin(Double.parseDouble(valueMin[i]));
						}
						else if (!"".equals(valueMax[i].trim())&&!"".equals(valueMin[i].trim())){
							indexRule.setValueMax(Double.parseDouble(valueMax[i]));
							indexRule.setValueMin(Double.parseDouble(valueMin[i]));
						};
					}
				} else {
					indexRule.setValue(value[i]);
				}
				indexRule.setScore(Integer.parseInt(score[i]));
				indexRule.setDegree(degree[i]);
				indexRuleList.add(indexRule);
			} catch (Exception e) {
				throw new MyRuntimeException("请检测第"+(i+1)+"行的数值填写，，得分只能是整数！");
			}
		}

		//删除规则
		if (UsedUtil.isNotNull(delIds)) {
			String[] delId = delIds.split(",");
			for (String ruleDelId: delId) {
				int ruleDelIntId = Integer.parseInt(ruleDelId);
				indexRuleMapper.deleteByPrimaryKey(ruleDelIntId);
			}
		}

		if (-1 == index.getId().intValue()) {
			//新增指标
			for (AbsIndexRule rule: indexRuleList) {
				rule.setId(null);
				rule.setIndexId(index.getId());
			}
			indexRuleMapper.insertList(indexRuleList);
		} else {
			//修改指标
			for (AbsIndexRule rule: indexRuleList) {
				int ruleUpdateId = rule.getId();
				if (-1 == ruleUpdateId) {
					//新建rule
					rule.setId(null);
					rule.setIndexId(index.getId());
					indexRuleMapper.insert(rule);
				} else {
					//修改rule
					indexRuleMapper.updateByPrimaryKeySelective(rule);
				}
			}
		}
	}

	//选择更新
	@Override
	public void updateSelective(AbsIndex index) {
		absIndexMapper.updateByPrimaryKeySelective(index);
	}

	//查行业内的指标编号是否重复
	@Override
	public Integer checkIndexCode(Integer modelId, String indexCode) {
		return absIndexMapper.checkIndexCode(modelId, indexCode);
	}
	
	//查定性指标
	@Override
	public List<AbsIndex> findNatureIndexByElementIds(List<Integer> elementIds) {
		return absIndexMapper.findNatureIndexByElementIds(elementIds);
	}

    //更好企业报表类型同时，更新index相关
	@Override
    public void syncIndex(Integer oldTypeId, Integer newTypeId) {
		try {
			Map<Integer, Integer> formulaIdMap = this.syncFormula(oldTypeId);
			this.syncModel(oldTypeId, newTypeId, formulaIdMap);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	//同步公式
	private Map<Integer, Integer> syncFormula(Integer oldTypeId) {
		Map<Integer, Integer> idMap = new HashMap<>();
		List<AbsIndexFormula> indexFormulaList = indexFormulaMapper.selectFormulaListByReportType(oldTypeId);

		for (AbsIndexFormula indexFormula : indexFormulaList) {
			//迭代公式
			Integer formulaId = indexFormula.getId();
			indexFormulaMapper.expireFormula(indexFormula);
			indexFormula.setReportTypeId(null);
			indexFormula.setLastVersionId(indexFormula.getId());
			indexFormula.setId(null);
			indexFormulaMapper.insert(indexFormula);
			idMap.put(formulaId, indexFormula.getId());

			List<AbsIndexFormula> childFormulaList = indexFormula.getFormulaList();
			List<AbsIndexFormula> newChildFormulaList = new ArrayList<>();

			//迭代子公式
			for (AbsIndexFormula childFormula : childFormulaList) {
				childFormula.setParentId(indexFormula.getId());
				childFormula.setLastVersionId(childFormula.getId());
				childFormula.setId(null);
				newChildFormulaList.add(childFormula);
			}
			indexFormulaMapper.insertList(newChildFormulaList);
		}

		return idMap;
	}

	//同步评分卡
	private void syncModel(Integer oldTypeId, Integer newTypeId, Map<Integer, Integer> formulaIdMap) {
		List<AbsIndexModel> modelList = modelMapper.selectModeListByReportType(oldTypeId);

		for (AbsIndexModel model : modelList) {
			//评级模型迭代
			modelMapper.expiredMode(model);
			Integer oldModelId = model.getId();
			model.setReportTypeId(newTypeId);
			model.setLastVersionId(oldModelId);
			model.setId(null);
			modelMapper.insert(model);
			industryDao.updateModelIdByType(oldModelId, model.getId(), 0);
			industryDao.updateModelIdByType(oldModelId, model.getId(), 1);

			List<AbsIndexModelRule> modelRuleList = model.getRuleList();
			List<AbsIndexModelRule> newModelRuleList = new ArrayList<>();
			for (AbsIndexModelRule modelRule : modelRuleList) {
				//迭代模型规则
				modelRule.setId(null);
				modelRule.setModelId(model.getId());
				newModelRuleList.add(modelRule);
			}
			modelRuleMapper.insertList(newModelRuleList);

			List<AbsModelElement> elementList = modelElementMapper.getListByModelId(oldModelId);
			for (AbsModelElement modelElement : elementList) {
				//评级因素迭代
				Integer oldElementId = modelElement.getId();
				modelElement.setId(null);
				modelElement.setModelId(model.getId());
				modelElementMapper.insert(modelElement);

				List<AbsIndex> indexList = absIndexMapper.getListByElementId(oldElementId);
				for (AbsIndex index : indexList) {
					//迭代指标
					index.setId(null);
					index.setElementId(modelElement.getId());
					index.setFormulaId(formulaIdMap.get(index.getFormulaId()));
					absIndexMapper.insert(index);

					List<AbsIndexRule> ruleList = index.getRuleList();
					List<AbsIndexRule> newRuleList = new ArrayList<>();
					for (AbsIndexRule indexRule : ruleList) {
						//迭代指标规则
						indexRule.setIndexId(index.getId());
						indexRule.setId(null);
						newRuleList.add(indexRule);
					}
					indexRuleMapper.insertList(newRuleList);
				}
			}
		}
	}
}	
