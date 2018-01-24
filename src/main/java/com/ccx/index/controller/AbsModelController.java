package com.ccx.index.controller;

import com.alibaba.fastjson.JSON;
import com.ccx.businessmng.model.AbsEnterpriseReportType;
import com.ccx.businessmng.service.AbsEnterpriseReportTypeService;
import com.ccx.enterprise.model.EnterpriseIndustry;
import com.ccx.enterprise.model.IndustryShow;
import com.ccx.enterprise.service.EnterpriseIndustryService;
import com.ccx.index.model.AbsIndexModel;
import com.ccx.index.service.AbsIndexModelService;
import com.ccx.util.MyRuntimeException;
import com.ccx.util.page.Page;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller	
@RequestMapping("/businessMng")
public class AbsModelController {

	private Logger logger = LogManager.getLogger(AbsModelController.class);

	@Autowired
	private AbsIndexModelService indexModelService;

	@Autowired
	private EnterpriseIndustryService enterpriseIndustryService;

	@Autowired
	private AbsEnterpriseReportTypeService reportTypeService;

	/*
	 * @author
	 * @description	模型列表
	 * @date
	*/
	@RequestMapping(value = "/modelIndex", method = RequestMethod.GET)
	public ModelAndView modelIndex() {
		ModelAndView mnv = new ModelAndView("businessMng/modelIndex");

		try {
			List<AbsEnterpriseReportType> typeList = reportTypeService.getList();
			mnv.addObject("typeList", JSON.toJSONString(typeList));
		} catch (Exception e) {
			logger.error("模型列表：", e);
		}

		return mnv;
	}

	/*
   * @author
   * @description	查看模型列表
   * @date
   */
	@RequestMapping(value = "/modelList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> modelList(Integer pageNo, Integer pageSize, String name) {
		Map<String, Object> resultMap = new HashMap<>();
		Page<AbsIndexModel> page = new Page<AbsIndexModel>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);

