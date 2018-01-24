package com.ccx.dictionary.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ccx.system.model.UserBg;
import com.ccx.util.ControllerUtil;
import com.ccx.util.JsonResult;
import com.ccx.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.ccx.dictionary.model.EnterpriseNature;
import com.ccx.dictionary.model.RateInstitution;
import com.ccx.dictionary.model.RateResult;
import com.ccx.dictionary.service.EnterpriseNatureService;
import com.ccx.dictionary.service.RateInstitutionService;
import com.ccx.dictionary.service.RateResultService;
import com.ccx.system.controller.BaseController;
import com.ccx.util.page.Page;


/**
 * 字典管理
 * @author xzd
 * @date 2017/7/3
 */
@Controller
@RequestMapping(value="/dictionary")
public class DictionaryController extends BaseController{
    private static final Logger logger = LogManager.getLogger(DictionaryController.class);

    /*企业性质*/
    @Autowired
    EnterpriseNatureService natureService;
    
    /*评级机构*/
    @Autowired
    RateInstitutionService rateInstitutionService;
    
    /*评级结果*/
    @Autowired
    RateResultService rateResultService;
    
	/**
	 * 企业性质列表页
	 */
	@GetMapping("/list")
	public String list(){
		return "dictionaryMng/dictionaryList";
	}
	
	/**
	 * 查询企业性质
	 * @return
	 */
	@ResponseBody
	@PostMapping("/findAllNature")
	public JsonResult findAllNature(Integer pageNo, Integer pageSize){
		//分页
		Page<EnterpriseNature> page = new Page<EnterpriseNature>();
		//参数
    	Map<String,Object> params = new HashMap<String,Object>();
    	
		//查询企业性质列表
		try {
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			page.setParams(params);
			
			page = natureService.findAll(page);

			//成功标识
			return JsonResult.ok(page);
		} catch (Exception e) {
			logger.error("查询企业性质数据列表失败:", e);
			//失败标识
			return JsonResult.error("查询企业性质数据列表失败！");
		}
	}

