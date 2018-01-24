package com.ccx.index.service.impl;

import com.ccx.enterprise.dao.EnterpriseIndustryDao;
import com.ccx.enterprise.model.EnterpriseIndustry;
import com.ccx.index.dao.AbsIndexModelMapper;
import com.ccx.index.dao.AbsIndexModelRuleMapper;
import com.ccx.index.model.AbsIndexModel;
import com.ccx.index.model.AbsIndexModelRule;
import com.ccx.index.service.AbsIndexModelService;
import com.ccx.util.ControllerUtil;
import com.ccx.util.DoubleUtil;
import com.ccx.util.MyRuntimeException;
import com.ccx.util.UsedUtil;
import com.ccx.util.page.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
	
@Service	
public class AbsIndexModelServiceImpl implements AbsIndexModelService {	
		
	@Autowired	
    private AbsIndexModelMapper absIndexModelMapper;

	@Autowired
	private AbsIndexModelRuleMapper indexModelRuleMapper;

	@Autowired
	private EnterpriseIndustryDao industryMapper;

	private Logger logger = LogManager.getLogger(AbsIndexModelServiceImpl.class);

		
	//主键获取	
	@Override	
	public AbsIndexModel getById(Integer id) {	
		return absIndexModelMapper.selectByPrimaryKey(id);	
	}	
		
	//获取无参list	
	@Override	
	public List<AbsIndexModel> getList() {	
		return null;	
	}	
		
	//获取有参数list	
	@Override	
	public List<AbsIndexModel> getList(AbsIndexModel model) {	
		return null;	
	}	
		
	//获取带分页list	
	@Override	
	public Page<AbsIndexModel> getPageList(Page<AbsIndexModel> page) {
		page.setRows(absIndexModelMapper.getPageList(page));
		return page;
	}	
		
	//通过条件获取	
	@Override	
	public AbsIndexModel getByModel(AbsIndexModel model) {	
		return null;	
	}	
	
	//保存对象	
	@Override	
	public int save(AbsIndexModel model) {	
		return absIndexModelMapper.insert(model);	
	}	
	
	//更新对象	
	@Override	
	public int update(AbsIndexModel model) {	
		return absIndexModelMapper.updateByPrimaryKey(model);	
	}

	@Override
	public int updateSelective(AbsIndexModel model) {
		return absIndexModelMapper.updateByPrimaryKeySelective(model);
	}
		
	//删除对象	
	@Override	
	public int deleteById(Integer id) {	
		return absIndexModelMapper.deleteByPrimaryKey(id);	
	}	
		
	//其他查询	
	@Override	
	public Map<String, Object> getOther() {	
		return null;	
	}

	//保存模型和规则
	@Override
	public void saveOrUpdateModelAndRule(
			HttpServletRequest request,
			 				     String codes,
								 String ruleIds,
								 String delIds,
								 String valueMins,
								 String valueMaxs,
								 String scores,
								 String degrees,
								 Integer id,
								 String name) {
		AbsIndexModel model = null;

		//规则校验
		double sumValue = 0.0;
		String[] code = codes.split(",");
		String[] ruleId = ruleIds.split(",");
		String[] valueMax = valueMaxs.split(",");
		String[] valueMin = valueMins.split(",",-1);
		String[] score = scores.split(",");
		String[] degree = degrees.split(",");
		List<AbsIndexModelRule> modelRuleList = new ArrayList<>();

		for (int i=0; i<code.length; i++) {
			AbsIndexModelRule modelRule = new AbsIndexModelRule();
			try {
				double max = Double.parseDouble(valueMax[i]);
				double min = Double.parseDouble(valueMin[i]);
				double tmp = DoubleUtil.sub(max, min);
				sumValue = DoubleUtil.add(sumValue, tmp);
				logger.info("[max-min]="+max+"-"+min+"="+sumValue);

				modelRule.setId(Integer.parseInt(ruleId[i]));
				modelRule.setModelId(id);
				modelRule.setCode(code[i]);
				modelRule.setValueMax(max);
				modelRule.setValueMin(min);
				modelRule.setScore(Integer.parseInt(score[i]));
				modelRule.setDegree(degree[i]);
				modelRuleList.add(modelRule);
			} catch (Exception e) {
				throw new MyRuntimeException("请检测第"+(i+1)+"行的区间值和得分填写，区间值只能是小数，得分只能是整数！");
			}
		}

		if (new Double(sumValue).compareTo(new Double(10)) != 0) {
			throw new MyRuntimeException("请检查分值区间，保证区间分值合为10");
		}

		//删除规则
		if (UsedUtil.isNotNull(delIds)) {
			String[] delId = delIds.split(",");
			for (String ruleDelId: delId) {
				int ruleDelIntId = Integer.parseInt(ruleDelId);
				indexModelRuleMapper.deleteByPrimaryKey(ruleDelIntId);
			}
		}
		if (-1 != id) {
			//修改模型
			model = absIndexModelMapper.selectByPrimaryKey(id);
			model.setName(name);
			absIndexModelMapper.updateByPrimaryKey(model);

			for (AbsIndexModelRule rule: modelRuleList) {
				int ruleUpdateId = rule.getId();
				if (-1 == ruleUpdateId) {
					//新建rule
					rule.setId(null);
					rule.setModelId(model.getId());
					indexModelRuleMapper.insert(rule);
				} else {
					//修改rule
					indexModelRuleMapper.updateByPrimaryKey(rule);
				}

			}
		} else {
			//新增模型
			model = new AbsIndexModel();
			model.setName(name);
			model.setCreator(ControllerUtil.getSessionUser(request).getLoginName());
			model.setCreateTime(new Date());
			absIndexModelMapper.insert(model);

			for (AbsIndexModelRule rule: modelRuleList) {
				rule.setId(null);
				rule.setModelId(model.getId());
			}

			indexModelRuleMapper.insertList(modelRuleList);
		}
	}

