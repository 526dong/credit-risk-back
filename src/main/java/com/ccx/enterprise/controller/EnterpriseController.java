package com.ccx.enterprise.controller;

import com.ccx.businessmng.model.AbsEnterpriseReportType;
import com.ccx.businessmng.service.AbsEnterpriseReportTypeService;
import com.ccx.enterprise.model.Enterprise;
import com.ccx.enterprise.model.HistoryApproval;
import com.ccx.enterprise.model.RateResult;
import com.ccx.enterprise.model.Report;
import com.ccx.enterprise.service.ApprovalService;
import com.ccx.enterprise.service.EnterpriseIndustryService;
import com.ccx.enterprise.service.EnterpriseService;
import com.ccx.enterprise.service.ReportService;
import com.ccx.index.model.AbsIndex;
import com.ccx.index.service.AbsIndexService;
import com.ccx.index.service.AbsModelElementService;
import com.ccx.reportmsg.manager.CommonGainReport;
import com.ccx.reportmsg.manager.CommonGainReportValue;
import com.ccx.reportmsg.manager.ComonReportPakage;
import com.ccx.reporttype.dao.AbsEnterpriseReportSheetMapper;
import com.ccx.reporttype.model.AbsEnterpriseReportSheet;
import com.ccx.util.*;
import com.ccx.util.page.Page;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建企业
 * @author xzd
 * @date 2017/6/15
 */
@Controller
@RequestMapping(value="/enterprise")
public class EnterpriseController{
	private static final Logger logger = LoggerFactory.getLogger(EnterpriseController.class);
	
	@Autowired
	private EnterpriseService enterpriseService;
	
	/*行业字典*/
	@Autowired
	private EnterpriseIndustryService industryService;
	
	/*指标*/
	@Autowired
	private AbsIndexService indexService;
	
	/*指标因素*/
	@Autowired
	private AbsModelElementService elementService;
	
	@Autowired
	private ReportService reportService;
	
	@Autowired
	private ApprovalService approvalService;

	/*报表数据*/
	@Autowired
	private CommonGainReport commonGainReport;

	/*报表数据*/
	@Autowired
	private CommonGainReportValue commonGainReportValue;

	/*财务报表类型数据*/
	@Autowired
	private AbsEnterpriseReportTypeService enterpriseReportTypeService;

	/*财务报表sheet*/
	@Autowired
	private AbsEnterpriseReportSheetMapper enterpriseReportSheetMapper;
	
	/**
	 * 创建企业列表页
	 */
	@GetMapping("/list")
	public String list(){
		return "enterpriseDataMng/enterpriseList";
	}
	
