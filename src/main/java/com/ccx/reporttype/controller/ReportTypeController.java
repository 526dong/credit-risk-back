package com.ccx.reporttype.controller;

import com.alibaba.fastjson.JSON;
import com.ccx.Constans;
import com.ccx.dictionary.model.RateInstitution;
import com.ccx.dictionary.model.RateResult;
import com.ccx.dictionary.service.RateInstitutionService;
import com.ccx.dictionary.service.RateResultService;
import com.ccx.enterprise.model.IndustryShow;
import com.ccx.enterprise.service.EnterpriseIndustryService;
import com.ccx.index.model.AbsIndexModel;
import com.ccx.ratedata.model.RateData;
import com.ccx.ratedata.service.RateDataService;
import com.ccx.reportmsg.manager.CommonGainReport;
import com.ccx.reportmsg.manager.ComonReportPakage;
import com.ccx.reporttype.model.AbsEnterpriseReportCheck;
import com.ccx.reporttype.model.AbsEnterpriseReportType;
import com.ccx.reporttype.pojo.AbsReportType;
import com.ccx.reporttype.service.AbsEnterpriseReportCheckService;
import com.ccx.reporttype.service.ReportTypeService;
import com.ccx.system.model.UserBg;
import com.ccx.util.*;
import com.ccx.util.page.Page;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * 评级数据管理
 * @author xzd
 * @date 2017/7/13
 */
@Controller	
@RequestMapping("/rtype")
public class ReportTypeController {
	private static final Logger logger = LogManager.getLogger(ReportTypeController.class);
	
	@Autowired	
	private ReportTypeService reportTypeService;

	@Autowired
	private AbsEnterpriseReportCheckService reportCheckService;

    @Autowired
    private CommonGainReport commonGainReport;

	/**	
	 * @author xzd	
	 * @description	评级数据列表
	 */	
	@GetMapping("/index")
	public ModelAndView list(HttpServletRequest request) {	
		//评级结果
/*		List<RateResult> resultList = rateResultService.getList();
		request.setAttribute("resultList", resultList);*/
		
		return new ModelAndView("reporttype/typelist");
	}	
	/**
	 * @author xzd
	 * @description	评级数据列表
	 */
	@GetMapping("/detail")
	public ModelAndView detail(HttpServletRequest request,Integer typeId) {
		if(typeId!=null) {
			List<ComonReportPakage.Modle> list = commonGainReport.getModles(typeId);
			List<String> sheets = new ArrayList<>();
			List<Map<String, Object>> res = new ArrayList<>();
			dealWithDetail(list, res, sheets);
			request.setAttribute("sheets", sheets);
			request.setAttribute("typeId", typeId);
			request.setAttribute("res", res);
			request.setAttribute("name", reportTypeService.getTypeByid(typeId).getName());
			System.out.println("json::::" + res);
		}
        return new ModelAndView("reporttype/typedetail");
	}
	@ResponseBody
	@PostMapping("/getDetail")
	public List<Map<String,Object>> getDetail(HttpServletRequest request, HttpServletResponse response,Integer typeId) throws Exception{
		List<ComonReportPakage.Modle> list = commonGainReport.getModles(typeId);
		List<Map<String,Object>>  res=new ArrayList<>();
		dealWithDetail(list,res,null);
		System.out.println("json::::"+JsonUtils.toJson(res));
		return  res;
	}
	private void dealWithDetail(List<ComonReportPakage.Modle> list, List<String > sheets){

		for(ComonReportPakage.Modle str:list){
			sheets.add(str.getSheetName());
		}
	}
	private void dealWithDetail(List<ComonReportPakage.Modle> list, List<Map<String,Object>>  res, List<String > sheets){

        for(ComonReportPakage.Modle str:list){
           if(sheets!=null) sheets.add(str.getSheetName());
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("lie",str.getFristClumn());
            List<ComonReportPakage.ModleField> fields  = str.getFields();
           Map<Integer,List<ComonReportPakage.ModleField>> m=new LinkedHashMap<>();
           if(fields!=null) {
               for (ComonReportPakage.ModleField st : fields) {
                   Integer hang = Integer.valueOf(st.getExcelName().substring(1));
                   List<ComonReportPakage.ModleField> modleFieldList = m.get(hang);
                   if (modleFieldList == null) {
                       modleFieldList = new ArrayList<>();
                       m.put(hang, modleFieldList);
                       if(st.getColunmNo()==2){
                           modleFieldList.add(new ComonReportPakage.ModleField());
                       }
                   }
                   modleFieldList.add(st);
               }
               List<List<ComonReportPakage.ModleField>> rlist = new ArrayList<>();
               for (Map.Entry<Integer, List<ComonReportPakage.ModleField>> stt : m.entrySet()) {
                   rlist.add(stt.getValue());
               }
               map.put("value", rlist);
           }
            res.add(map);
        }
    }

