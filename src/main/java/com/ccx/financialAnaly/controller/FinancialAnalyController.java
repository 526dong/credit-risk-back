package com.ccx.financialAnaly.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.ccx.financialAnaly.model.FinancialAnalyBean;
import com.ccx.financialAnaly.service.FinancialAnalyService;
import com.ccx.reportmsg.manager.CommonGainReport;
import com.ccx.reportmsg.manager.ComonReportPakage;
import com.ccx.system.model.UserBg;
import com.ccx.util.ControllerUtil;
import com.ccx.util.PropertiesUtil;
import com.ccx.util.StringUtils;
import com.ccx.util.UsedUtil;
import com.ccx.util.page.Page;

@Controller
@RequestMapping("/financialAnaly")
public class FinancialAnalyController{
	
	private Logger logger = LogManager.getLogger(FinancialAnalyController.class);
	private static FormulaEvaluator evaluator;
	
	private static final String EXCEL_XLS = "xls";  
    private static final String EXCEL_XLSX = "xlsx"; 
	
	@Autowired
	private FinancialAnalyService financialAnalyService;
	@Autowired
	private CommonGainReport commonGainReport;

	/**
	 * 进入企业财务分析管理界面
	 * @return
	 */
	@RequestMapping(value="/financialTemplateList", method=RequestMethod.GET) 
	public String toFinancialModuleList(HttpServletRequest request){
//		financialAnalyService.modifiFinancialTemplateReportType(1,9);
		//查询报表类型已经被改变的list
		List<FinancialAnalyBean> historyTypeTemplateList = financialAnalyService.getAllHistoryTypeTemplate();
		request.setAttribute("historyTypeTemplateList", JSON.toJSON(historyTypeTemplateList));
		return "financialAnalysisMng/financialTemplateList";
	}
	
	/**
	 * 进入企业财务分析上传模板界面
	 * @return
	 */
	@RequestMapping(value="/toImportFinancialTemplate", method=RequestMethod.GET) 
	public String toImportFinancialTemplate(HttpServletRequest request){
		List<LinkedHashMap<String, Object>> reportTypeList = financialAnalyService.getReportTypeList();
		request.setAttribute("reportTypeList", reportTypeList);
		return "financialAnalysisMng/financialTemplateImport";
	}
	
	/**
	 * 进入查看企业财务界面
	 * @return
	 */
	@RequestMapping(value="/toFinancialTemplateDetail", method=RequestMethod.GET) 
	public String toFinancialTemplateDetail(HttpServletRequest request){
		String financialId = null == request.getParameter("financialId")?"":request.getParameter("financialId").trim();
		FinancialAnalyBean financialAnalyBean = financialAnalyService.getFinancialTemplateById(Long.valueOf(financialId));
		request.setAttribute("financialAnalyBean", financialAnalyBean);
		return "financialAnalysisMng/financialTemplateDetail";
	}
	
	/**
	 * 进入修改企业财务界面
	 * @return
	 */
	@RequestMapping(value="/toUpdateFinancialTemplate", method=RequestMethod.GET) 
	public String toUpdateFinancialTemplate(HttpServletRequest request){
		String financialId = null == request.getParameter("financialId")?"":request.getParameter("financialId").trim();
		FinancialAnalyBean financialAnalyBean = financialAnalyService.getFinancialTemplateById(Long.valueOf(financialId));
		List<LinkedHashMap<String, Object>> reportTypeList = financialAnalyService.getReportTypeList();
		request.setAttribute("reportTypeList", reportTypeList);
		request.setAttribute("financialAnalyBean", financialAnalyBean);
		return "financialAnalysisMng/financialTemplateUpdate";
	}
	

