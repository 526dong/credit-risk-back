package com.ccx.index.controller;

import com.alibaba.fastjson.JSON;
import com.ccx.businessmng.model.AbsEnterpriseReportType;
import com.ccx.businessmng.service.AbsEnterpriseReportTypeService;
import com.ccx.index.model.AbsIndex;
import com.ccx.index.model.AbsIndexFormula;
import com.ccx.index.service.AbsIndexFormulaService;
import com.ccx.reportmsg.manager.CommonGainReport;
import com.ccx.reportmsg.manager.ComonReportPakage;
import com.ccx.util.EnterpriseRatingUtils;
import com.ccx.util.JsonResult;
import com.ccx.util.MyRuntimeException;
import com.ccx.util.page.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller	
@RequestMapping("/businessMng")
public class AbsFormulaController {

	private Logger logger = LogManager.getLogger(AbsFormulaController.class);

	@Autowired
	private AbsIndexFormulaService indexFormulaService;

	@Autowired
	private AbsEnterpriseReportTypeService reportTypeService;

	@Autowired
	private CommonGainReport commonGainReport;

	@Autowired
	private EnterpriseRatingUtils enterpriseRatingUtils;

	/*	
	 * @author 	
	 * @description	
	 * @date	
	*/
	@RequestMapping(value = "/formulaIndex", method = RequestMethod.GET)
	public ModelAndView formulaIndex(HttpServletRequest request) {
		ModelAndView mnv = new ModelAndView("businessMng/formulaIndex");

		try {
			List<AbsEnterpriseReportType> reportTypeList = reportTypeService.getList();
			List<AbsEnterpriseReportType> typeList = reportTypeService.getList();

			mnv.addObject("typeList", JSON.toJSONString(typeList));
			mnv.addObject("reportTypeList", reportTypeList);
		} catch (Exception e) {
			logger.error("公式列表：",e);
		}
		return mnv;
	}

	/*
   * @author
   * @description	查看公式列表
   * @date
  */
	@RequestMapping(value = "/formulaList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> formulaList(Integer pageNo, Integer pageSize, String name, Integer reportTypeId) {
		Map<String, Object> resultMap = new HashMap<>();
		Page<AbsIndexFormula> page = new Page<AbsIndexFormula>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("parentFlag", new Integer(1));
		params.put("name", "%"+name+"%");
		params.put("reportTypeId", reportTypeId);

		try {
			page.setParams(params);
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			page = indexFormulaService.getPageList(page);

			resultMap.put("code", 200);
			resultMap.put("page", page);
		} catch (Exception e) {
			logger.error("查看列表:", e);
			resultMap.put("code", 500);
		}
		return resultMap;
	}

	/*
	 * @author
	 * @description	编辑公式页面
	 * @date
	*/
	@RequestMapping(value = "formulaAddOrEdit", method = RequestMethod.GET)
	public ModelAndView formulaAddOrEdit(@RequestParam(required = true) Integer id, String method) {
		ModelAndView mnv = new ModelAndView("businessMng/formulaAddOrEdit");

		try {
			if (-1 != id) {
				//编辑或查看
				prepareFormula(id, mnv);
			}

			List<AbsEnterpriseReportType> reportTypeList = reportTypeService.getList();
			mnv.addObject("reportTypeList", reportTypeList);
			//this.prepareReportAllDiv(reportTypeId, mnv);
			mnv.addObject("method", method);
		} catch (Exception e) {
			logger.error("新增编辑公式页面:", e);
		}
		return mnv;
	}

	/*
	* @Description:  根据报表id查模板
	* @Author:       zhaotm
	* @CreateDate:   ${DATE} ${TIME}
	* @Param:        v1.0
	* @Return        json
	*/
	@PostMapping("prepareReportAllDiv")
	@ResponseBody
	public JsonResult prepareReportAllDiv(@RequestParam(required = true) Integer reportTypeId) {
		try {
			List<ComonReportPakage.Modle> modleList = commonGainReport.getModles(reportTypeId);
			return JsonResult.ok(modleList);
		} catch (Exception e) {
			logger.error("公式查询报表模板失败:",e);
			return JsonResult.error("公式查询报表模板失败");
		}
	}

