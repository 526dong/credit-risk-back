package com.ccx.index.controller;

import com.alibaba.fastjson.JSON;
import com.ccx.businessmng.model.AbsEnterpriseReportType;
import com.ccx.businessmng.service.AbsEnterpriseReportTypeService;
import com.ccx.index.model.AbsIndex;
import com.ccx.index.model.AbsIndexFormula;
import com.ccx.index.model.AbsIndexModel;
import com.ccx.index.model.AbsModelElement;
import com.ccx.index.service.AbsIndexFormulaService;
import com.ccx.index.service.AbsIndexModelService;
import com.ccx.index.service.AbsIndexService;
import com.ccx.index.service.AbsModelElementService;
import com.ccx.util.MyRuntimeException;
import com.ccx.util.page.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Controller	
@RequestMapping("/businessMng")
public class AbsIndexController {

	Lock eleLock = new ReentrantLock();

	Lock indexLock = new ReentrantLock();

	private Logger logger = LogManager.getLogger(AbsIndexController.class);

	@Autowired
	private AbsIndexService absIndexService;

	@Autowired
	private AbsIndexModelService indexModelService;

	@Autowired
    private AbsModelElementService absModelElementService;

	@Autowired
	private AbsIndexFormulaService indexFormulaService;

	@Autowired
	private AbsEnterpriseReportTypeService reportTypeService;

	/*	
	 * @author 	
	 * @description	
	 * @date	
	*/
	@RequestMapping(value = "/indexMngIndex", method = RequestMethod.GET)
	public ModelAndView indexMngIndex(HttpServletRequest request) {
		ModelAndView mnv = new ModelAndView("businessMng/indexMngIndex");

		try {
			List<AbsEnterpriseReportType> typeList = reportTypeService.getList();
			mnv.addObject("typeList", JSON.toJSONString(typeList));
		} catch (Exception e) {
			logger.error("指标首页：", e);
		}
		return mnv;
	}

	/*
       * @author
       * @description	查看模型列表
       * @date
       */
	@RequestMapping(value = "/indexMngModelList", method = RequestMethod.POST)
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
	 * @description	因素首页
	 * @date
	*/
	@RequestMapping(value = "/elementIndex", method = RequestMethod.GET)
	public ModelAndView elementIndex(@RequestParam(required = true) Integer modelId) {
		ModelAndView mnv = new ModelAndView("businessMng/elementIndex");

		try {
			AbsIndexModel model = indexModelService.getById(modelId);
			mnv.addObject("modelId", modelId);
			mnv.addObject("modelName", model.getName());
		} catch (Exception e) {
			logger.error("因素首页:", e);
		}
		return mnv;
	}