	/**	
	 * @author xzd	
	 * @description	查询评级数据列表
	 */	
	@ResponseBody
	@PostMapping("/findAll")
	public Map<String, Object> findAll(HttpServletRequest request, Integer pageNo, Integer pageSize){
		//result jsp map
	Map<String, Object> resultMap = new HashMap<>();
		//分页
		Page<AbsReportType> page = new Page<AbsReportType>();
		//查询参数
    	//Map<String,Object> params = ControllerUtil.request2Map(request);
    	
		try {
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			page = reportTypeService.getPageList(page);
			//成功标识
			resultMap.put("code", 200);
			resultMap.put("page", page);
		} catch (Exception e) {
			//失败标识
			resultMap.put("code", 500);
			e.printStackTrace();
			logger.error("查看评级数据列表:", e);
		}
		return resultMap;
	}




	@GetMapping("toImport")
	public ModelAndView toImport(HttpServletRequest request) {


		return new ModelAndView("reporttype/typeimport");
	}
	@GetMapping("/toUpdate")
	public ModelAndView toUpdate(HttpServletRequest request,Integer typeId) {
		if(typeId!=null){
			request.setAttribute("typeId",typeId);
			AbsEnterpriseReportType type = reportTypeService.getTypeByid(typeId);
			request.setAttribute("typeName",type.getName());
		}
		return new ModelAndView("reporttype/typeimport");
	}

