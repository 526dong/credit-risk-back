package com.ccx.industry.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ccx.enterprise.model.EnterpriseIndustry;
import com.ccx.enterprise.model.IndustryShow;
import com.ccx.enterprise.service.EnterpriseIndustryService;
import com.ccx.util.page.Page;


/**
 * 行业管理
 * @author xzd
 * @date 2017/7/3
 */
@Controller
@RequestMapping(value="/industry")
public class IndustryController{
    private static final Logger logger = LogManager.getLogger(IndustryController.class);

    /*前台行业*/
    @Autowired
    EnterpriseIndustryService industryService;

    /*后台行业*/
    @Autowired
    //AbsIndustryBackService backIndustryService;

    /**
	 * 行业列表页
	 */
	@GetMapping("/list")
	public String industryList(HttpServletRequest request){
		logger.debug("跳转行业列表展示页面");

		return "industryMng/industryList";
	}

	/**
	 * 查询行业列表
	 * @return
	 */
	@ResponseBody
	@PostMapping("/findAllIndustry")
	public Map<String, Object> findAllIndustry(Integer pageNo, Integer pageSize, String searchContent){
		logger.debug("查询行业列表");
		Map<String, Object> resultMap = new HashMap<>();

		//分页
		Page<IndustryShow> page = new Page<IndustryShow>();

		//参数
    	Map<String,Object> params = new HashMap<String,Object>();

    	params.put("searchContent", searchContent);

		try {
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			page.setParams(params);
			page = industryService.findAllIndustry(page);

			//成功标识
			resultMap.put("code", 200);
			resultMap.put("page", page);
		} catch (Exception e) {
			logger.error("查看行业列表:", e);
			//失败标识
			resultMap.put("code", 500);
		}

		return resultMap;
	}

	/**
	 * 查询后台行业列表
	 * @return
	 */
	/*@ResponseBody
	@PostMapping("/findAllBackIndustry")
	public Map<String, Object> findAllBackIndustry(Integer pageNo, Integer pageSize){
		logger.debug("查询行业列表");
		Map<String, Object> resultMap = new HashMap<>();

		//分页
		Page page = new Page<>();

		//参数
    	Map<String,Object> params = new HashMap<String,Object>();

		try {
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			page.setParams(params);

			//后台行业列表
			page = backIndustryService.getPageList(page);

			//成功标识
			resultMap.put("code", 200);
			resultMap.put("page", page);
		} catch (Exception e) {
			logger.error("查看行业列表:", e);
			//失败标识
			resultMap.put("code", 500);
		}

		return resultMap;
	}*/

	/**
	 * 跳转新增行业页面
	 * @param request
	 */
	@GetMapping("/add")
	public ModelAndView add(){
		logger.debug("跳转新增行业页面");

		return new ModelAndView("industryMng/industryAdd");
	}

	/**
	 * 添加行业-数据库操作
	 * @param request
	 */
	@ResponseBody
	@PostMapping("/doAdd")
	public Map<String, Object> addIndustry(HttpServletRequest request, @RequestBody IndustryShow industryList){
		logger.debug("添加或更新企业性质-数据库操作");

		Map<String, Object> map = new HashMap<>();

		//获取当前登录用户信息
    	/*AbsUser user = (AbsUser)request.getSession().getAttribute("zxuser");*/

    	//一级行业
    	EnterpriseIndustry industry1 = new EnterpriseIndustry();
    	//二级行业
    	EnterpriseIndustry industry2 = new EnterpriseIndustry();

    	industry1.setName(industryList.getName1());
    	industry2.setName(industryList.getName2());

    	/*if (user != null) {
    		//添加创建人
    		industry1.setCreatorName(user.getName());
    		industry2.setCreatorName(user.getName());
		}*/

		//添加创建时间
		industry1.setCreateDate(new Date());
		industry2.setCreateDate(new Date());

		//一级行业添加父节点
		industry1.setPid(0);

		try {
			industryService.insert(industry1);

			//二级行业添加父节点
			industry2.setPid(industry1.getId());

			industryService.insert(industry2);

			logger.debug("行业添加成功！");
			map.put("result", 1);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("行业添加失败！");
			map.put("result", 0);
		}

		return map;
	}

	/**
	 * 跳转更新行业页面
	 * @param request
	 */
	@GetMapping("/update")
	public ModelAndView update(HttpServletRequest request){
		logger.debug("跳转更新行业页面");

		String id = request.getParameter("id");
		boolean idFlag = id != null && !"".equals(id);

		if (idFlag) {
			//二级行业
			EnterpriseIndustry industry2 = industryService.findIndustryById(Integer.parseInt(id));

			if (industry2 != null && industry2.getPid() != null && !"".equals(industry2.getPid())) {
				//一级行业
				EnterpriseIndustry industry1 = industryService.findIndustryById(industry2.getPid());

				if (industry1 != null){
					request.setAttribute("industry1", industry1);
				}

				request.setAttribute("industry2", industry2);
			}
		}

		return new ModelAndView("industryMng/industryUpdate");
	}