	/**
	 * 验证企业性质名称的唯一性
	 * @return
	 */
	@PostMapping("/validateNatureName")
	@ResponseBody
	public JsonResult validateNatureName(HttpServletRequest request,
		@RequestParam(required = true) Integer natureId, @RequestParam(required = true) String name){
		int count = 0;
		boolean flag = true;

		try {
			count = natureService.findByName(name.trim());
			//更新
			if (natureId > 0) {
				if (count == 1) {
					EnterpriseNature rateData = natureService.findById(natureId);
					//更新时，数据库里查询到一个，说明是当前输入的，可以保存
					if (name.equals(rateData.getName())) {
						//不是当前查询到的，重复，不保存
						flag = true;
					}
				} else if (count > 1) {
					flag = true;
				}
			} else {
				//新增
				if (count > 0) {
					flag = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("通过企业性质名称查询企业性质列表失败", e);
			//失败标识
			return JsonResult.error("通过企业性质名称查询企业性质列表失败！");
		}

		if (flag) {
			return JsonResult.ok(null);
		} else {
			return JsonResult.error("企业性质名称已存在，请重新添加！");
		}
	}
	
	/**
	 * 添加或更新企业性质
	 * @param request
	 */
	@GetMapping("/saveOrUpdateNature")
	public ModelAndView saveOrUpdateNature(HttpServletRequest request){
		//企业性质id
		String id = request.getParameter("id");

		if (StringUtils.isEmpty(id)) {
			//更新
			EnterpriseNature nature = natureService.findById(Integer.parseInt(id));
			request.setAttribute("nature", nature);
		}
		
		return new ModelAndView("dictionaryMng/natureSaveOrUpdate");
	}
	
	/**
	 * 添加或更新企业性质-数据库操作
	 * @param request
	 */
	@ResponseBody
	@PostMapping("/doSaveOrUpdateNature")
	public JsonResult doSaveOrUpdateNature(HttpServletRequest request, Integer id, String name){
		EnterpriseNature nature = new EnterpriseNature();

		//获取当前登录用户信息
		UserBg user = (UserBg) ControllerUtil.getSessionUser(request);
		//添加创建人
		if (null != user) {
			//添加创建人
			nature.setCreatorName(user.getLoginName());
		}

		boolean addFlag = true;
		boolean updateFlag = true;

    	//通过是否有id来判断添加更新
    	if (id != 0) {
    		try {
    			nature.setId(id);
    			nature.setName(name);

				natureService.update(nature);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("企业性质更新失败:", e);
				updateFlag = false;

			}
		} else {
			//添加创建时间
        	nature.setCreateDate(new Date());
        	
    		try {
				nature.setId(null);
				nature.setName(name);

    			natureService.insert(nature);
    		} catch (Exception e) {
    			e.printStackTrace();
    			logger.error("企业性质添加失败:", e);
				addFlag = false;
    		}
		}

		if (addFlag && updateFlag) {
			//成功标识
			return JsonResult.ok(null);
		} else {
    		if (!addFlag) {
				return JsonResult.error("企业性质添加失败！");
			} else {
				return JsonResult.error("企业性质更新失败！");
			}
		}
	}
	
	/**
	 * 删除企业性质
	 * @param request
	 */
	@ResponseBody
	@PostMapping("/deleteNature")
	public JsonResult deleteNature(HttpServletRequest request){
		//判空处理
		String id = request.getParameter("id");

		if (!StringUtils.isEmpty(id)) {
			try {
				natureService.deleteById(Integer.parseInt(id));
				return JsonResult.ok(null);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				logger.error("企业性质删除失败:", e);
				return JsonResult.error("企业性质删除失败！");
			}
		} else {
			return JsonResult.error("企业性质id为空！");
		}
	}
	
	/**
	 * 评级机构列表页
	 */
	@GetMapping("/rateInstitution")
	public String rateInstitution(){
		return "dictionaryMng/rateInstitutionList";
	}
	
	/**
	 * 查询评级机构
	 * @return
	 */
	@ResponseBody
	@PostMapping("/findAllRateInstitution")
	public JsonResult findAllRateInstitution(Integer pageNo, Integer pageSize){
		//分页
		Page<RateInstitution> page = new Page<RateInstitution>();
    	
		//参数
		Map<String,Object> params = new HashMap<String,Object>();
    	
		//查询企业性质列表
		try {
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			page.setParams(params);
			
			//查询评级机构列表
			page = rateInstitutionService.getPageList(page);

			//成功标识
			return JsonResult.ok(page);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询评级机构数据列表失败:", e);
			//失败标识
			return JsonResult.error("查询评级机构数据列表失败！");
		}
	}

	/**
	 * 验证企业性质名称的唯一性
	 * @return
	 */
	@PostMapping("/validateInsName")
	@ResponseBody
	public JsonResult validateInsName(HttpServletRequest request,
			@RequestParam(required = true) Integer insId, @RequestParam(required = true) String name){
		int count = 0;

		boolean flag = true;

		try {
			count = rateInstitutionService.findCountByName(name.trim());
			//更新
			if (insId > 0) {
				if (count == 1) {
					RateInstitution rateInstitution= rateInstitutionService.getById(insId);
					//更新时，数据库里查询到一个，说明是当前输入的，可以保存
					if (name.equals(rateInstitution.getName())) {
						//不是当前查询到的，重复，不保存
						flag = false;
					}
				} else if (count > 1) {
					flag = false;
				}
			} else {
				//新增
				if (count > 0) {
					flag = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("通过评级机构名称查询机构列表失败:", e);
			return JsonResult.error("通过评级机构名称查询机构列表失败！");
		}

		if (flag) {
			//成功标识
			return JsonResult.ok(null);
		} else {
			//失败标识
			return JsonResult.error("评级机构名称已存在，请重新添加！");
		}
	}

	/**
	 * 验证企业性质名称的唯一性
	 * @return
	 */
	@PostMapping("/validateInsCode")
	@ResponseBody
	public JsonResult validateInsCode(HttpServletRequest request,
			@RequestParam(required = true) Integer insId, @RequestParam(required = true) String code){
		int count = 0;

		boolean flag = true;

		try {
			count = rateInstitutionService.findByCode(code.trim());
			//更新
			if (insId > 0) {
				if (count == 1) {
					RateInstitution rateInstitution= rateInstitutionService.getById(insId);
					//更新时，数据库里查询到一个，说明是当前输入的，可以保存
					if (code.equals(rateInstitution.getCode())) {
						//不是当前查询到的，重复，不保存
						flag = false;
					}
				} else if (count > 1) {
					flag = false;
				}
			} else {
				//新增
				if (count > 0) {
					flag = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("通过统一信用代码查询机构列表失败:", e);
			//失败标识
			return JsonResult.error("通过统一信用代码查询机构列表失败！");
		}

		if (flag) {
			//成功标识
			return JsonResult.ok(null);
		} else {
			//失败标识
			return JsonResult.error("统一信用代码已存在，请重新添加！");
		}
	}
	
	/**
	 * 添加或更新评级机构
	 * @param request
	 */
	@GetMapping("/saveOrUpdateRateInstitution")
	public ModelAndView saveOrUpdateRateInstitution(HttpServletRequest request){
		//评级机构id
		String id = request.getParameter("id");

		if (!StringUtils.isEmpty(id)) {
			//更新
			RateInstitution rateInstitution = rateInstitutionService.getById(Integer.parseInt(id));
			
			request.setAttribute("rateInstitution", rateInstitution);
		}
		
		return new ModelAndView("dictionaryMng/rateInstitutionSaveOrUpdate");
	}
	
	/**
	 * 添加或更新评级机构-数据库操作
	 * @param request
	 */
	@ResponseBody
	@PostMapping("/doSaveOrUpdateRateInstitution")
	public JsonResult doSaveOrUpdateRateInstitution(HttpServletRequest request, Integer id, String name, String code){
		RateInstitution rateInstitution = new RateInstitution();

		//获取当前登录用户信息
		UserBg user = (UserBg) ControllerUtil.getSessionUser(request);
		//添加创建人
		if (null != user) {
			//添加创建人
			rateInstitution.setCreatorName(user.getLoginName());
		}

		boolean addFlag = true;
		boolean updateFlag = true;

    	//通过是否有id来判断添加更新
    	if (id !=0) {
    		try {
    			rateInstitution.setId(id);
    			rateInstitution.setName(name);
				rateInstitution.setCode(code);

    			rateInstitutionService.update(rateInstitution);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("评级机构更新失败！");
				updateFlag = false;
			}
		} else {
			//添加创建时间
			rateInstitution.setCreateTime(new Date());
        	
    		try {
				rateInstitution.setId(null);
				rateInstitution.setName(name);
				rateInstitution.setCode(code);

    			rateInstitutionService.save(rateInstitution);
    		} catch (Exception e) {
    			e.printStackTrace();
    			logger.error("评级机构添加失败！");
				addFlag = false;
    		}
		}

		if (addFlag && updateFlag) {
			//成功标识
			return JsonResult.ok(null);
		} else {
			if (!addFlag) {
				return JsonResult.error("评级机构添加失败！");
			} else {
				return JsonResult.error("评级机构更新失败！");
			}
		}
	}
	
	/**
	 * 删除评级机构
	 * @param request
	 */
	@ResponseBody
	@PostMapping("/deleteRateInstitution")
	public JsonResult deleteRateInstitution(HttpServletRequest request){
		//判空处理
		String id = request.getParameter("id");

		if (!StringUtils.isEmpty(id)) {
			try {
				rateInstitutionService.deleteById(Integer.parseInt(id));
				//成功标识
				return JsonResult.ok(null);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				logger.error("评级机构删除失败:", e);
				//失败标识
				return JsonResult.error("评级机构删除失败!");
			}
		} else {
			return JsonResult.error("评级机构id为空!");
		}
	}
	
	/**
	 * 评级结果列表页
	 */
	@GetMapping("/rateResult")
	public String rateResult(){
		return "dictionaryMng/rateResultList";
	}
	
	/**
	 * 查询评级结果
	 * @return
	 */
	@ResponseBody
	@PostMapping("/findAllRateResult")
	public JsonResult findAllRateResult(Integer pageNo, Integer pageSize){
		//分页
		Page<RateResult> page = new Page<RateResult>();
    	
		//参数
		Map<String,Object> params = new HashMap<String,Object>();
    	
		//查询企业性质列表
		try {
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			page.setParams(params);
			
			//查询评级结果列表
			page = rateResultService.getPageList(page);

			//成功标识
			return JsonResult.ok(page);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询评级结果数据列表失败:", e);
			//失败标识
			return JsonResult.error("查询评级结果数据列表失败！");
		}
	}
	
	/**
	 * 添加或更新评级结果
	 * @param request
	 */
	@GetMapping("/saveOrUpdateRateResult")
	public ModelAndView saveOrUpdateRateResult(HttpServletRequest request){
		//评级结果id
		String id = request.getParameter("id");

		if (!StringUtils.isEmpty(id)) {
			//更新
			RateResult rateResult = rateResultService.getById(Integer.parseInt(id));
			request.setAttribute("rateResult", rateResult);
		}
		
		return new ModelAndView("dictionaryMng/rateResultSaveOrUpdate");
	}
	
	/**
	 * 添加或更新评级结果-数据库操作
	 * @param request
	 */
	@ResponseBody
	@PostMapping("/doSaveOrUpdateRateResult")
	public JsonResult doSaveOrUpdateRateResult(HttpServletRequest request, Integer id, String name){
		RateResult rateResult = new RateResult();

		boolean addFlag = true;
		boolean updateFlag = true;

    	//通过是否有id来判断添加更新
    	if (id != 0) {
    		try {
    			rateResult.setId(id);
    			rateResult.setName(name);

    			rateResultService.update(rateResult);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("评级结果更新成功！");
				updateFlag = false;
			}
		} else {
			//添加创建时间
			rateResult.setCreateTime(new Date());
        	
    		try {
				rateResult.setId(null);
				rateResult.setName(name);

    			rateResultService.save(rateResult);
    		} catch (Exception e) {
    			e.printStackTrace();
    			logger.error("评级结果添加失败！");
				addFlag = false;
    		}
		}

		if (addFlag && updateFlag) {
			//成功标识
			return JsonResult.ok(null);
		} else {
			if (!addFlag) {
				return JsonResult.error("评级结果添加失败！");
			} else {
				return JsonResult.error("评级结果更新成功！");
			}
		}
	}
	
	/**
	 * 删除评级结果
	 * @param request
	 */
	@ResponseBody
	@PostMapping("/deleteRateResult")
	public JsonResult deleteRateResult(HttpServletRequest request){
		//判空处理
		String id = request.getParameter("id");

		if (!StringUtils.isEmpty(id)) {
			try {
				rateResultService.deleteById(Integer.parseInt(id));
				//成功标识
				return JsonResult.ok(null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("评级结果删除失败！");
				//失败标识
				return JsonResult.error("评级结果删除失败！");
			}
		} else {
			return JsonResult.error("评级结果id为空！");
		}
	}
	
}