	/**
	 * 查询创建企业列表
	 * @return
	 */
	@PostMapping("/findAll")
	@ResponseBody
	public JsonResult findAll(HttpServletRequest request, Integer pageNo, Integer pageSize){
		//获取查询条件
		Map<String, Object> params = ControllerUtil.request2Map(request);
		
		//分页
		Page<Enterprise> page = new Page<Enterprise>();

		try {
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);

			page.setParams(params);
			page = enterpriseService.findAll(page);

			//成功标识
			return JsonResult.ok(page);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询企业列表失败:", e);
			//失败标识
			return JsonResult.error("查询企业列表失败！");
		}
	}

	/**
	 * 查询创建企业列表
	 * @return
	 */
	@PostMapping("/findAllEnt")
	@ResponseBody
	public JsonResult findAllEnt(HttpServletRequest request, Integer pageNo, Integer pageSize){
		//获取查询条件
		Map<String, Object> params = ControllerUtil.request2Map(request);

		//分页
		Page<Map<String, Object>> page = new Page<Map<String, Object>>();

		try {
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);

			page.setParams(params);
			page = enterpriseService.findAllEnt(page);

			//成功标识
			return JsonResult.ok(page);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询已评级企业列表失败:", e);
			//失败标识
			return JsonResult.error("查询已评级企业列表失败！");
		}
	}
	
	/**
	 * 异步加载二级行业
	 */
	@ResponseBody
	@PostMapping("/getIndustry")
	public Map<String, Object> getIndustry(HttpServletRequest request){
		//jsp result map
		Map<String, Object> resultMap = new HashMap<>();
		
		//加载二级行业
		resultMap = EnterpriseDataUtils.loadIndustry(request, industryService);
		
		return resultMap;
	}
	
	/**
	 * 异步加载指标
	 */
	@ResponseBody
	@PostMapping("/getIndex")
	public JsonResult getIndex(@RequestParam(required = true) Integer industry2Id,
			@RequestParam(required = true) Integer entType){
		//params map
		Map<String, Integer> params = new HashMap<>();

		params.put("industry2Id", industry2Id);
		params.put("entType", entType);

		//指标列表
		List<AbsIndex> indexList = null;

		try {
			//加载指标
			indexList = EnterpriseDataUtils.loadIndex(industryService, indexService, elementService, params);

			//成功返回数据
			return JsonResult.ok(indexList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询指标列表失败：", e);
			return JsonResult.error("查询指标列表失败！");
		}
	}
	
	/**
	 * 跳转企业创建查看页面
	 * @return
	 */
	@GetMapping("/detail")
	public String detail(HttpServletRequest request){
		//企业id
		String idStr = request.getParameter("id");

		if (!StringUtils.isEmpty(idStr)) {
			int id = Integer.parseInt(idStr);

			try {
				//查询企业主体信息
				Enterprise enterprise = enterpriseService.findById(id);

				if (enterprise != null){
					request.setAttribute("enterprise", enterprise);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("企业主体信息查询失败", e);
			}

			request.setAttribute("enterpriseId", id);
		}

		return "enterpriseDataMng/enterpriseHistoryRate";
	}
	
	/**
	 * 查询历史评级信息
	 * @return
	 */
	@PostMapping("/findHistoryRate")
	@ResponseBody
	public JsonResult findHistoryRate(HttpServletRequest request, Integer pageNo, Integer pageSize){
		//获取查询条件
		Map<String, Object> params = new HashMap<>();

		String enterpriseId = request.getParameter("enterpriseId");

		params.put("enterpriseId", Integer.parseInt(enterpriseId));

		if (enterpriseId != null && !"".equals(enterpriseId)) {
			//分页
			Page<Map<String, Object>> page = new Page<Map<String, Object>>();

			try {
				page.setPageNo(pageNo);
				page.setPageSize(pageSize);

				page.setParams(params);
				page = enterpriseService.findHistoryRate(page);

				//成功标识
				return JsonResult.ok(page);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("查询企业评级历史列表失败:", e);
				//失败标识
				return JsonResult.error("查询企业评级历史列表失败！");
			}
		} else {
			logger.error("查询企业评级历史列表失败，企业id为空！");
			return JsonResult.error("查询企业评级历史列表失败，企业id为空！");
		}
	}
	
	/**
	 * 跳转企业创建查看页面
	 * @return
	 */
	@GetMapping("/rateDetail")
	public String rateDetail(HttpServletRequest request) throws Exception {
		//评级id
		String approvalId = request.getParameter("approvalId");

		//一、查询评级信息
		if (StringUtils.isNotBlank(approvalId)) {
			HistoryApproval historyApproval = approvalService.selectById(Integer.parseInt(approvalId));

			if (historyApproval != null) {
				request.setAttribute("approval", historyApproval);
			}
		}

		//二、评级信息查看
		//企业id
		String enterpriseId = request.getParameter("enterpriseId");
		
		if (StringUtils.isNotBlank(enterpriseId)) {
			int id = Integer.parseInt(enterpriseId);
		
			//企业主体信息
			Enterprise enterprise = new Enterprise();
			
			try {
				enterprise = enterpriseService.findById(id);
				
				if (enterprise != null){
					//加载企业数据：指标规则
					EnterpriseDataUtils.loadIndexData(request, enterprise, enterpriseService);
					
					request.setAttribute("enterprise", enterprise);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("企业主体信息查询失败");
			}
			
			request.setAttribute("enterpriseId", id);
		}
		
		return "enterpriseDataMng/enterpriseRateDetail";
	}
	
	/**
	 * 企业评级详情
	 * @return
	 */
	@ResponseBody
	@PostMapping("/rateInfo")
	public JsonResult rateInfo(HttpServletRequest request, @RequestParam(required = true) String ratingApplyNum){
		//通过因素group by
		Map<String, Integer> countMap = new HashMap<>();
		
		if (!StringUtils.isEmpty(ratingApplyNum)) {
			try {
				//通过评级申请编号进行查询评级列表
				List<RateResult> rateResultList = enterpriseService.findByRatingApplyNum(ratingApplyNum);
				for (RateResult result:rateResultList) {
					if (null == countMap.get(result.getElementName())) {
						countMap.put(result.getElementName(), 1);
					} else {
						int n = countMap.get(result.getElementName());
						countMap.put(result.getElementName(), ++n);
					}
				}
				
				String eleName = "";
				for (RateResult result:rateResultList) {
					if (result.getElementName() != null && !eleName.equals(result.getElementName())) {
						int n = countMap.get(result.getElementName());
						result.setCount(n);
						eleName = result.getElementName();
					}
				}
				
				//成功标识
				return JsonResult.ok(rateResultList);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("查询企业评级详情失败:", e);
				//失败标识
				return JsonResult.error("查询企业评级详情失败！");
			}
		} else {
			//失败标识
			return JsonResult.error("查询企业评级详情失败，评级申请编号！");
		}
	}

	/**
	 * 查询报表列表详情
	 * @param enterpriseId
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@PostMapping("/reportList")
	public JsonResult reportList(HttpServletRequest request, @RequestParam(required = true)Integer enterpriseId){
		//report list
		List<Report> reportBean = new ArrayList<>();

		try {
			//通过企业id查询企业报表概况信息
			reportBean = reportService.findByEnterpriseId(enterpriseId);
			return JsonResult.ok(reportBean);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询企业报表概况信息失败", e);
			//失败标识
			return JsonResult.error("查询企业报表概况信息失败!");
		}
	}

	/**
	 * 单个报表概况信息
	 * @param request
	 * @return Report
	 */
	@ResponseBody
	@PostMapping("/mainReport")
	public JsonResult mainReport(HttpServletRequest request){
		//报表概况id
		String reportId = request.getParameter("id");

		try {
			//通过报表id查询企业报表概况信息
			ComonReportPakage.BaseValues reportData = commonGainReportValue.getBaseValue(Integer.parseInt(reportId));

			if (reportData != null && reportData.getReport() != null) {
				Report report = reportData.getReport();
				if (!"".equals(report.getType())) {
					String reportName = enterpriseReportTypeService.getNameById(report.getType());

					if (!StringUtils.isEmpty(reportName)) {
						report.setReportName(reportName);

						reportData.setReport(report);
					}
				}
			}

			//成功标识
			return JsonResult.ok(reportData);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询企业报表信息失败", e);
			//失败标识
			return JsonResult.error("查询企业报表信息失败!");
		}
	}

	/**
	 * 通过确定的行业报表对应确定的报表类型来加载报表模板
	 * @param request
	 * @param reportType 报表类型
	 * @return
	 */
	@ResponseBody
	@PostMapping("/getReportModel")
	public JsonResult getReportModel(HttpServletRequest request, @RequestParam(required = true)Integer reportType) {
		try {
			//加载默认报表数据
			List<ComonReportPakage.Modle> reportData = commonGainReport.getModles(reportType);
			//成功标识
			return JsonResult.ok(reportData);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载报表模板数据失败！", e);
			//失败标识
			return JsonResult.error("加载报表模板数据失败!");
		}
	}

	/**
	 * 通过财务报表类型加载报表联表公式
	 * @param request
	 * @param reportType 报表类型
	 * @return
	 *//*
	@ResponseBody
	@PostMapping("/getReportCheckFormula")
	public JsonResult getReportCheckFormula(HttpServletRequest request, @RequestParam(required = true)Integer reportType) {
		try {
			//通过财务报表类型加载报表联表公式
			List<AbsEnterpriseReportCheck> reportCheckList = enterpriseReportCheckApi.getByReportType(reportType);
			//成功标识
			return JsonResult.ok(reportCheckList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载报表联表公式失败！", e);
			//失败标识
			return JsonResult.error("加载报表联表公式失败!");
		}
	}*/

	/**
	 * 导出报表
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/exportReportDataExcel")
	public void exportReportDataExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//报表id
		Integer reportId = Integer.parseInt(request.getParameter("reportId") == "" ? "0" : request.getParameter("reportId"));
		//财务报表类型
		Integer reportType = Integer.parseInt(request.getParameter("reportType") == "" ? "0" : request.getParameter("reportType"));
		//财务报表子表类型
		Integer reportSonType = Integer.parseInt(request.getParameter("reportSonType") == "" ? "0" : request.getParameter("reportSonType"));
		//财务报表第几张子表
		Integer reportIndex = Integer.parseInt(request.getParameter("reportIndex") == "" ? "0" : request.getParameter("reportIndex"));

		//导出的报表名称
		String exportFileName = "" + reportType;

		//Excel模板所在的路径
		String filePath  = null;

		try {
			filePath = EnterpriseDataUtils.getFilePath(PropertiesUtil.getData("file-path.properties", "enterprise_report_template_path"), exportFileName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		File file = new File(filePath);

		// 初始化一个工作簿
		Workbook wb = null;
		try {
			//通过流读取文件
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			wb = new HSSFWorkbook(bis);
		} catch (Exception e) {
			//通过流读取文件
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			wb = new XSSFWorkbook(bis);
			e.printStackTrace();
		}

		//文件名称
		String fileName	= dealFileNameEncode(request, reportType, reportSonType);

		if (reportId != 0) {
			//通过报表id查询报表数据
			ComonReportPakage.BaseValues baseValue = commonGainReportValue.getBaseValue(reportId);

			if (baseValue != null) {
				//报表中子表数据
				List<ComonReportPakage.Values> values = baseValue.getValues();

				if (values != null && values.size() > 0) {
					try {
						//设置response参数，可以打开下载页面
						response.reset();
						response.setHeader("Content-Disposition", "attachment;filename=\""+fileName+".xls\"");
						response.setContentType("application/msexcel;charset=UTF-8");// 设置类型

						OutputStream out = response.getOutputStream();

						ExportReportExcel exportExcel = new ExportReportExcel();
						exportExcel.doReportExcel(out, wb, values, reportIndex);

						out.flush();
						out.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 解决导出文件名称乱码问题
	 * @param request
	 * @param reportType
	 * @param reportSonType
	 * @return
	 */
	public String dealFileNameEncode(HttpServletRequest request, Integer reportType, Integer reportSonType){
		//文件名称
		String fileName = null;

		//处理IE乱码
		String userAgent = request.getHeader("user-agent");

		//通过报表获取文件名称
		String str = getFileName(reportType, reportSonType);

		if (userAgent != null && userAgent.indexOf("Firefox") >= 0 || userAgent.indexOf("Chrome") >= 0 || userAgent.indexOf("Safari") >= 0) {
			try {
				fileName = new String(str.getBytes("UTF-8"),"ISO-8859-1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			try {
				fileName= URLEncoder.encode(str, "UTF8"); //其他浏览器
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		return fileName;
	}

	/**
	 * 获取报表子表sheet名称-即文件名称
	 * @param reportType
	 * @param reportSonType
	 * @return
	 */
	public String getFileName(Integer reportType, Integer reportSonType){
		String sheetName = "";

		if (reportSonType == 0) {
			//1、全表导出
			//通过报表类型查询报表名称
			AbsEnterpriseReportType enterpriseReportType = enterpriseReportTypeService.getById(reportType);

			if (enterpriseReportType != null) {
				sheetName = enterpriseReportType.getName();
			}
		} else {
			//2、单表导出
			//通过报表类型和报表子表类型查询报表子表名称
			AbsEnterpriseReportSheet reportSheet = enterpriseReportSheetMapper.selectByPrimaryKey(reportSonType);

			if (reportSheet != null) {
				sheetName = reportSheet.getName();
			}
		}

		return sheetName;
	}
}