	/**
	 * 下载评级示例模板
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/download")
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) throws IOException{

        try {
			DownloadUtil.download(Constans.TYPE_EXCEL_MODLE,"评级类型模型.xls",response);
        } catch (Exception e) {
           e.printStackTrace();
        }

	}
	/**
	 * 下载类型模板
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/downloadType")
    public void downloadType(HttpServletRequest request, HttpServletResponse response,Integer id) throws IOException{
        try {
			File file=DownloadUtil.findFile(Constans.TYPE_EXCEL_SAVE_DIR,id+".");
			System.out.println("file;:::"+file);
			if(file!=null){
				AbsEnterpriseReportType type = reportTypeService.getTypeByid(id);
				/**
				 * 取得文件的后缀名
				 */
				String ext = (file.getName()).substring((file.getName()).lastIndexOf(".") + 1).toLowerCase();
				System.out.println("ext;:::"+ext);
				DownloadUtil.download(Constans.TYPE_EXCEL_SAVE_DIR+file.getName(),
						(type!=null&&type.getName()!=null)?type.getName()+"."+ext:file.getName(),response);
			}
        } catch (Exception e) {
           e.printStackTrace();
        }

	}
	/**
	 * @Description:校验新增报表类型时的类型名称唯一性
	 * @Author: wxn
	 * @Date: 2018/1/18 15:14:35
	 * @Param: [request, pageNo, pageSize]
	 * @Return java.util.Map<java.lang.String,java.lang.Object>
	 */
	@ResponseBody
	@PostMapping("/checkFileName")
	public String checkFileName(HttpServletRequest request){
		//获取请求参数
		Map<String,Object> map = ControllerUtil.request2Map(request);
		String result = "error";
		try {
			result = reportTypeService.checkFileName(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON.toJSONString(result);
	}


	/**
	 * 导入评级数据
	 * @param excelFile
	 * 异常类型：
	 * 1-"文件为空"
	 * 2-"上传文件中只有标题没有数据"
	 * 3-"反射转换异常，上传失败，请联系管理员"
	 * 4-"上传的文件数据在数据库中已经存在"
	 * @return resultMap
	 */
	@ResponseBody
	@PostMapping("/import")
	public String doImport(MultipartFile excelFile,
			HttpServletRequest request, HttpServletResponse response,String name) throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		if(name==null){
			resultMap.put("code",111);
			resultMap.put("msg","参数为空");
		}
		try {
			resultMap=reportTypeService.saveOrUpdateType(excelFile,request,name,null);
		}catch (Exception e){
			resultMap.put("code",999);
			resultMap.put("msg","系统异常");
			e.printStackTrace();
		}
		return  JsonUtils.toJson(resultMap);
	}
	@ResponseBody
	@PostMapping("/update")
	public Map<String, Object> updata(MultipartFile excelFile,
			HttpServletRequest request, HttpServletResponse response,String name,Integer oldTypeId) throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		if(name==null&&oldTypeId==null){
			resultMap.put("code",111);
			resultMap.put("msg","参数为空");
		}
		try {
			resultMap=reportTypeService.saveOrUpdateType(excelFile,request,name,oldTypeId);
		}catch (Exception e){
			resultMap.put("code",999);
			resultMap.put("msg","系统异常");
			e.printStackTrace();
		}
		System.out.println(JsonUtils.toJson(resultMap));
		return  resultMap;
	}
	/**
	 * @author xzd
	 * @description	评级数据列表
	 */
	@GetMapping("/setind")
	public ModelAndView setind(HttpServletRequest request,Integer typeId) {
		if(typeId!=null){
			request.setAttribute("list",reportTypeService.getTypes(typeId));
			request.setAttribute("typeId",typeId);
			AbsEnterpriseReportType type=reportTypeService.getTypeByid(typeId);
			request.setAttribute("typeName",type.getName());
		}
		return new ModelAndView("reporttype/typeMatch");
	}

	/*
	* @author
	* @description	查看模型行业列表列表
	* @date
	*/
	@RequestMapping(value = "/modelIndustryList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> modelIndustryList(Integer pageNo, Integer pageSize, String name, String modelFlag) {
		Map<String, Object> resultMap = new HashMap<>();
		Page<IndustryShow> page = new Page<IndustryShow>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		try {
			page.setParams(params);
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			page = reportTypeService.getPageList2(page);
			resultMap.put("code", 100);
			resultMap.put("page", page);
		} catch (Exception e) {
			logger.error("模型行业列表:", e);
			resultMap.put("code", 500);
		}
		return resultMap;
	}
	/*
	* @author
	* @description	类型匹配行业关联关系
	* @date
	*/
	@RequestMapping(value = "/typeMatchSave", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> modelIndustryList( String ids,Integer typeId) {
		Map<String, Object> resultMap = new HashMap<>();
		System.out.println("ids::::::::::"+ids);
		System.out.println("typeId::::::::::"+typeId);
		try {
			resultMap=reportTypeService.saveTypeAndIns(ids,typeId);
		}catch (Exception e){
			resultMap.put("code",999);
			resultMap.put("msg","系统异常");
			e.printStackTrace();
		}
		return resultMap;
	}
	@RequestMapping(value = "/status/{status}")
	@ResponseBody
	public Map<String, Object> updateStatus(@PathVariable String status,Integer typeId) {
		Map<String, Object> resultMap = new HashMap<>();
		if(status==null){
			resultMap.put("code",999);
			resultMap.put("msg","系统异常");
		}
		try {
			resultMap=reportTypeService.updateStatus(typeId, status);
		}catch (Exception e){
			e.printStackTrace();
			resultMap.put("code",999);
			resultMap.put("msg","系统异常");
		}
		return resultMap;
	}

	@GetMapping("/setRule")
	public ModelAndView setRule(@RequestParam(required = true) Integer id) {
		ModelAndView mnv = new ModelAndView("reporttype/typeRule");

		try {
			AbsEnterpriseReportCheck reportCheck = new AbsEnterpriseReportCheck();
			reportCheck.setReportType(id);
			List<AbsEnterpriseReportCheck> list = reportCheckService.getList(reportCheck);
			mnv.addObject("typeId", id);
			mnv.addObject("ruleList", JSON.toJSON(list));
		}  catch (Exception e) {
			logger.error("报表规则列表：", e);
		}

		return mnv;
	}

	@PostMapping("/setRuleSave")
	@ResponseBody
	public JsonResult setRuleSave(@RequestParam(required = true) Integer typeId,
									String formulaHtmlContent,
									String formulaContent) {
		try {
			reportCheckService.saveSetRule(typeId, formulaHtmlContent, formulaContent);
			return JsonResult.ok(null);
		}  catch (Exception e) {
			logger.error("报表规则列表：", e);
			if (e instanceof MyRuntimeException) {
				return JsonResult.error(400, null,e.getMessage());
			} else {
				return JsonResult.error(null);
			}
		}
	}

	@RequestMapping(value = "/exelupload", method = RequestMethod.POST)
	@ResponseBody
	public String upload( HttpServletRequest request) throws IOException {
		if ("application/octet-stream".equals(request.getContentType())) { //HTML 5 上传
			try {
				try {
					String dispoString = request.getHeader("Content-Disposition");
					/*int iFindStart = dispoString.indexOf("filename=\"") + 10;
					int iFindEnd = dispoString.indexOf("\"", iFindStart);*/
					String sFileName = dispoString.split("filename=")[1];
					System.out.println("sfilename::::::"+sFileName);

				}catch (Exception e){
					e.printStackTrace();
				}
				int i = request.getContentLength();
				byte buffer[] = new byte[i];
				int j = 0;
				while (j < i) { //获取表单的上传文件
					int k = request.getInputStream().read(buffer, j, i - j);
					j += k;
				}
				FileOutputStream fileOutputStream = new FileOutputStream("E://data//ceshi//MMM"+System.currentTimeMillis()+".xlsx");
				fileOutputStream.write(buffer,0,buffer.length);
				fileOutputStream.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return "upload";
	}
}