	/**
     * 
     * @Title: findAllSysRole  
     * @author: WXN
     * @Description: 获取用户列表(分页)(这里用一句话描述这个方法的作用)   
     * @param: @param map
     * @param: @return      
     * @return: Page<UserVo>      
     * @throws
     */
    @RequestMapping(value="/getFinancialTemplateList", method=RequestMethod.POST) 
   	@ResponseBody
   	public Page<FinancialAnalyBean> getFinancialTemplateList(HttpServletRequest request) {
    	//获取请求参数
    	Map<String,Object> map = ControllerUtil.request2Map(request);
    	Page<FinancialAnalyBean> pages = new Page<FinancialAnalyBean>();
    	Map<String,Object> params = new HashMap<String,Object>();
    	logger.info("参数=========================>"+map);
		// 获取当前页数
		String currentPage = (String) map.get("currentPage");
		//获取每页展示数
		String pageSize = (String) map.get("pageSize");
		//当前页数
		int pageNum = 1;
		if (UsedUtil.isNotNull(currentPage)) { 
			pageNum = Integer.valueOf(currentPage);
		}
		// 设置当前页数
		pages.setPageNo(pageNum);
		//设置参数
		pages.setParams(params);
		//设置每页展示数
		if(UsedUtil.isNotNull(pageSize)){ 
			pages.setPageSize(Integer.valueOf(pageSize));
		}
		//查询账号列表
		pages = financialAnalyService.getFinancialTemplateList(pages);
		return pages;
	}
    
    /**
     * 
     * @描述：获取报表类型数据，并且不能包含财务分析模板数据库中已经存在的类型
     * @创建时间：  2017年9月28日
     * @作者：武向楠
     * @参数： @param request
     * @参数： @return      
     * @return String     
     * @throws
     */
    @RequestMapping(value="/getReportTypeList", method=RequestMethod.POST) 
   	@ResponseBody
   	public String getReportTypeList(HttpServletRequest request) {
    	List<LinkedHashMap<String, Object>> reportTypeList = financialAnalyService.getReportTypeList();
    	return JSON.toJSONString(reportTypeList);
    }
    
    /**
     * 
     * @描述：更改财务分析状态 启用/禁用/删除
     * @创建时间：  2017年9月28日
     * @作者：武向楠
     * @参数： @param request
     * @参数： @return      
     * @return String     
     * @throws
     */
    @RequestMapping(value="/updateFinancialStates", method=RequestMethod.POST) 
   	@ResponseBody
   	public String updateFinancialStates(HttpServletRequest request) {
    	String result = "9999";
    	String id = null == request.getParameter("financialId")?"":request.getParameter("financialId").trim();
    	String updateMsg = null == request.getParameter("financialFlag")?"":request.getParameter("financialFlag").trim();
    	if(UsedUtil.isNotNull(id) && UsedUtil.isNotNull(updateMsg)){
    		long financialId= Long.parseLong(id);
    		FinancialAnalyBean financialAnalyBean = new FinancialAnalyBean();
    		financialAnalyBean.setId(financialId);
    		if("startUse"==updateMsg || "startUse".equals(updateMsg)){
    			financialAnalyBean.setState(1);
    		}else if ("stopUse"==updateMsg || "stopUse".equals(updateMsg)) {
    			financialAnalyBean.setState(0);
			}else if ("delete"==updateMsg || "delete".equals(updateMsg)) {
    			financialAnalyBean.setIsDel(1);
			}
    		result = financialAnalyService.updateFinancialStates(financialAnalyBean);
    	}
    	return result;
    }
    
    
    /**
     * 
     * @描述：下载财务分析模板
     * @创建时间：  2017年9月28日
     * @作者：武向楠
     * @参数： @param request
     * @参数： @param response      
     * @return void     
     * @throws
     */
    @RequestMapping(value="/exportFinancialTemplate", method=RequestMethod.GET) 
   	public void exportFinancialTemplate(HttpServletRequest request,HttpServletResponse response) {
    	InputStream inputStream = null;
		OutputStream os = null;
    	String financialIdd = null == request.getParameter("financialId")?"":request.getParameter("financialId").trim();
		if(UsedUtil.isNotNull(financialIdd)){
			Long financialId = Long.valueOf(financialIdd);
			//获取文件路径以及文件名信息
			FinancialAnalyBean financialAnalyBean = financialAnalyService.getFinancialTemplateById(financialId);
			if(null != financialAnalyBean){
				try {
    				String filePath = financialAnalyBean.getPath();//文件路径
    				String template_name = financialAnalyBean.getTemplateName();//模板在服务器中的别名
        			String fileName = financialAnalyBean.getName()+template_name.substring(template_name.lastIndexOf("."),template_name.length());//文件名称
        			String zipName=new String(fileName.getBytes("gb2312"), "iso8859-1");//Zip名称 中文转码
        			response.setContentType("application/vnd.ms-excel");
        			response.setHeader("Content-Disposition", "attachment;fileName="+zipName);
        			inputStream = new FileInputStream(filePath+template_name);
        			os = response.getOutputStream();
        			byte[] b = new byte[2048];
        			int length;
        			while ((length = inputStream.read(b)) > 0) {
        				os.write(b, 0, length);
        			}
        			// 这里主要关闭。
        			os.flush();
        			os.close();
        			inputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("下载Excel文件异常");
				}
			}
    	}
    }
    