	/**
	 * 更新行业-数据库操作
	 * @param request
	 */
	@ResponseBody
	@PostMapping("/doUpdate")
	public Map<String, Object> doUpdate(HttpServletRequest request, @RequestBody IndustryShow industryList){
		logger.debug("更新行业-数据库操作");

		Map<String, Object> map = new HashMap<>();

		//获取当前登录用户信息
    	/*AbsUser user = (AbsUser)request.getSession().getAttribute("zxuser");*/

		EnterpriseIndustry industry1 = new EnterpriseIndustry();
    	EnterpriseIndustry industry2 = new EnterpriseIndustry();

    	/*if (user != null) {
    		//添加创建人
    		industry1.setCreatorName(user.getName());
    		industry2.setCreatorName(user.getName());
		}*/

		//update
		try {
	    	//数据查到的一级行业
			industry1 = industryService.findIndustryById(industryList.getPid());
	    	//数据查到的二级行业
			industry2 = industryService.findIndustryById(industryList.getId());

			industry1.setName(industryList.getName1());
			industry2.setName(industryList.getName2());

			//添加创建时间
			industry1.setCreateDate(new Date());
			industry2.setCreateDate(new Date());

	    	//一级行业
	    	industryService.update(industry1);
	    	//二级行业
			industryService.update(industry2);
			logger.debug("行业更新成功！");
			map.put("result", 1);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("行业更新成功！");
			map.put("result", 0);
		}

		return map;
	}

	/**
	 * 跳转行业匹配页面
	 * @param request
	 */
	/*@GetMapping("/match")
	public ModelAndView match(HttpServletRequest request){
		logger.debug("跳转行业匹配页面");

		String id = request.getParameter("id");
		boolean idFlag = id != null && !"".equals(id);

		if (idFlag) {
			//二级行业
			EnterpriseIndustry industry2 = industryService.findIndustryById(Integer.parseInt(id));

			if (industry2 != null && industry2.getPid() != null && !"".equals(industry2.getPid())) {
				//一级行业
				EnterpriseIndustry industry1 = industryService.findIndustryById(industry2.getPid());

				if (industry1 != null){
					request.setAttribute("industry1", industry1);
				}

				request.setAttribute("industry2", industry2);
			}
		}

		//拼接自动补全的后台行业
		String backIndustryListString = getBackIndustryListString();

		request.setAttribute("backIndustryListString", backIndustryListString);

		return new ModelAndView("industryMng/industryMatch");
	}*/

	/**
	 * 行业匹配-数据库操作
	 * @param request
	 */
	@ResponseBody
	@PostMapping("/doMatch")
	public Map<String, Object> doMatch(HttpServletRequest request, @RequestBody IndustryShow industryList){
		logger.debug("行业匹配-数据库操作");

		Map<String, Object> map = new HashMap<>();

		//二级行业
		EnterpriseIndustry industry2 = industryService.findIndustryById(industryList.getId());

		if (industry2 != null) {
			//更新后台行业id到前台行业表中
			//industry2.setBackId(industryList.getBackId());

			try {
				industryService.update(industry2);
				logger.debug("行业匹配成功！");
				map.put("result", 1);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("行业匹配失败！");
				map.put("result", 0);
			}
		}

		return map;
	}

	/**
	 * 跳转行业匹配页面
	 * @param request
	 */
	/*@GetMapping("/batchMatch")
	public ModelAndView batchMatch(HttpServletRequest request){
		logger.debug("跳转行业匹配页面");

		//查询后台行业列表并进行拼接
		String backIndustryListString = getBackIndustryListString();
		request.setAttribute("backIndustryListString", backIndustryListString);

		return new ModelAndView("industryMng/industryBatchMatch");
	}*/

	/**
	 * 保存批量行业匹配
	 * @param request
	 */
	@ResponseBody
	@PostMapping("/doBatchMatch")
	public Map<String, Object> doBatchMatch(HttpServletRequest request, String ids, String backIds){
		logger.debug("保存批量行业匹配");

		//二级行业id
		String[] idsArr = ids.split(",");

		//后台行业id
		String[] backIdsArr = backIds.split(",");

		Map<String, Object> map = new HashMap<>();

		/*List<EnterpriseIndustry> industryList = new ArrayList<>();*/

		if (idsArr.length == backIdsArr.length) {
			int len = idsArr.length;

			//查询更新
			for (int i = 0; i < len; i++) {
				//-1是空的，去掉-1
				if (!"-1".equals(backIdsArr[i])) {
					EnterpriseIndustry industry = industryService.findIndustryById(Integer.parseInt(idsArr[i]));

					try {
						industryService.update(industry);
						map.put("result", 1);
						logger.debug("保存行业匹配成功");

					} catch (Exception e) {
						e.printStackTrace();
						map.put("result", 0);
						logger.error("保存行业匹配失败");
					}
					/*industryList.add(industry);*/
				}
			}

			/*//批量保存
			try {
				industryService.batchUpdate(industryList);
				map.put("result", 1);
				logger.debug("批量保存行业匹配成功");
			} catch (Exception e) {
				e.printStackTrace();
				map.put("result", 0);
				logger.error("批量保存行业匹配失败");
			}*/
		}

		return map;
	}