    /*
     * @author
     * @description	查看因素列表
     * @date
    */
    @RequestMapping(value = "/elementList", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> elementList(Integer pageNo, Integer pageSize, @RequestParam(required = true) Integer modelId) {
        Map<String, Object> resultMap = new HashMap<>();
        Page<AbsModelElement> page = new Page<AbsModelElement>();
        Map<String, Object> params = new HashMap<String, Object>();

		try {
			params.put("modelId", modelId);
			page.setParams(params);
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
            page = absModelElementService.getPageList(page);

            resultMap.put("code", 200);
            resultMap.put("page", page);
        } catch (Exception e) {
            logger.error("查看因素列表:", e);
            resultMap.put("code", 500);
        }
        return resultMap;
    }

	/*
	 * @author
	 * @description	因素页面新增编辑详细
	 * @date
	*/
	@RequestMapping(value = "/elementAddOrEditOrShow", method = RequestMethod.GET)
	public ModelAndView elementAddOrEditOrShow(
			@RequestParam(required = true) Integer id,
			@RequestParam(required = true) Integer modelId,
			@RequestParam(required = true) String method) {
		ModelAndView mnv = new ModelAndView("businessMng/elementAddOrEditOrShow");

		try {
			if (-1 != id) {
				AbsModelElement element = absModelElementService.getById(id);
				mnv.addObject("element", element);
			}

			AbsIndexModel model = indexModelService.getById(modelId);
			mnv.addObject("model", model);
			mnv.addObject("method", method);
		} catch (Exception e) {
			logger.error("因素页面新增编辑详细:", e);
		}
		return mnv;
	}

	/*
	 * @author
	 * @description	保存更新因素
	 * @date
	*/
	@RequestMapping(value = "/elementSaveOrUpdate", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> elementSaveOrUpdate(AbsModelElement element) {
		Map<String, Object> resultMap = new HashMap<>();

		try {
			resultMap.put("code", 200);
			if (-1 == element.getId()) {
				eleLock.lock();
				if (absModelElementService.checkCode(element.getModelId(), element.getCode()) > 0) {
					resultMap.put("code", 400);
					resultMap.put("msg", "因素编号已被占用！");
				} else {
					element.setModelId(element.getModelId());
					element.setId(null);
					absModelElementService.save(element);
				}
			} else {
				eleLock.lock();
				AbsModelElement elementBak = absModelElementService.getById(element.getId());

				if (elementBak.getCode().equals(element.getCode())) {
					//修改的code和原有相同
					if (absModelElementService.checkCode(element.getModelId(), element.getCode()) <= 1) {
						absModelElementService.updateSelective(element);
					} else {
						resultMap.put("code", 400);
						resultMap.put("msg", "因素编号已被占用！");
					}
				} else {
					//修改的code和原有不同
					if (absModelElementService.checkCode(element.getModelId(), element.getCode()) > 0) {
						resultMap.put("code", 400);
						resultMap.put("msg", "因素编号已被占用！");
					} else {
						element.setModelId(element.getModelId());
						absModelElementService.updateSelective(element);
					}
				}
			}
		} catch (Exception e) {
			logger.error("保存因素:", e);
			resultMap.put("code", 500);
		}

		eleLock.unlock();
		return resultMap;
	}

	/*
	 * @author
	 * @description	更新因素状态
	 * @date
	*/
	@RequestMapping(value = "/elementUpdateState", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> elementUpdateState(@RequestParam(required = true) Integer id,
												  @RequestParam(required = true) Integer state) {
		Map<String, Object> resultMap = new HashMap<>();

		try {
			resultMap.put("code", 200);
			absModelElementService.UpdateState(id, state);
		} catch (Exception e) {
			logger.error("更新因素状态:", e);
			resultMap.put("code", 500);
		}
		return resultMap;
	}

	/*
	 * @author
	 * @description	指标首页
	 * @date
	*/
	@RequestMapping(value = "/indexIndex", method = RequestMethod.GET)
	public ModelAndView indexIndex(@RequestParam(required = true) Integer eleId, @RequestParam(required = true) Integer modelId) {
		ModelAndView mnv = new ModelAndView("businessMng/indexIndex");
		try {
			AbsIndexModel model = indexModelService.getById(modelId);
			AbsModelElement element = absModelElementService.getById(eleId);
			mnv.addObject("eleId", eleId);
			mnv.addObject("model", model);
			mnv.addObject("element", element);
		} catch (Exception e) {
			logger.error("因素指标首页:", e);
		}
		return mnv;
	}

	/*
   * @author
   * @description	查看指标列表
   * @date
  */
	@RequestMapping(value = "/indexList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> indexList(Integer pageNo, Integer pageSize, @RequestParam(required = true) Integer eleId) {
		Map<String, Object> resultMap = new HashMap<>();
		Page<AbsIndex> page = new Page<AbsIndex>();
		Map<String, Object> params = new HashMap<String, Object>();

		try {
			params.put("eleId", eleId);
			page.setParams(params);
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			page = absIndexService.getPageList(page);

			resultMap.put("code", 200);
			resultMap.put("page", page);
		} catch (Exception e) {
			logger.error("查看指标列表:", e);
			resultMap.put("code", 500);
		}
		return resultMap;
	}

	/*
	 * @author
	 * @description	新建编辑指标页面
	 * @date
	*/
	@RequestMapping(value = "/indexAddOrEdit", method = RequestMethod.GET)
	public ModelAndView indexAddOrEdit(@RequestParam(required = true) Integer id,
											 @RequestParam(required = true) Integer modelId,
											 @RequestParam(required = true) String method,
											 @RequestParam(required = true) Integer eleId) {
		ModelAndView mnv = new ModelAndView("businessMng/indexAddOrEdit");

		try {
			if (-1 != id) {
				AbsIndex index = absIndexService.getById(id);
				mnv.addObject("index", index);
			}
			AbsIndexModel model = indexModelService.getById(modelId);
			AbsModelElement element = absModelElementService.getById(eleId);
			mnv.addObject("model", model);
			mnv.addObject("element", element);
			mnv.addObject("method", method);
		} catch (Exception e) {
			logger.error("新建编辑指标页面:", e);
		}
		return mnv;
	}

	/*
	 * @author
	 * @description	查看指标页面
	 * @date
	*/
	@RequestMapping(value = "/indexShow", method = RequestMethod.GET)
	public ModelAndView indexShow(@RequestParam(required = true) Integer id,
								  @RequestParam(required = true) Integer modelId,
								  @RequestParam(required = true) String method,
								  @RequestParam(required = true) Integer eleId) {
		ModelAndView mnv = new ModelAndView("businessMng/indexShow");

		try {
			AbsIndex index = absIndexService.getById(id);
			mnv.addObject("index", index);
			AbsIndexModel model = indexModelService.getById(modelId);
			AbsModelElement element = absModelElementService.getById(eleId);
			mnv.addObject("model", model);
			mnv.addObject("element", element);
		} catch (Exception e) {
			logger.error("查看指标页面:", e);
		}
		return mnv;
	}

	/*
	 * @author
	 * @description	指标搜索公式
	 * @date
	*/
	@RequestMapping(value = "/indexSearchFormula", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> indexSearchFormula(String formulaName, @RequestParam(required = true) Integer reportTypeId) {
		Map<String, Object> resultMap = new HashMap<>();

		try {
			List<AbsIndexFormula> list = indexFormulaService.getbyLikeName(formulaName, reportTypeId);
			resultMap.put("list", list);
			resultMap.put("code", 200);
		} catch (Exception e) {
			logger.error("指标搜素公式:", e);
			resultMap.put("code", 500);

		}
		return resultMap;
	}

	/*
	 * @author
	 * @description	保存指标
	 * @date
	*/
	@RequestMapping(value = "/indexSaveOrUpdate", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> indexSaveOrUpdate(@RequestParam(required = true) String codes,
										 String ruleIds,
										 String delIds,
										 String valueMins,
										 String values,
										 String valueMaxs,
										 String scores,
										 String degrees,
										 @RequestParam(required = true) Integer eleId,
										 @RequestParam(required = true) Integer modelId,
										 AbsIndex index) {
		Map<String, Object> resultMap = new HashMap<>();

		try {
			resultMap.put("code", 200);
			if (-1 == index.getId()) {
				indexLock.lock();
				if (absIndexService.checkIndexCode(modelId, index.getIndexCode()) > 0) {
					resultMap.put("code", 400);
					resultMap.put("msg", "指标编号已被占用！");
				} else {
					absIndexService.saveOrUpdatIndexAndRule(codes, ruleIds, delIds, valueMins, values, valueMaxs, scores, degrees, eleId, index);
				}
			} else {
				indexLock.lock();
				AbsIndex indexBak = absIndexService.getById(index.getId());

				if (indexBak.getIndexCode().equals(index.getIndexCode())) {
					//code相同
					if (absIndexService.checkIndexCode(modelId, index.getIndexCode()) <= 1) {
						absIndexService.saveOrUpdatIndexAndRule(codes, ruleIds, delIds, valueMins, values, valueMaxs, scores, degrees, eleId, index);
					} else {
						resultMap.put("code", 400);
						resultMap.put("msg", "指标编号已被占用！");
					}
				} else {
					//cdoe不同
					if (absIndexService.checkIndexCode(modelId, index.getIndexCode()) > 0) {
						resultMap.put("code", 400);
						resultMap.put("msg", "指标编号已被占用！");
					} else {
						absIndexService.saveOrUpdatIndexAndRule(codes, ruleIds, delIds, valueMins, values, valueMaxs, scores, degrees, eleId, index);
					}
				}
			}
		} catch (Exception e) {
			if (e instanceof MyRuntimeException) {
				resultMap.put("code", 400);
				resultMap.put("msg", e.getMessage());
			} else {
				logger.error("保存指标:", e);
				resultMap.put("code", 500);
			}
		}

		indexLock.unlock();
		return resultMap;
	}

	/*
	 * @author
	 * @description	更新指标状态
	 * @date
	*/
	@RequestMapping(value = "/indexUpdateState", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> indexUpdateState(@RequestParam(required = true)Integer id, @RequestParam(required = true) Integer state) {
		Map<String, Object> resultMap = new HashMap<>();

		try {
			resultMap.put("code", 200);
			AbsIndex index = new AbsIndex();
			index.setId(id);
			index.setState(state);
			absIndexService.updateSelective(index);
		} catch (Exception e) {
			logger.error("更新状态失败:", e);
			resultMap.put("code", 500);
		}

		return resultMap;
	}

}