    /**
	 * 导入财务报表
	 * @param excelFile
	 * 异常类型：
	 * 0-"上传成功"
	 * 1-"文件为空"
	 * 2-"上传文件中只有标题没有数据"
	 * 3-"上传的文件格式不正确"
	 * 4-"上传失败，请联系管理员"
	 * 5-"上传的文件数据在数据库中已经存在"
	 * 6-"上传的文件数据解析校验报错"
	 * 7-"财务模板与报表模板比对校验报错"
	 * 8-"财务模板与报表模板比对校验不匹配"
	 * 9-"传参错误"
	 * @return resultMap
	 */
	@ResponseBody
	@PostMapping(value="/importFinancialTemplate")
	public String importFinancialTemplate(HttpServletRequest request, HttpServletResponse response,MultipartFile excelFile) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String reportid = null==request.getParameter("reportTypeId")?"":request.getParameter("reportTypeId").trim();
		String updateFlag = null==request.getParameter("updateFlag")?"":request.getParameter("updateFlag").trim();
		String financialId = null==request.getParameter("financialId")?"":request.getParameter("financialId").trim();
		int dataTypeId = 0;
		if (UsedUtil.isNotNull(reportid)) {
			dataTypeId = Integer.parseInt(reportid);
		}else{
			logger.info("没有获取到报表类型id!");
    		resultMap.put("msg", 9);
            return JSON.toJSONString(resultMap);
		}
		if (null == excelFile || excelFile.isEmpty()){
    		logger.info("没有得到上传文件!");
    		resultMap.put("msg", 1);
            return JSON.toJSONString(resultMap);
    	}
		//解析报表内容并与报表类型数据内容进行匹配，若匹配成功返回0
		Map<String, Object> checkoutMap  = checkoutFinancialAndDataType(request,response,excelFile,dataTypeId);
		if ((Integer)checkoutMap.get("result") == 0) {
			//匹配成功，开始进行文件上传并入库操作
			int uploadResult = 4;
			if(UsedUtil.isNotNull(updateFlag) && ("update".equals(updateFlag) || "update" == updateFlag)){
				//更新操作
				if(UsedUtil.isNotNull(financialId)){
					uploadResult = updateFinancialTemplate(request,response,excelFile,dataTypeId,financialId);
				}
			}else{
				//新增操作
				uploadResult = uploadFinancialTemplate(request,response,excelFile,dataTypeId);
			}
			resultMap.put("msg", uploadResult);
		}else {
			resultMap.put("msg", (Integer)checkoutMap.get("result"));
			resultMap.put("falseKey", (String)checkoutMap.get("falseKey"));
    		resultMap.put("falseNum", (Integer)checkoutMap.get("falseNum"));
    		System.err.println("上传返回结果========="+JSON.toJSONString(resultMap));
    		response.setContentType("text/html;charset=UTF-8");  
            response.getWriter().print(JSON.toJSONString(resultMap));  
			return null;
		}
		return JSON.toJSONString(resultMap);
	}
	
	/**
	 * 解析报表内容并与报表类型数据内容进行匹配，若匹配成功返回0
	 * @param excelFile
	 * 异常类型：
	 * 0-"上传成功"
	 * 1-"文件为空"
	 * 2-"上传文件中只有标题没有数据"
	 * 3-"上传的文件格式不正确"
	 * 4-"上传失败，请联系管理员"
	 * 5-"上传的文件数据在数据库中已经存在"
	 * 6-"上传的文件数据解析校验报错"
	 * 7-"财务模板与报表模板比对校验报错"
	 * 8-"财务模板与报表模板比对校验不匹配"
	 * 9-"传参错误"
	 * @return resultMap
	 */
	public Map<String, Object> checkoutFinancialAndDataType(HttpServletRequest request, 
			HttpServletResponse response,MultipartFile excelFile,int dataTypeId) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int result = 8;
		InputStream inputStream = null;
		try {
            inputStream = excelFile.getInputStream();
            //文件名
	    	String fileName = excelFile.getOriginalFilename();
            // 初始化一个工作簿
            Workbook wb = null;
            if(fileName.endsWith(EXCEL_XLS)){ //Excel 2003  xls
            	wb = new HSSFWorkbook(inputStream);  
            } if(fileName.endsWith(EXCEL_XLSX)){  // Excel 2007/2010  xlsx
                wb = new XSSFWorkbook(inputStream);  
            } 
            //获取第二个sheet页-原始数据 合并报表-固定
            Sheet sheet = wb.getSheetAt(1);
            /**
             * 第一步 获取上传财务模板中的所有报表类型  即sheet页中的第一列以及所在行号
             */
            //定义第一列属性的list
            List<String> cellList = new ArrayList<String>();
            //定义第一列行号的list
            List<Integer> cellNumList = new ArrayList<Integer>();
            //excel行数 并不一定都是有效行 即可能存在空行
            int lastRowNum = sheet.getLastRowNum();
            logger.info("sheet表行数为：" + lastRowNum);
            for(int i=0; i<lastRowNum; i++){
                Row row = sheet.getRow(i);//取第一行的列
                if(UsedUtil.isNotNull(row)){
                	Cell cell = row.getCell(0);//取第一列的单元格
                	if(UsedUtil.isNotNull(row)){
                        String cellValue = getCellValue(cell);//取单元格内容
                        if(UsedUtil.isNotNull(cellValue)){
                        	cellList.add(cellValue); //第一列所有的属性的list 不包括空的单元格
                        	cellNumList.add(i);//第一列所有的行号的list 不包括空的单元格  属性与行号一一对应
                        }
                	}
                }
            }
            logger.info("第一步>>>>>>>>财务模板第二个sheet页中第一列属性list为=========》》》" + cellList);
            logger.info("第一步>>>>>>>>财务模板第二个sheet页中第一列行号list为=========》》》" + cellNumList);
            /**
             * 第二步 获取与该财务模板匹配的  报表类型名称以及所在行
             */
            //获取财务模板中的 数据类型名称 以及所在行
            List<ComonReportPakage.Modle> dataTypelist = null;
            try {
            	dataTypelist = commonGainReport.getModles(dataTypeId);//获取该数据类型id下的
            	logger.info("根据类型id获取报表类型的类型名称List=========》》》" + dataTypelist);
                if (null == dataTypelist || dataTypelist.isEmpty()) {
                	logger.info("根据类型id获取报表类型的类型名称List为空");
                	result = 8; // 成功标识
                	 resultMap.put("result", result);
                     return resultMap;
    			}
			} catch (Exception e) {
				logger.info("根据类型id获取报表类型的类型名称报错，第225行，报错方法为=========》》》commonGainReport.getModles",e);
				result = 7; // 成功标识
				resultMap.put("result", result);
	            return resultMap;
			}
            //确定财务模板中的类型表名以及所在行
            Map<String, Integer> shuxingMap = new LinkedHashMap<String, Integer>();
            //循环数据类型名称和财务sheet中的属性 当属性相同时 确定该字表所在的行 从而找出该字表所拥有的子属性
            for (int i = 0; i < dataTypelist.size(); i++) {
            	String dataTypeName = dataTypelist.get(i).getSheetName();//数据类型名称
            	//循环财务sheet页中的map
            	for (int j = 0; j < cellList.size(); j++) {
            		String cellListN = cellList.get(j);
            		if(UsedUtil.isNotNull(cellListN)){
            			if(cellListN == dataTypeName || cellListN.equals(dataTypeName)){
            				shuxingMap.put(cellListN, cellNumList.get(j));
            			}
            		}
				}
			}
            if (null == shuxingMap || 0 == shuxingMap.size() || shuxingMap.isEmpty()) {
            	logger.info("获取财务类型以及所在行为空");
            	result = 8; // 成功标识
            	resultMap.put("result", result);
                return resultMap;
			}
            logger.info("第二步>>>>>>>>数据类型名称以及所在行为=========》》》" + shuxingMap);
            /**
             * 第三步 获取财务模板中每一个 报表类型下的所有属性 
             * 封装结构为 {报表类型1:{属性1:行号,属性1:行号,属性1:行号,属性1:行号,...},报表类型2:{属性1:行号,属性1:行号,属性1:行号,属性1:行号,...},...}
             */
            //找出数据类型名称以及他下面的子属性
            //找出数据类型名称以及他下面的子属性的位置
            List<String> temList = new ArrayList<String>();
    		List<Integer> temList2 = new ArrayList<Integer>();
            for (int i = 0; i < cellList.size(); i++) {
				String cellListname = cellList.get(i);
				//遍历map中的键  
				for (String key : shuxingMap.keySet()) {  
				    if(key==cellListname || cellListname.equals(key)){
				    	temList.add(key);
				    	temList2.add(i);
				    }  
				}
			}
            Map<String, Map<String, Integer>> caiwushuxingMap = new LinkedHashMap<String, Map<String, Integer>>();
            for (int i = 0; i < temList2.size(); i++) {
            	int temI = 0;
            	int temJ = 0;
            	if(i<temList2.size()-1){
            		temI = temList2.get(i)+1;
            		temJ = temList2.get(i+1);
    			}else{
    				temI = temList2.get(i)+1;
            		temJ = cellList.size();
    			}
//            	List<Map<String, Integer>> zishuxingtemList = new ArrayList<Map<String, Integer>>();
            	Map<String, Integer> zishuxingMap = new LinkedHashMap<String, Integer>();
            	for (int j = temI; j < temJ; j++) {
            		zishuxingMap.put(cellList.get(j), cellNumList.get(j));
//            		zishuxingtemList.add(zishuxingMap);
				}
            	caiwushuxingMap.put(temList.get(i), zishuxingMap);
    		}
            if (null == caiwushuxingMap || 0 == caiwushuxingMap.size() || caiwushuxingMap.isEmpty()) {
            	logger.info("获取数据类型名称以及其下所有属性名称和所在行号为空");
            	result = 8; // 成功标识
            	resultMap.put("result", result);
                return resultMap;
			}
            logger.info("第三步>>>>>>>>数据类型名称以及其下所有属性名称和所在行号=========》》》" + caiwushuxingMap);
            /**
             * 第四步 根据报表类型id以及类型名称获取 报表模板中的具体子属性信息
             */
            try {
            	//设置成功标识 但是只要有一个为false 则验证不通过
            	boolean successFlag = true;
            	//遍历map中的键  根据key查找报表中的信息
    			for (String key : caiwushuxingMap.keySet()) {  
    				//获取财务模板中的 子属性
    				Map<String,ComonReportPakage.ModleField> dataTypeMap = null;
    				try {
    					dataTypeMap = commonGainReport.getModleFieldBySheetId(dataTypeId,key, CommonGainReport.MapKeyEnum.FIELD_NAME);
    					logger.info("根据类型id以及类型名称获取的报表类型模板中子属性map=========》》》" + dataTypeMap);
    					if (null == dataTypeMap || 0 == dataTypeMap.size() || dataTypeMap.isEmpty()) {
    		            	logger.info("根据类型id以及类型名称获取的报表类型模板中子属性map为空");
    		            	result = 8; // 成功标识
    		            	resultMap.put("result", result);
    		                return resultMap;
    					}
    				} catch (Exception e) {
    					logger.info("根据类型id以及类型名称获取的子属性map报错，第290行，报错方法为=========》》》commonGainReport.getModleFieldBySheetId", e);
    					result = 7; // 成功标识
    					resultMap.put("result", result);
    		            return resultMap;
					}
    	            Map<String, Integer> caiwuzishuxingMap = caiwushuxingMap.get(key);
    	            //校验财务模板中 每个类型中的子属性是否包含在 报表类型的字段中；若不包含说明财务模板与该报表类型不匹配
//    	            List<String> caiwuzishuxinglist = new ArrayList<String>();
    	    		List<String> dataTypeshuxinglist = new ArrayList<String>();
    	            for (String dataTypekey : dataTypeMap.keySet()) {
    	            	dataTypeshuxinglist.add(dataTypekey.trim());
    	            }
    	            boolean flag = false;
    	            for (String caiwuzishuxingkey : caiwuzishuxingMap.keySet()) {
    	            	int falseNum = (Integer)caiwuzishuxingMap.get(caiwuzishuxingkey);
    	            	flag = dataTypeshuxinglist.contains(caiwuzishuxingkey);
    	            	if (!flag) {
    	            		resultMap.put("falseKey", caiwuzishuxingkey);
    	            		resultMap.put("falseNum", falseNum+1);
							break;
						}
    	            }
    	            if (!flag) {
    	            	logger.info("上传Excel文件校验不通过，原因============>报表数据字段中不包含某个财务模板字段");
    	            	successFlag = false;
    	            	break;
					}
    			}
    			if (successFlag) {//匹配成功
    				result = 0; // 成功标识
    				resultMap.put("result", result);
				} else {
					logger.info("上传Excel文件校验不通过，原因============>excel财务模板与报表模板比对校验不匹配");
					result = 10; // 成功标识
					resultMap.put("result", result);
		            return resultMap;
				}
                
			} catch (Exception e) {
				logger.info("上传Excel文件失败，失败原因============>excel财务模板与报表模板比对校验报错", e);
				result = 7; // 成功标识
				resultMap.put("result", result);
	            return resultMap;
			}
		}catch(Exception e){
            logger.info("上传Excel文件报错，错误原因============>excel财务模板解析报错", e);
            result = 6; // 成功标识
            resultMap.put("result", result);
            return resultMap;
        }
        return resultMap;
	}
	
	/**
	 * 
	 * @描述：匹配成功，开始进行文件上传并入库操作
	 * @创建时间：  2017年9月27日
	 * @作者：武向楠
	 * @参数： @param request
	 * @参数： @param response
	 * @参数： @param excelFile
	 * 异常类型：
	 * 0-"上传成功"
	 * 1-"文件为空"
	 * 2-"上传文件中只有标题没有数据"
	 * 3-"上传的文件格式不正确"
	 * 4-"反射转换异常，上传失败，请联系管理员"
	 * 5-"上传的文件数据在数据库中已经存在"
	 * @参数： @return
	 * @参数： @throws Exception      
	 * @return Map<String,Object>     
	 * @throws
	 */
	public int uploadFinancialTemplate(HttpServletRequest request, 
			HttpServletResponse response,MultipartFile excelFile,int dataTypeId) throws Exception{
		int result = 4;
		//获取当前登录用户信息
		UserBg user = (UserBg) ControllerUtil.getSessionUser(request);
//		ImportExecl poi = new ImportExecl(excelFile);
		String newFileName = null;
		//文件保存目录路径
		String savePath = PropertiesUtil.getData("file-path.properties","financial_analy_template_path");
		if (!savePath.endsWith("/")) {// 结尾是否以"/"结束
			savePath = savePath + File.separator;
		}
		File saveDirFile = new File(savePath);
    	if (!saveDirFile.exists()) {
    		saveDirFile.mkdirs();
    	}
    	//最大文件大小 - 30M
    	//long maxSize = 30000000L;
    	try {
	    	if(!ServletFileUpload.isMultipartContent(request)){
	    		logger.info("上传文件为空");
	    		result = 1;
	            return result;
	    	}
	    	if (null == excelFile || excelFile.isEmpty()){
	    		logger.info("没有得到上传文件-上传文件中只有标题没有数据!");
	    		result = 2;
	            return result;
	    	}
	    	//检查扩展名
	    	String fileName = excelFile.getOriginalFilename();
	    	//扩展名
	    	String fileExt = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
	    	fileExt = fileExt.toLowerCase();//扩展名转化为小写
	    	fileName = fileName.substring(0, fileName.lastIndexOf("."));
	    	if(EXCEL_XLS != fileExt && !EXCEL_XLS.equals(fileExt) && EXCEL_XLSX != fileExt && !EXCEL_XLSX.equals(fileExt)){
	        	logger.info("上传文件扩展名是不允许的扩展名!");
	        	result = 3;
	            return result;
			}
	    	SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		    newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;//保存的图像名
		    File uploadedFile = new File(savePath, newFileName);//创建新图片文件
		    excelFile.transferTo(uploadedFile);//保存图片内容
		    //上传成功 将信息存入数据库
		    FinancialAnalyBean financialAnalyBean = new FinancialAnalyBean();
		    financialAnalyBean.setName(fileName);
		    financialAnalyBean.setTemplateName(newFileName);
		    financialAnalyBean.setPath(savePath);
		    financialAnalyBean.setState(0);
		    financialAnalyBean.setIsDel(0);
		    financialAnalyBean.setCreater(user.getLoginName());
		    financialAnalyBean.setCreateTime(new Date());
		    financialAnalyBean.setReportTypeId(dataTypeId);
		    //存储财务模板信息
		    int saveResult = financialAnalyService.saveFinancialTemplate(financialAnalyBean);
		    if (saveResult>0) {
		    	result = 0;
			} else {
				result = 4;
			}
    	}catch (Exception e) {
			logger.error("上传文件过程中异常", e);
			result = 4;
            return result;
		}
		return result;
	}
	
	/**
	 * 
	 * @描述：匹配成功，开始进行文件更新入库操作
	 * @创建时间：  2017年9月27日
	 * @作者：武向楠
	 * @参数： @param request
	 * @参数： @param response
	 * @参数： @param excelFile
	 * 异常类型：
	 * 0-"上传成功"
	 * 1-"文件为空"
	 * 2-"上传文件中只有标题没有数据"
	 * 3-"上传的文件格式不正确"
	 * 4-"反射转换异常，上传失败，请联系管理员"
	 * 5-"上传的文件数据在数据库中已经存在"
	 * @参数： @return
	 * @参数： @throws Exception      
	 * @return Map<String,Object>     
	 * @throws
	 */
	public int updateFinancialTemplate(HttpServletRequest request, 
			HttpServletResponse response,MultipartFile excelFile,int dataTypeId,String financialId) throws Exception{
		int result = 4;
		//获取当前登录用户信息
		UserBg user = (UserBg) ControllerUtil.getSessionUser(request);
		String newFileName = null;
		//文件保存目录路径
		String savePath = PropertiesUtil.getData("file-path.properties","financial_analy_template_path");
		if (!savePath.endsWith("/")) {// 结尾是否以"/"结束
			savePath = savePath + File.separator;
		}
		File saveDirFile = new File(savePath);
    	if (!saveDirFile.exists()) {
    		saveDirFile.mkdirs();
    	}
    	//最大文件大小 - 30M
    	//long maxSize = 30000000L;
    	try {
	    	if(!ServletFileUpload.isMultipartContent(request)){
	    		logger.info("上传文件为空");
	    		result = 1;
	            return result;
	    	}
	    	if (null == excelFile || excelFile.isEmpty()){
	    		logger.info("没有得到上传文件-上传文件中只有标题没有数据!");
	    		result = 2;
	            return result;
	    	}
	    	//检查扩展名
	    	String fileName = excelFile.getOriginalFilename();
	    	//扩展名
	    	String fileExt = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
	    	fileExt = fileExt.toLowerCase();//扩展名转化为小写
	    	fileName = fileName.substring(0, fileName.lastIndexOf("."));
	        if(EXCEL_XLS != fileExt && !EXCEL_XLS.equals(fileExt) && EXCEL_XLSX != fileExt && !EXCEL_XLSX.equals(fileExt)){
	        	logger.info("上传文件扩展名是不允许的扩展名!");
	        	result = 3;
	            return result;
			}
	    	SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		    newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;//保存的图像名
		    File uploadedFile = new File(savePath, newFileName);//创建新图片文件
		    System.err.println("上传文件路径======"+savePath);
		    System.err.println("上传文件名称======"+newFileName);
		    excelFile.transferTo(uploadedFile);//保存图片内容
		    //上传成功 将信息存入数据库
		    FinancialAnalyBean financialAnalyBean = financialAnalyService.getFinancialTemplateById(Long.valueOf(financialId));
		    financialAnalyBean.setName(fileName);
		    financialAnalyBean.setTemplateName(newFileName);
		    financialAnalyBean.setPath(savePath);
		    financialAnalyBean.setModifier(user.getLoginName());
		    financialAnalyBean.setModifTime(new Date());
		    //存储财务模板信息
		    int saveResult = financialAnalyService.updateFinancialTemplate(financialAnalyBean);
		    if (saveResult>0) {
		    	result = 0;
			} else {
				result = 4;
			}
    	}catch (Exception e) {
			logger.error("上传文件过程中异常", e);
			result = 4;
            return result;
		}
		return result;
	}
	
	/**
	 * 
	 * @描述：用于判断excel空行
	 * @创建时间：  2017年9月28日
	 * @作者：武向楠
	 * @参数： @param row
	 * @参数： @return      
	 * @return boolean     
	 * @throws
	 */
    private boolean checkEmptyRow(Row row) {
        boolean flag = true;
        int nums = row.getLastCellNum();
        for(int j=0; j < nums; j++){
            Cell cell = row.getCell(j);
            String cellValue = getCellValue(cell);
            if(UsedUtil.isNotNull(cellValue)){
                flag = false;
                break;
            }
        }
        return flag;
    }
    
    /**
     * 对Excel的各个单元格的格式进行判断并转换
     */
    private static String getCellValue(Cell cell) {
        //判断是否为null或空串
        if (cell==null || cell.toString().trim().equals("")) {
            return "";
        }
        String cellValue = "";
        int cellType=cell.getCellType();
        if(cellType == Cell.CELL_TYPE_FORMULA){ //表达式类型
            cellType= evaluator.evaluate(cell).getCellType();
        }
        switch (cellType) {
        case Cell.CELL_TYPE_STRING: //字符串类型
            cellValue= cell.getStringCellValue().trim();
            cellValue=StringUtils.isEmpty(cellValue) ? "" : cellValue; 
            break;
        case Cell.CELL_TYPE_BOOLEAN:  //布尔类型
            cellValue = String.valueOf(cell.getBooleanCellValue()); 
            break; 
        case Cell.CELL_TYPE_NUMERIC: //数值类型
             if (HSSFDateUtil.isCellDateFormatted(cell)) {  //判断日期类型
            	 Date d = cell.getDateCellValue();
                 DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                 cellValue = formater.format(d);
             } else {  //否
                 cellValue = new DecimalFormat("#.######").format(cell.getNumericCellValue()); 
             } 
            break;
        default: //其它类型，取空串吧
            cellValue = "";
            break;
        }
        return cellValue.trim();
    }
    
    
}