	/**
	 * 删除行业
	 * @param request
	 */
	@ResponseBody
	@PostMapping("/delete")
	public Map<String, Object> deleteIndustry(HttpServletRequest request){
		logger.debug("删除行业");

		Map<String, Object> map = new HashMap<>();

		String id = request.getParameter("id");
		boolean idFlag = id != null && !"".equals(id);

		if (idFlag) {
			try {
				industryService.deleteById(Integer.parseInt(id));
				logger.debug("行业删除成功！");
				map.put("result", 1);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("行业删除失败！");
				map.put("result", 0);
			}
		}

		return map;
	}

	/**
	 * 跳转新增后台行业页面
	 * @param request
	 */
	@GetMapping("/backAdd")
	public ModelAndView backAdd(){
		logger.debug("跳转新增后台行业页面");

		return new ModelAndView("industryMng/industryBackAdd");
	}

	/**
	 * 添加后台行业-数据库操作
	 * @param request
	 */
	/*@ResponseBody
	@PostMapping("/doBackAdd")
	public Map<String, Object> doBackAdd(HttpServletRequest request, @RequestBody AbsIndustryBack backIndustry){
		logger.debug("添加或更新企业性质-数据库操作");

		Map<String, Object> map = new HashMap<>();

		//添加创建时间
		backIndustry.setCerateTime(new Date());

		try {
			backIndustryService.save(backIndustry);
			logger.debug("后台行业添加成功！");
			map.put("result", 1);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("后台行业添加失败！");
			map.put("result", 0);
		}

		return map;
	}*/

	/**
	 * 跳转更新后台行业页面
	 * @param request
	 */
	/*@GetMapping("/backUpdate")
	public ModelAndView backUpdate(HttpServletRequest request){
		logger.debug("跳转更新后台行业页面");

		String id = request.getParameter("id");
		boolean idFlag = id != null && !"".equals(id);

		if (idFlag) {
			AbsIndustryBack backIndustry = backIndustryService.getById(Integer.parseInt(id));

			if (backIndustry != null) {
				request.setAttribute("backIndustry", backIndustry);
			}
		}

		return new ModelAndView("industryMng/industryBackUpdate");
	}*/

	/**
	 * 更新后台行业-数据库操作
	 * @param request
	 */
	/*@ResponseBody
	@PostMapping("/doBackUpdate")
	public Map<String, Object> doBackUpdate(HttpServletRequest request, @RequestBody AbsIndustryBack backIndustry){
		logger.debug("更新后台行业-数据库操作");

		Map<String, Object> map = new HashMap<>();

		//添加更新时间
		backIndustry.setCerateTime(new Date());

		try {
			backIndustryService.update(backIndustry);
			logger.debug("后台行业更新成功！");
			map.put("result", 1);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("后台行业更新失败！");
			map.put("result", 0);
		}

		return map;
	}*/

	/**
	 * 删除后台行业
	 * @param request
	 */
	/*@ResponseBody
	@PostMapping("/backDelete")
	public Map<String, Object> backDelete(HttpServletRequest request){
		logger.debug("删除后台行业");

		Map<String, Object> map = new HashMap<>();

		String id = request.getParameter("id");
		boolean idFlag = id != null && !"".equals(id);

		if (idFlag) {
			try {
				backIndustryService.deleteById(Integer.parseInt(id));
				logger.debug("行业删除成功！");
				map.put("result", 1);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("行业删除失败！");
				map.put("result", 0);
			}
		}

		return map;
	}*/

	/**
	 * 拼接自动补全的后台行业
	 * @return
	 */
	/*public String getBackIndustryListString(){
		//后台行业列表
		List<AbsIndustryBack> backIndustryList = backIndustryService.getList();

		//拼接自动补全的后台行业
		String backIndustryListString = null;

		if(backIndustryList != null && backIndustryList.size() > 0){
			for(int i =0;i<backIndustryList.size();i++){
				backIndustryListString = backIndustryListString +","+"{id:"+backIndustryList.get(i).getId()+","+"name:\""+backIndustryList.get(i).getName()+"\"}";
	        }
			backIndustryListString = "["+backIndustryListString.substring(5)+"]";
		}

		return backIndustryListString;
	}*/
}