	//删除模型和规则
	@Override
	public void deleteModelAndRule(AbsIndexModel model) {
		industryMapper.deleteModelRe0(model.getId());
		industryMapper.deleteModelRe1(model.getId());
		absIndexModelMapper.expiredMode(model);
	}

	//保存匹配
	@Override
	public void saveMatch(String id, Integer reportTypeId, String industryId, String type, String delId, String delType, boolean batchFlag) {
		if (UsedUtil.isNotNull(id)) {
			//批量和单个匹配公共方法
			String[] ids = id.split(",");

			//批量处理modelId
			for (int j=0; j<ids.length; j++ ) {
				if (UsedUtil.isNotNull(ids[j])) {
					Integer moId = Integer.parseInt(ids[j]);

					if (UsedUtil.isNotNull(industryId)) {
						//更新匹配
						String[] industryIds = industryId.split(",");
						String[] types = type.split(",");
						Integer indId = null;
						Integer t = null;

						if (false == batchFlag) {
							//单个匹配
							for (int i = 0; i < industryIds.length; i++) {
								//保存此模型下的行业
								indId = Integer.parseInt(industryIds[i]);
								t = Integer.parseInt(types[i]);
								//模型和表类型修改
								AbsIndexModel model = absIndexModelMapper.selectByPrimaryKey(moId);

								if (model == null) {
									throw new MyRuntimeException("未查询到评分模型");
								}
								Integer reportTypeIdBak = model.getReportTypeId();
								if (null == reportTypeIdBak) {
									model.setReportTypeId(reportTypeId);
									absIndexModelMapper.updateByPrimaryKey(model);
								} else if (reportTypeId.intValue() != reportTypeIdBak.intValue()) {
									model.setReportTypeId(reportTypeId);
									absIndexModelMapper.updateByPrimaryKey(model);
									//删除上次匹配的行业
									industryMapper.deleteModelRe0(moId);
									industryMapper.deleteModelRe1(moId);
								}

								EnterpriseIndustry industry = industryMapper.selectByPrimaryKey(indId);
								if (0 == t && null != industry.getModelId0() && moId.intValue() != industry.getModelId0().intValue()) {
									throw new MyRuntimeException(industry.getName()+"行业 中大型已匹配其他评分卡，请先修改");
								}
								if (1 == t && null != industry.getModelId1() && moId.intValue() != industry.getModelId1().intValue()) {
									throw new MyRuntimeException(industry.getName()+"行业 小微型已匹配其他评分卡，请先修改");
								}
								this.mySaveIndustry(indId, moId, t);
							}
						} else {
							//批量匹配
							indId = Integer.parseInt(industryIds[j]);
							t = Integer.parseInt(types[j]);
							this.mySaveIndustry(indId, moId, t);
						}
					}
				} else {
					//删除行业的模型关联
				}
			}
		}

		if (UsedUtil.isNotNull(delId)) {
			//删除匹配
			String[] delIndustryIds = delId.split(",");
			String[] delIndustryTypes = delType.split(",");

			for (int i=0; i<delIndustryIds.length; i++) {
				Integer indId = Integer.parseInt(delIndustryIds[i]);
				Integer t = Integer.parseInt(delIndustryTypes[i]);

				EnterpriseIndustry industry = industryMapper.selectByPrimaryKey(indId);
				if (0 == t) {
					industry.setModelId0(null);
				} else if (1 == t) {
					industry.setModelId1(null);
				}
				industryMapper.updateByPrimaryKey(industry);
			}
		}
	}

	private void mySaveIndustry(Integer indId, Integer moId, Integer t) {
		EnterpriseIndustry industry = industryMapper.selectByPrimaryKey(indId);
		if (0 == t) {
			industry.setModelId0(moId);
		} else if (1 == t) {
			industry.setModelId1(moId);
		}
		industryMapper.updateByPrimaryKey(industry);
	}

	//通过名字模糊查询
    @Override
    public List<AbsIndexModel> getByLikeName(String name) {
        return absIndexModelMapper.getByLikeName(name);
    }
}	
