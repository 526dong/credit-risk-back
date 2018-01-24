package com.ccx.pricedata.controller;	
	
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ccx.system.model.UserBg;
import com.ccx.util.ControllerUtil;
import com.ccx.util.EnterpriseDataUtils;
import com.ccx.util.JsonResult;
import com.ccx.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ccx.dictionary.model.RateResult;
import com.ccx.dictionary.service.RateResultService;
import com.ccx.pricedata.model.PriceData;
import com.ccx.pricedata.service.PriceDataService;
import com.ccx.util.page.Page;	
	
/**
 * 定价数据管理
 * @author xzd
 * @date 2017/7/13
 */
@Controller	
@RequestMapping("/priceData")	
public class PriceDataController {	
	private static final Logger logger = LogManager.getLogger(PriceDataController.class);	
	
	@Autowired	
	private PriceDataService priceDataService;	
	
	/*评级结果*/
	@Autowired
	private RateResultService rateResultService;
	
	/**	
	 * @author xzd	
	 * @description	定价数据列表
	 */	
	@GetMapping("list")
	public ModelAndView list(HttpServletRequest request) {	
		return new ModelAndView("priceDataMng/priceDataList");
	}	
	
	/**	
	 * @author xzd	
	 * @description	查询定价数据列表
	 */	
	@ResponseBody
	@PostMapping("/findAll")
	public JsonResult findAll(Integer pageNo, Integer pageSize){
		//分页
		Page<PriceData> page = new Page<PriceData>();
		//参数
    	Map<String,Object> params = new HashMap<String,Object>();
    	
		try {
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			page.setParams(params);
			
			page = priceDataService.getPageList(page);

			//成功标识
			return JsonResult.ok(page);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询定价数据列表失败:", e);
			//失败标识
			return JsonResult.error("查询定价数据列表失败!");
		}	
	}

	/**
	 * @author xzd
	 * @description	更新定价
	 */	
	@GetMapping("/update")	
	public ModelAndView update(HttpServletRequest request) {
		//定价id
		String id = request.getParameter("id");

		if (!StringUtils.isEmpty(id)) {
			try {
				//通过id查询定价数据
				PriceData priceData = priceDataService.getById(Integer.parseInt(id));

				request.setAttribute("priceData", priceData);
			} catch (Exception e) {
				logger.error("查询定价数据信息失败");
				e.printStackTrace();
			}
		}
		
		//获取字典列表-评级结果
		EnterpriseDataUtils.getDictionary(request, false, null, rateResultService);
		
		return new ModelAndView("priceDataMng/priceDataUpdate");	
	}

	/**
	 * @author xzd 	
	 * @description	更新定价-数据库操作
	 */	
	@ResponseBody	
	@PostMapping("/doUpdate")	
	public JsonResult doUpdate(HttpServletRequest request, PriceData priceData) {
		try {
			//修改人
			UserBg sessionUser = ControllerUtil.getSessionUser(request);
			priceData.setCreatorName(sessionUser.getLoginName());
			priceDataService.update(priceData);
			return JsonResult.ok(null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("更新定价-数据库操作失败");
			return JsonResult.error("更新定价-数据库操作失败!");
		}
	}

}