	/*
	 * @author
	 * @description	编辑公式页面
	 * @date
	*/
	@RequestMapping(value = "formulaShow", method = RequestMethod.GET)
	public ModelAndView formulaShow(@RequestParam(required = true) Integer id, String method) {
		ModelAndView mnv = new ModelAndView("businessMng/formulaShow");

		try {
			//编辑或查看
			prepareFormula(id, mnv);
			mnv.addObject("method", method);
			List<AbsEnterpriseReportType> typeList = reportTypeService.getList();
			mnv.addObject("typeList", JSON.toJSON(typeList));
		} catch (Exception e) {
			logger.error("新增编辑公式页面:", e);
		}
		return mnv;
	}

	/*
	 * @author
	 * @description	保存公式
	 * @date
	*/
	@RequestMapping(value = "/formulaSaveOrUpdate", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> formulaSaveOrUpdate(HttpServletRequest request, AbsIndexFormula formula) {
		Map<String, Object> resultMap = new HashMap<>();

		try {
			boolean flag = false;
			StringBuilder sb = new StringBuilder();
			List<AbsIndex> indexList = indexFormulaService.findIndexUsage(formula.getId());
			List<Integer> ids = indexFormulaService.getIdByFormulaName(formula.getFormulaName());
			if(ids.size()>0){
				resultMap.put("code", 400);
				resultMap.put("msg", "公式名称已经被使用");
				return  resultMap;
			}
			if (indexList.size() >0) {
				for (AbsIndex index: indexList) {
					if (formula.getYearLen() < index.getAveYears()) {
						flag = true;
						sb.append(index.getIndexName());
					}
				}
			}
			if (flag) {
				resultMap.put("code", 400);
				resultMap.put("msg", "指标："+sb.toString()+"已经引用了公式定义的年份，请先修改公式中的年份！");
			} else {
				resultMap.put("code", 200);
				Map<String, String> formulaMap = enterpriseRatingUtils.getAllCacheFormula(indexFormulaService, formula.getReportTypeId());
				enterpriseRatingUtils.validateFormula(formula, formulaMap);
				indexFormulaService.saveOrUpdate(request, formula);
				//enterpriseRatingUtils.deleteCacheFormula();
			}
		} catch (Exception e) {
			if (e instanceof MyRuntimeException) {
				resultMap.put("code", 400);
				resultMap.put("msg", e.getMessage());
			} else {
				resultMap.put("code", 500);
			}
			logger.error("保存公式:", e);

		}
		return resultMap;
	}

	/*
	 * @author
	 * @description	公查找引用
	 * @date
	*/
	@RequestMapping(value = "/formulaFindUsage", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> formulaFindUsage(@RequestParam(required = true)Integer id) {
		Map<String, Object> resultMap = new HashMap<>();

		try {
			resultMap.put("code", 200);
			List<AbsIndex> indexList = indexFormulaService.findIndexUsage(id);
			if (indexList.size() >0) {
				resultMap.put("extis", "extis");
				StringBuilder sb = new StringBuilder();
				for (AbsIndex index: indexList) {
					sb.append(index.getIndexName());
				}
				resultMap.put("name",sb.toString());
			} else {
				resultMap.put("extis", "notExtis");
			}
		} catch (Exception e) {
			logger.error("公查找引用:", e);
			resultMap.put("code", 500);
		}
		return resultMap;
	}

	/*
	 * @author
	 * @description	保存公式
	 * @date
	*/
	@RequestMapping(value = "/formulaDel", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> formulaDel(Integer id) {
		Map<String, Object> resultMap = new HashMap<>();

		try {
			resultMap.put("code", 200);
			indexFormulaService.deleteFormulaAndIndexUsage(id);
			//enterpriseRatingUtils.deleteCacheFormula();
		} catch (Exception e) {
			logger.error("删除公式:", e);
			resultMap.put("code", 500);
		}
		return resultMap;
	}

	private void prepareFormula(Integer id, ModelAndView mnv) {
		AbsIndexFormula formula = indexFormulaService.getById(id);
		if (null != formula) {
			for (AbsIndexFormula formularBak : formula.getFormulaList()) {
				if (1 == formularBak.getYear()) {
					formula.setFormulaContent1(formularBak.getFormulaContent());
					formula.setFormulaHtmlContent1(formularBak.getFormulaHtmlContent());
				} else if (2 == formularBak.getYear()) {
					formula.setFormulaContent2(formularBak.getFormulaContent());
					formula.setFormulaHtmlContent2(formularBak.getFormulaHtmlContent());
				} else if (3 == formularBak.getYear()) {
					formula.setFormulaContent3(formularBak.getFormulaContent());
					formula.setFormulaHtmlContent3(formularBak.getFormulaHtmlContent());
				}
			}
		}
		mnv.addObject("formula", formula);
	}

	//把字段放进去
	/*private void prepareReportAllDiv(Integer typeId,  ModelAndView mnv) {
		prepareReportAssetDiv(mnv);
		prepareReportLeDiv(mnv);
		prepareReportProfitLossDiv(mnv);
		prepareReportCashFlowDiv(mnv);
		prepareReportACFDiv(mnv);
		prepareReportNFSDiv(mnv);
	}*/

	/*private void prepareReportAssetDiv(ModelAndView mnv) {
		StringBuilder sb = new StringBuilder();
		sb.append("货币资金$CASH_EQUIVALENTS");
		sb.append(",结算备付金$DEPOSIT_RESER");
		sb.append(",拆出资金$LENDINGS");
		sb.append(",交易性金融资产$FIN_ASSETS_TRANDING");
		sb.append(",应收票据$NOTES_RECEIVABAL");
		sb.append(",应收帐款$ACCOUNT_RECEIVABAL");
		sb.append(",预付帐款$PREPAYMENT");
		sb.append(",应收保费$PREMIUMS_RECEIVABAL");
		sb.append(",应收分保帐款$REIN_ACCOUNT_RECEIVABAL");
		sb.append(",应收分保合同准备金$YSFBHTZBJ");
		sb.append(",应收利息$INTEREST_RECEIVBAL");
		sb.append(",其他应收款$OTHER_ACCOUNT_RECEIVABAL");
		sb.append(",买入返售金融资产$FIN_ASSETS_BUYINGBACK");
		sb.append(",存货$INVENTORY");
		sb.append(",一年内到期的非流动$NONCURRENT_ASSETS_1Y");
		sb.append(",其他流动资产$OTHER_CURRENT_ASSETS");
		sb.append(",流动资产合计$TOTAL_CURRENT_ASSETS");
		sb.append(",发放贷款及垫款$LOANS_PAYMENTS");
		sb.append(",可供出售金融资产$FIN_ASSETS_SALE");
		sb.append(",持有至到期投资$INVEST_MATURITY");
		sb.append(",长期应收款$ACCOUNT_RECEIVABAL_LONG");
		sb.append(",长期股权投资$EQUITY_INVESTMENT_LONG");
		sb.append(",投资性房地产$INVESTMENT_REAL_ESTATE");
		sb.append(",固定资产$FIXED_ASSETS");
		sb.append(",在建工程$CONSTRUCTION_PROGRESS");
		sb.append(",工程物资$ENGINEERING_MATERIALS");
		sb.append(",固定资产清理$DISPOSAL_FIXED_ASSETS");
		sb.append(",生产性生物资产$PRODUCTIVE_LIVING_ASSETS");
		sb.append(",油气资产$OIL_GAS_ASSETS");
		sb.append(",无形资产$INTANGIBLE_ASSETS");
		sb.append(",开发支出$DEVELOPMENT_EXPENDITURE");
		sb.append(",商誉$GOODWILL");
		sb.append(",长期待摊费用$DEFERRED_EXPENSES_LONG");
		sb.append(",递延所得税资产$DEFERRED_INCOMETAX_ASSETS");
		sb.append(",其他非流动资产$OTHER_NONCURRENT_ASSETS");
		sb.append(",非流动资产合计$TOTAL_NONCURRENT_ASSETS");
		sb.append(",资产总计$TOTAL_ASSETS");

		mnv.addObject("assetList", this.getReportVOList(sb));
	}

	private void prepareReportLeDiv(ModelAndView mnv) {
		StringBuilder sb = new StringBuilder();
		sb.append("短期借款$BORROWINGS_SHORT");
		sb.append(",向中央银行借款$BORROWINGS_CENTRALBANK");
		sb.append(",吸收存款及同业存放$XSCKJTYCF");
		sb.append(",拆入资金$BORROWINGS_BANKS_OTHER");
		sb.append(",交易性金融负债$TRANSACTIONAL_LIABILITIES");
		sb.append(",应付票据$NOTES_PAYABLE");
		sb.append(",应付帐款$ACCOUNT_PAYABLE");
		sb.append(",预收款项$ACCOUNT_PAYABLE_ADVANCE");
		sb.append(",卖出回购金融资产款$FINASSETS_SOLD_REPURCHASE");
		sb.append(",应付手续费及佣金$COMMISSION_PAYABLE");
		sb.append(",应付职工薪酬$PAYROLL_PAYABLE");
		sb.append(",应交税费$TAX_PAYABLE");
		sb.append(",应付利息$INTEREST_PAYABLE");
		sb.append(",其他应付款$OTHER_ACCOUNT_PAYABLE");
		sb.append(",应付分保帐款$REIN_ACCOUNT_PAYABLE");
		sb.append(",保险合同准备金$BXHTZBJ");
		sb.append(",代理买卖证券款$DLMMZQK");
		sb.append(",代理承销证券款$DLCXZQK");
		sb.append(",一年内到期的非流动负债$YNNDQDFLDFZ");
		sb.append(",其他流动负债$OTHER_CLOSE_LIABILITIES");
		sb.append(",流动负债合计$TOTAL_CLOSE_LIABILITIES");
		sb.append(",长期借款$BORROWINGS_LONG");
		sb.append(",应付债券$BONDS_LONG");
		sb.append(",长期应付款$ACCOUNTS_PAYABLE_LONG");
		sb.append(",专项应付款$SPECIAL_ACCOUNTS_PAYABLE");
		sb.append(",预计负债$ESTIMATED_LIABILITIES");
		sb.append(",递延所得税负债$DEFFERED_INCOMETAX_LIABILITIES");
		sb.append(",其他非流动负债$OTHER_NONCLOSE_LIABILITIES");
		sb.append(",非流动负债合计$TOTAL_NONCLOSE_LIABILITIES");
		sb.append(",负债合计$TOTAL_LIABILITIES");
		sb.append(",实收资本(或股本)$PAID_CAPITAL");
		sb.append(",其他权益工具$OTHER_EQUITY_INSTRUMENT");
		sb.append(",资本公积$CAPITAL_SURPLUS");
		sb.append(",减：库存股$TREASURY_SHARE");
		sb.append(",其他综合收益$OTHER_COMPREHENSIVE_INCOME");
		sb.append(",盈余公积$EARNED_SURPLUS");
		sb.append(",一般风险准备$GENERIC_RISK_RESERVE");
		sb.append(",未分配利润$UNDISTRIBUTED_PROFIT");
		sb.append(",外币报表折算差额$CURRENCY_TRANSLATION_DIFFERENCES");
		sb.append(",归属于母公司所有者权益合计$GSYMGSSYZQYHJ");
		sb.append(",少数股东权益$MINORITY_EQUITY");
		sb.append(",所有者权益合计$TOTAL_EQUITIES");
		sb.append(",负债和所有者权益总计$TOTAL_LIABILITIES_EQUITIES");

		mnv.addObject("LeList", this.getReportVOList(sb));
	}

	private void prepareReportProfitLossDiv(ModelAndView mnv) {
		StringBuilder sb = new StringBuilder();
		sb.append("一、营业总收入$TOTAL_OPERATING_INCOME");
		sb.append(",其中:营业收入$OPERATING_INCOME");
		sb.append(",利息收入$INTEREST_INCOME");
		sb.append(",已赚保费$PREMIUM_INCOME");
		sb.append(",手续费及佣金收入$COMMISSION_INCOME");
		sb.append(",二、营业总成本$TOTAL_OPERATING_COST");
		sb.append(",其中:营业成本$OPERATING_COST");
		sb.append(",利息支出$INTEREST_EXPENSES");
		sb.append(",手续费及佣金支出$COMMISSION_EXPENSES");
		sb.append(",退保金$LOAN_VALUE");
		sb.append(",赔付支出净额$COMPENSATION_EXPENSES");
		sb.append(",提取保险合同准备金净额$APPROPRIATION_DEPOSIT_DUTY");
		sb.append(",保单红利支出$DIVIDEND_EXPENSES_INSURED");
		sb.append(",分保费用$REINSURANCE_EXPENSES");
		sb.append(",营业税金及附加$BUSINESS_TAXES_SURCHARGES");
		sb.append(",销售费用$SELLING_EXPENSES");
		sb.append(",管理费用$GENERAL_ADMINISTRATIVE_EXPENSES");
		sb.append(",财务费用$FINANCIAL_EXPENSES");
		sb.append(",资产减值损失$ASSETS_DEVALUATION");
		sb.append(",加:公允价值变动收益(损失以\"-\"号填列)$PROFIT_LOSS_FAIRVALUE");
		sb.append(",投资收益(损失以\"-\"填列)$INVESTMENT_EARNINGS");
		sb.append(",其中:对联营企业和合营企业的投资收益(损失以\"-\"填列)$EARNINGS_JOINT_VENTURES");
		sb.append(",汇兑收益(损失以\"-\"填列)$EXCHANGE_GAIN_LOSS");
		sb.append(",三、营业利润（亏损以\"-\"填列）$OPERATING_PROFIT");
		sb.append(",加:营业外收入$NONOPERATING_INCOME");
		sb.append(",减:营业外支出$NONOPERATING_EXPENSES");
		sb.append(",其中:非流动资产处置损失$LOSS_NONCLOSEASSETS_DISPOSAL");
		sb.append(",四、利润总额（亏损以\"-\"填列）$PROFIT_BEFORE_TAXES");
		sb.append(",减:所得税费用$INCOME_TAX");
		sb.append(",五、净利润（亏损以\"-\"填列）$NET_PROFIT");
		sb.append(",归属于母公司所有者的净利润$GSYMGSSYZDJLR");
		sb.append(",少数股东损益$MINORITY_EQUITY_INCOME");
		sb.append(",六、每股收益:$EARNING_PERSHARE");
		sb.append(",(一)基本每股收益$EARNING_PERSHARE_BASIC");
		sb.append(",(二)稀释每股收益$EARNING_PERSHARE_DILUTED");

		mnv.addObject("profitLossList", this.getReportVOList(sb));
	}

	private void prepareReportCashFlowDiv(ModelAndView mnv) {
		StringBuilder sb = new StringBuilder();
		sb.append("销售商品、提供劳务收到的现金$CASHRECEIVED_COMMODITIES_LABOR");
		sb.append(",客户存款和同业存放款项净增加额$INCREASE_DEPOSIT");
		sb.append(",向中央银行借款净增加额$INCREASE_BORROWINGS_CENTRALBANK");
		sb.append(",向其他金融机构拆入资金净增加额$INCREASE_BORROWING_OTHERINSTITUTION");
		sb.append(",收到原保险合同保费取得的现金$CASHRECEIVED_ORIGINAL_INSURANCE");
		sb.append(",收到再保险业务现金净额$CASHRECEIVED_REINSURANCE_BUSINESS");
		sb.append(",保户储金及投资款净增加额$INCREASE_INSURED_DEPOSIT");
		sb.append(",处置交易性金融资产净增加额$INCREASE_FINANCIAL_DISPOSAL");
		sb.append(",收取利息、手续费及佣金的现金$CASHRECIVED_INTEREST_COMMISSION");
		sb.append(",拆入资金净增加额$INCREASE_BORROWING_FUNDS");
		sb.append(",回购业务资金净增加额$INCREASE_REPURCHASING_FUNDS");
		sb.append(",收到的税费返还$REFUND_TAX");
		sb.append(",收到其他与经营活动有关的现金$CASHRECEIVED_OTHER_OPERATING");
		sb.append(",经营活动现金流入小计$CASHINFLOW_OPERATING_ACTIVITIES");
		sb.append(",购买商品、接受劳务支付的现金$CASHPAID_COMMODITIES_LABOR");
		sb.append(",客户贷款及垫款净增加额$INCREASE_CUSTOMER_LOANS");
		sb.append(",存放中央银行和同业款项净增加额$INCREASE_DEPOSIT_BANK");
		sb.append(",支付原保险合同赔付款项的现金$CASHPAID_ORIGINAL_INSURANCE");
		sb.append(",支付利息、手续费及佣金的现金$CASHPAID_INTEREST_COMMISSION");
		sb.append(",支付保单红利的现金$CASHPAID_POLICY_DIVIDEND");
		sb.append(",支付给职工以及为职工支付的现金$CASHPAID_EMPLOYEE");
		sb.append(",支付的各项税费$CASHPAID_TAX");
		sb.append(",支付其他与经营活动有关的现金$CASHPAID_OTHER_OPERATING");
		sb.append(",经营活动现金流出小计$CASHOUTFLOW_OPERATING_ACTIVITIES");
		sb.append(",经营活动产生的现金流量净额$NETCASHFLOW_OPERATING_ACTIVITIES");
		sb.append(",收回投资收到的现金$CASHRECEIVED_INVESTMENT_WITHDRAWAL");
		sb.append(",取得投资收益收到的现金$CASHRECEIVED_RETURNS_INVESTMENTS");
		sb.append(",处置固定资产、无形资产和其他长期资产收回的现金净额$CASHRECEIVED_DISPOSAL_ASSETS");
		sb.append(",处置子公司及其他营业单位收到的现金净额$CASHRECEIVED_DISPOSAL_SUBSIDIARY");
		sb.append(",收到其他与投资活动有关的现金$CASHRECEIVED_OTHER_INVESTING");
		sb.append(",投资活动现金流入小计$CASHINFLOW_INVESTMENT_ACTIVITIES");
		sb.append(",购建固定资产、无形资产和其他长期资产支付的现金$CASHPAID_PURCHASING_ASSETS");
		sb.append(",投资支付的现金$CASHPAID_INVESTMENT");
		sb.append(",质押贷款净增加额$INCREASE_HYPOTHECATED_LOAN");
		sb.append(",取得子公司及其他营业单位支付的现金净额$CASHPAID_PURCHASING_SUBSIDIARY");
		sb.append(",支付其他与投资活动有关的现金$CASHPAID_OTHER_INVESTING");
		sb.append(",投资活动现金流出小计$CASHOUTFLOW_INVESTMENT_ACTIVITIES");
		sb.append(",投资活动产生的现金流量净额$NETCASHFLOW_INVESTMENT_ACTIVITIES");
		sb.append(",吸收投资收到的现金$CASHRECEIVED_EQUITY_INVESTMENTS");
		sb.append(",其中：子公司吸收少数股东投资收到的现金$CASHRECEIVED_MINORITYINVESTORS_SUBSIDIARIES");
		sb.append(",取得借款收到的现金$PROCEEDS_BORROWINGS");
		sb.append(",发行债券收到的现金$CASHRECEIVED_BONDS");
		sb.append(",收到其他与筹资活动有关的现金$CASHRECEIVED_OTHER_FINANCING");
		sb.append(",筹资活动现金流入小计$CASHINFLOW_FINANCING_ACTIVITIES");
		sb.append(",偿还债务支付的现金$CASHPAID_DEBTS");
		sb.append(",分配股利、利润或偿付利息支付的现金$CASHPAID_DISTRIBUTION_DIVIDENDS");
		sb.append(",其中：子公司支付给少数股东的股利、利润$DIVIDENDS_MINORITY_SHAREHOLDERS");
		sb.append(",支付其他与筹资活动有关的现金$CASHPAID_OTHER_FINANCING");
		sb.append(",筹资活动现金流出小计$CASHOUTFLOW_FINANCING_ACTIVITIES");
		sb.append(",筹资活动产生的现金流量净额$NETCASHFLOW_FINANCING_ACTIVITIES");
		sb.append(",汇率变动对现金及现金等价物的影响$EFFECT_FOREIGN_EXCHANGE");
		sb.append(",现金及现金等价物净增加额$INCREASE_CASH_CASHEQUIVALENTS");
		sb.append(",加:期初现金及现金等价物余额$INITIAL_CASH_CASHEQUIVALENTS");
		sb.append(",期末现金及现金等价物余额$ENDING_CASH_CASHEQUIVALENTS");

		mnv.addObject("cashFlowList", this.getReportVOList(sb));
	}
	
	private void prepareReportACFDiv(ModelAndView mnv) {
		StringBuilder sb = new StringBuilder();
		sb.append("净利润$NET_PROFIT");
		sb.append(",加:资产减值准备$PROVISION_DIMINUTION_STOCK");
		sb.append(",固定资产折旧、油气资产折耗、生产性生物资产折旧$DEPRECIATION_FIXED_ASSETS");
		sb.append(",无形资产摊销$AMORTISATION_INTANGIBLE_ASSETS");
		sb.append(",长期待摊费用摊销$INCREASE_LONG_DEFERRED_EXPENDITURE");
		sb.append(",处置固定资产、无形资产和其他长期资产的损失$LOSS_DISPOSAL_FIXEDASSETS");
		sb.append(",固定资产报废损失(收益以\"-\"号填列)$FIXEDASSETS_WRITTEN_OFF");
		sb.append(",公允价值变动损失(收益以\"-\"号填列)$PROFIT_LOSS_FAIRVALUE");
		sb.append(",财务费用(收益以\"-\"号填列)$FINANCIAL_EXPENSES");
		sb.append(",投资损失(收益以\"-\"号填列)$INVESTMENT_EARNINGS");
		sb.append(",递延所得税资产减少(增加以\"-\"号填列)$DEFERRED_TAX_INCOME");
		sb.append(",递延所得税负债增加(减少以\"-\"号填列)$DEFERRED_TAX_LIABILITIES");
		sb.append(",存货的减少(增加以\"-\"号填列)$DECREASE_INVENTORIES");
		sb.append(",经营性应收项目的减少(增加以\"-\"号填列)$DECREASE_OPERATING_RECEIVABLES");
		sb.append(",经营性应收项目的增加(减少以\"-\"号填列)$INCREASE_OPERATING_PAYABLES");
		sb.append(",其他$OTHERS");
		sb.append(",经营活动产生的现金流量净额$NETCASHFLOW_OPERATING_ACTIVITIES");
		sb.append(",债务转为资本$CONVERSION_DEBT_CAPITAL");
		sb.append(",一年内到期的可转换公司债务$RECLASSIFY_CONVERTIBLE_BONDS");
		sb.append(",融资租入固定资产$FIXEDASSETS_FINANCE_LEASES");
		sb.append(",现金的期末余额$ENDING_CASH");
		sb.append(",减:现金的期初余额$INITIAL_CASH");
		sb.append(",加:现金等价物的期末余额$ENDING_CASHEQUIVALENTS");
		sb.append(",减:现金等价物的期初余额$INITIAL_CASHEQUIVALENTS");
		sb.append(",现金及现金等价物净增加额$INCREASE_CASH_CASHEQUIVALENTS");

		mnv.addObject("ACFList", this.getReportVOList(sb));
	}
	
	private void prepareReportNFSDiv(ModelAndView mnv) {
		StringBuilder sb = new StringBuilder();
		sb.append("抵押资产额$MORTGAGE_ASSESTS");
		sb.append(",财务性利息支出$FINANCIAL_INTEREST");
		sb.append(",资本化利息支出$CAPITALIZED_INTEREST");
		sb.append(",主营业务收入年初数$INITIAL_MAIN_OPERATING_INCOME");
		sb.append(",主营业务收入年末数$ENDING_MAIN_OPERATING_INCOME");
		sb.append(",主营业务成本年初数$INITIAL_MAIN_OPERATING_COST");
		sb.append(",主营业务成本年末数$ENDING_MAIN_OPERATING_COST");
		sb.append(",经营性租赁总额$TOTAL_OPERATING_LEASE");
		sb.append(",融资性租赁总额$TOTAL_FINANCIAL_LEASE");
		sb.append(",股利支出$DIVIDENDS_SPENDING");
		sb.append(",非经常性损益$NONRECURRING_GAINS_LOSSES");
		sb.append(",对外担保金额$AMOUNT_GUARANTEE");
		sb.append(",一年内到期的担保金额$AMOUNT_GUARANTEE_Y1");
		sb.append(",一至两年到期的担保金额$AMOUNT_GUARANTEE_Y2");
		sb.append(",两至三年到期的担保金额$AMOUNT_GUARANTEE_Y23");
		sb.append(",三年以上应收账款$ACCOUNT_RECEIVABAL_YMORE3");
		sb.append(",三年以上其它应收款$OTHER_ACCOUNT_RECEIVABAL_YMORE3");
		sb.append(",三年以上应付账款$ACCOUNT_PAYABLE_YMORE3");
		sb.append(",三年以上其他应付款$OTHER_ACCOUNT_PAYABLE_YMORE3");
		sb.append(",坏账准备$BAD_DEBT_RESERVES");
		sb.append(",存货跌价准备$RESERVE_STOCK_DEPRECIATION");
		sb.append(",短期投资跌价准备$RESERVE_SHORT_INVESTMENT");
		sb.append(",长期投资减值准备$RESERVE_LONG_INVESTMENT");
		sb.append(",固定资产减值准备$RESERVE_FIXED_ASSETS");
		sb.append(",无形资产减值准备$RESERVE_INTANGIBLE_ASSETS");
		sb.append(",委托贷款减值准备$RESERVE_ENTRUSTED_LOANS");
		sb.append(",在建工程减值准备$RESERVE_CONSTRUCTION_PROGRESS");
		sb.append(",授信总额(集团口径)$CREDIT_GROUP");
		sb.append(",授信总额(母公司口径)$CREDIT_PARENTCOMPANY");
		sb.append(",尚未使用的授信额度(集团口径)$UNUSED_CREDIT_GROUP");
		sb.append(",尚未使用的授信额度(母公司口径)$UNUSED_CREDIT_PARENTCOMPANY");
		sb.append(",合同销售金$AMOUNT_SALE_CONTRACT");
		sb.append(",土地储备$LAND_BANKING");
		sb.append(",财务报表审计情况$AUDIT_FINANCIAL_STATEMENT");
		sb.append(",会计事务所$ACCOUNTING_FIRM");
		sb.append(",审计意见类型$AUDIT_OPINION");
		sb.append(",报告期内重大收购及出售资产吸收合并事项$PURCHASING_SELLING_MERGING");
		sb.append(",重大合同及其履行情况$CONTRACT_FULFILLING");
		sb.append(",重大关联交易说明$SIGNIFICANT_RELATED_TRANSACTIONS");
		sb.append(",担保事项$GUARANTEES");
		sb.append(",重大诉讼仲裁事项$MATERIAL_LITIGATION_ARBITRATION");
		sb.append(",大股东报告期内重大承诺事项$SIGNIFICANT_COMMITMENTS");
		sb.append(",资产负债表日后事项$EVENTS_AFTER_BALANCEDATE");
		sb.append(",其他重要事项$OTHER_IMPORTANT_MATTERS");

		mnv.addObject("NFSList", this.getReportVOList(sb));
	}

	private List<ReportVo> getReportVOList(StringBuilder sb) {
		String[] nameColumns = sb.toString().split(",");
		List<ReportVo> reportVoList = new ArrayList<>();
		for (int i=0; i<nameColumns.length; i++) {
			String[] nameColumn = nameColumns[i].split("\\$");
			ReportVo reportVo = new ReportVo(nameColumn[0], nameColumn[1]);
			reportVoList.add(reportVo);
		}
		return reportVoList;
	}*/

}