		try {
			page.setParams(params);
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			page = indexModelService.getPageList(page);
			List<AbsIndexModel> modelList = page.getRows();

			for (AbsIndexModel model: modelList) {
				StringBuilder sb = new StringBuilder();

				List<EnterpriseIndustry> industryList0 = enterpriseIndustryService.getListByModelIdAndType(model.getId(), 0);
				for (EnterpriseIndustry industry: industryList0) {
					sb.append(industry.getName()).append("-大中型; ");
				}

				List<EnterpriseIndustry> industryList1 = enterpriseIndustryService.getListByModelIdAndType(model.getId(), 1);
				for (EnterpriseIndustry industry: industryList1) {
					sb.append(industry.getName()).append("-小微型; ");
				}

				if (sb.length() == 0) {
					sb.append("未匹配");
				}
				model.setIndustryNameAndType(sb.toString());
			}

			resultMap.put("code", 200);
			resultMap.put("page", page);
		} catch (Exception e) {
			logger.error("模型列表:", e);
			resultMap.put("code", 500);
		}
		return resultMap;
	}

	/*
	 * @author
	 * @description	模型单个匹配
	 * @date
	*/
	@RequestMapping(value = "/modelMatch", method = RequestMethod.GET)
	public ModelAndView modelMatch(@RequestParam(required = true) Integer id) {
		ModelAndView mnv = new ModelAndView("businessMng/modelMatch");

		try {
			prepareMatchView(id, mnv);
			List<AbsEnterpriseReportType> typeList = reportTypeService.getList();
			mnv.addObject("reportTypeList", typeList);
		} catch (Exception e) {
			logger.error("模型单个匹配", e);
		}

		return mnv;
	}

	/*
	 * @author
	 * @description	模型批量匹配
	 * @date
	*/
	@RequestMapping(value = "/modelMatchBatch", method = RequestMethod.GET)
	public ModelAndView modelMatchBatch() {
		ModelAndView mnv = new ModelAndView("businessMng/modelMatchBatch");
		return mnv;
	}

	/*
	* @author
	* @description	模糊查询模型列表
	* @date
	*/
	@RequestMapping(value = "/modelSearch", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> modelSearch(@RequestParam(required = true) String name) {
		Map<String, Object> resultMap = new HashMap<>();
		try {

			List<AbsIndexModel> modelList = indexModelService.getByLikeName(name);
			resultMap.put("list", modelList);
			resultMap.put("code", 200);
		} catch (Exception e) {
			logger.error("模糊查询模型列表:", e);
			resultMap.put("code", 500);
		}
		return resultMap;
	}

	/*
	* @author
	* @description	查看模型行业列表列表
	* @date
	*/
	@RequestMapping(value = "/modelIndustryList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> modelIndustryList(Integer pageNo,
                                                 Integer pageSize,
                                                 String name,
                                                 Integer reportTypeId,
                                                 String modelFlag) {
		Map<String, Object> resultMap = new HashMap<>();
		Page<IndustryShow> page = new Page<IndustryShow>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		params.put("reportTypeId", reportTypeId);

		try {
			page.setParams(params);
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			page = enterpriseIndustryService.getPageList(page);
			List<IndustryShow> showListAll = new ArrayList<>();

			for (IndustryShow industry: page.getRows()) {
				industry.setType(0);
				showListAll.add(industry);
				//IndustryShow industry2 = new IndustryShow(industry.getId2(), industry.getName1(), industry.getName2(), industry.getModelId0(), industry.getModelId1(), 1);
				//showListAll.add(industry2);
			}

			if ("true".equals(modelFlag)) {
				for (IndustryShow industry: showListAll) {
					Integer modelId = null;

					if (industry.getType() == 0) {
						modelId = industry.getModelId0();
					} else if (industry.getType() == 1) {
						modelId = industry.getModelId1();
					}
					if (null != modelId) {
						AbsIndexModel model = indexModelService.getById(modelId);
						industry.setModelName(model.getName());
					} else {
						industry.setModelName("");
					}

				}
			}
			resultMap.put("code", 200);
			page.setRows(showListAll);
			resultMap.put("page", page);
		} catch (Exception e) {
			logger.error("模型行业列表:", e);
			resultMap.put("code", 500);
		}
		return resultMap;
	}

	/*
	 * @author
	 * @description	保存模型匹配
	 * @date
	*/
	@RequestMapping(value = "/modelMatchSave", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> modelMatchSave(@RequestParam(required = true) String id,
											  @RequestParam(required = true) Integer reportTypeId,
											  String industryId,
											  String type,
											  String delId,
											  String delType,
											  Boolean batchFlag) {
		Map<String, Object> resultMap = new HashMap<>();

		try {
			resultMap.put("code", 200);
			indexModelService.saveMatch(id, reportTypeId, industryId, type, delId, delType ,batchFlag);
		} catch (Exception e) {
			if (e instanceof MyRuntimeException) {
				resultMap.put("code", 400);
				resultMap.put("msg", e.getMessage());
			} else {
				logger.error("保存模型匹配:", e);
				resultMap.put("code", 500);
			}
		}

		return resultMap;
	}

	/*
	 * @author
	 * @description	模型新增修改展示页面
	 * @date
	*/
	@RequestMapping(value = "/modelAddOrEdit", method = RequestMethod.GET)
	public ModelAndView modelAddOrEdit(@RequestParam(required = true) Integer id,
											 @RequestParam(required = true) String method) {
		ModelAndView mnv = new ModelAndView("businessMng/modelAddOrEdit");

		try {
			if (-1 != id) {
				AbsIndexModel model = indexModelService.getById(id);
				mnv.addObject("model", model);
			}
			mnv.addObject("method", method);
		} catch (Exception e) {
			logger.error("模型新增修改页面:", e);
		}
		return mnv;
	}

	/*
	 * @author
	 * @description	模型新增修改展示页面
	 * @date
	*/
	@RequestMapping(value = "/modelShow", method = RequestMethod.GET)
	public ModelAndView modelShow(@RequestParam(required = true) Integer id,
											 @RequestParam(required = true) String method) {
		ModelAndView mnv = new ModelAndView("businessMng/modelShow");

		try {
			prepareMatchView(id, mnv);
			mnv.addObject("method", method);
		} catch (Exception e) {
			logger.error("模型展示页面:", e);
		}
		return mnv;
	}

	/*
	 * @author
	 * @description	保存模型
	 * @date
	*/
	@RequestMapping(value = "/modelSaveOrUpdate", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> modelSaveOrUpdate(HttpServletRequest request,
												 @RequestParam(required = true) String codes,
												 @RequestParam(required = true)  String ruleIds,
												 @RequestParam(required = true)  String delIds,
												 @RequestParam(required = true)  String valueMins,
												 @RequestParam(required = true)  String valueMaxs,
												 @RequestParam(required = true)  String scores,
												 @RequestParam(required = true)  String degrees,
												 @RequestParam(required = true)  Integer id,
												 @RequestParam(required = true) String name) {
		Map<String, Object> resultMap = new HashMap<>();

		try {
			resultMap.put("code", 200);
			indexModelService.saveOrUpdateModelAndRule(request, codes, ruleIds, delIds, valueMins, valueMaxs, scores, degrees, id, name);
		} catch (Exception e) {
			if (e instanceof MyRuntimeException ) {
				resultMap.put("code", 400);
				resultMap.put("msg", e.getMessage());
			} else if (e instanceof DuplicateKeyException) {
				resultMap.put("code", 400);
				resultMap.put("msg", "评分卡名称已被占用！");
			} else {
				logger.error("保存模型:", e);
				resultMap.put("code", 500);
			}
		}

		return resultMap;
	}

	/*
	 * @author
	 * @description	更新模型状态
	 * @date
	*/
	/*@RequestMapping(value = "/modelUpdateState", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> modelUpdateState(@RequestParam(required = true)Integer id,
												@RequestParam(required = true)Integer backId,
												@RequestParam(required = true)Integer state,
												@RequestParam(required = true) Integer type) {
		Map<String, Object> resultMap = new HashMap<>();

		try {
			resultMap.put("code", 200);
			AbsIndexModel model = new AbsIndexModel();
			model.setId(id);
			model.setState(state);
			lock.lock();
			int num = indexModelService.checkEnableModelNumByInsustryIdAndType(backId, type);
			if (num >= 1) {
				resultMap.put("code", 400);
				resultMap.put("msg", "适合当前企业规模的评分卡已有一个处于开启状态！");
			} else {
				indexModelService.updateSelective(model);
			}
		} catch (Exception e) {
			logger.error("更新状态失败:", e);
			resultMap.put("code", 500);
		}

		lock.unlock();
		return resultMap;
	}*/

	/*
	 * @author
	 * @description	删除模型
	 * @date
	*/
	@RequestMapping(value = "/modelDel", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> modelDel(@RequestParam(required = true)Integer id) {
		Map<String, Object> resultMap = new HashMap<>();

		try {
			resultMap.put("code", 200);
			AbsIndexModel model = new AbsIndexModel();
			model.setId(id);
			indexModelService.deleteModelAndRule(model);
		} catch (Exception e) {
			logger.error("删除模型失败:", e);
			resultMap.put("code", 500);
		}
		return resultMap;
	}

	private void prepareMatchView(Integer id, ModelAndView mnv) {
		AbsIndexModel model = indexModelService.getById(id);
		//二级行业列表
		List<EnterpriseIndustry> industryList = enterpriseIndustryService.getListByModelId(model.getId());
		if (industryList.size() > 0) {
			List<Integer> idList = new ArrayList<>();
			for (EnterpriseIndustry industry: industryList) {
				idList.add(industry.getId());
			}
			List<IndustryShow> showList = enterpriseIndustryService.getOneTwoListByIdsAndModelId(idList, model.getId());
			mnv.addObject("list", showList);
		}

		mnv.addObject("model", model);
	}

}
