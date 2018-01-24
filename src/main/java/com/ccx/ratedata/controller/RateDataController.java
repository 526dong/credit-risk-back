package com.ccx.ratedata.controller;

import com.alibaba.fastjson.JSON;
import com.ccx.dictionary.model.RateInstitution;
import com.ccx.dictionary.model.RateResult;
import com.ccx.dictionary.service.RateInstitutionService;
import com.ccx.dictionary.service.RateResultService;
import com.ccx.enterprise.model.Enterprise;
import com.ccx.ratedata.model.RateData;
import com.ccx.ratedata.service.RateDataService;
import com.ccx.system.model.UserBg;
import com.ccx.util.*;
import com.ccx.util.page.Page;
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
@RequestMapping("/rateData")	
public class RateDataController {	
	private static final Logger logger = LogManager.getLogger(RateDataController.class);	
	
	@Autowired	
	private RateDataService rateDataService;	
	
	/*评级机构*/
	@Autowired	
	private RateInstitutionService rateInstitutionService;
	
	/*评级结果*/
	@Autowired
	private RateResultService rateResultService;
		
	/**	
	 * @author xzd
	 * @description	评级数据列表
	 */	
	@GetMapping("list")
	public ModelAndView list(HttpServletRequest request) {	
		//字典表-评级结果
		EnterpriseDataUtils.getDictionary(request, false, rateInstitutionService, rateResultService);

		return new ModelAndView("rateDataMng/rateDataList");
	}	
	
	/**	
	 * @author xzd	
	 * @description	查询评级数据列表
	 */	
	@ResponseBody
	@PostMapping("/findAll")
	public JsonResult findAll(HttpServletRequest request, Integer pageNo, Integer pageSize){
		//分页
		Page<RateData> page = new Page<RateData>();
		//查询参数
    	Map<String,Object> params = ControllerUtil.request2Map(request);
    	
		try {
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			page.setParams(params);
			
			page = rateDataService.getPageList(page);

			//成功标识
			return JsonResult.ok(page);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询评级数据列表失败:", e);
			//失败标识
			return JsonResult.error("查询评级数据列表失败！");
		}
	}
		
	/**
	 * @author xzd	
	 * @description	新增评级
	 */	
	@GetMapping("/add")	
	public ModelAndView add(HttpServletRequest request) {
		//获取字典列表-评级机构，评级结果
		EnterpriseDataUtils.getDictionary(request, true, rateInstitutionService, rateResultService);
		
		return new ModelAndView("rateDataMng/rateDataAdd");	
	}
	
	/**
	 * 验证企业名称的唯一性
	 * @return
	 */
	@PostMapping("/validateName")
	@ResponseBody
	public JsonResult validateName(HttpServletRequest request,
			@RequestParam(required = true) Integer rateDataId, @RequestParam(required = true) String name){
		int count = 0;

		boolean nameFlag = true;

		try {
			count = rateDataService.getByName(name.trim());
			//更新
			if (rateDataId > 0) {
				if (count == 1) {
					RateData rateData = rateDataService.getById(rateDataId);
					//更新时，数据库里查询到一个，说明是当前输入的，可以保存
					if (!name.equals(rateData.getName())) {
						//不是当前查询到的，重复，不保存
						nameFlag = false;
					}
				} else if (count > 1) {
					nameFlag = false;
				}
			} else {
				//新增
				if (count > 0) {
					nameFlag = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("通过企业名称查询企业失败", e);
			return JsonResult.error("通过企业名称查询企业失败！");
		}

		if (nameFlag) {
			return JsonResult.ok(null);
		} else {
			return JsonResult.error("企业名称已存在，请重新添加！");
		}
	}

	/**
	 * 验证统一信用代码的唯一性
	 * @return
	 */
	@PostMapping("/validateCode")
	@ResponseBody
	public JsonResult validateCode(HttpServletRequest request, @RequestParam(required = true) Integer rateDataId,
		   String creditCode, String organizationCode, String certificateCode){
		int count = 0;

		boolean flag1 = true;

		if (!StringUtils.isEmpty(creditCode)) {
			try {
				count = rateDataService.findByCreditCode(creditCode.trim());

				//更新
				if (rateDataId > 0) {
					if (count == 1) {
						RateData rateData = rateDataService.getById(rateDataId);
						//更新时，数据库里查询到一个，说明是当前输入的，可以保存
						if (!creditCode.equals(rateData.getCreditCode())) {
							//不是当前查询到的，重复，不保存
							flag1 = false;
						}
					} else if (count > 1) {
						flag1 = false;
					}
				} else {
					//新增
					if (count > 0) {
						flag1 = false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("通过统一信用代码查询企业失败", e);
				//失败标识
				return JsonResult.error("通过统一信用代码查询企业失败！");
			}
		}

		boolean flag2 = true;

		if (!StringUtils.isEmpty(organizationCode)) {
			try {
				count = rateDataService.findByOrganizationCode(organizationCode.trim());

				//更新
				if (rateDataId > 0) {
					if (count == 1) {
						RateData rateData = rateDataService.getById(rateDataId);
						//更新时，数据库里查询到一个，说明是当前输入的，可以保存
						if (!organizationCode.equals(rateData.getOrganizationCode())) {
							//不是当前查询到的，重复，不保存
							flag2 = false;
						}
					} else if (count > 1) {
						flag2 = false;
					}
				} else {
					//新增
					if (count > 0) {
						flag2 = false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("通过组织机构代码查询企业失败", e);
				//失败标识
				return JsonResult.error("通过组织机构代码查询企业失败！");
			}
		}

		boolean flag3 = true;

		if (!StringUtils.isEmpty(certificateCode)) {
			try {
				count = rateDataService.findByCertificateCode(certificateCode.trim());

				//更新
				if (rateDataId > 0) {
					if (count == 1) {
						RateData rateData = rateDataService.getById(rateDataId);
						//更新时，数据库里查询到一个，说明是当前输入的，可以保存
						if (!certificateCode.equals(rateData.getCertificateCode())) {
							//不是当前查询到的，重复，不保存
							flag3 = false;
						}
					} else if (count > 1) {
						flag3 = false;
					}
				} else {
					//新增
					if (count > 0) {
						flag3 = false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("通过事证号查询企业失败", e);
				return JsonResult.error("通过事证号查询企业失败!");
			}
		}

		if (flag1 && flag2 && flag3) {
			return JsonResult.ok(null);
		} else {
			if (!flag1) {
				return JsonResult.error("统一信用代码已存在，请重新添加！");
			} else if (!flag2) {
				return JsonResult.error("组织机构代码已存在，请重新添加！");
			} else {
				return JsonResult.error("事证号已存在，请重新添加！");
			}
		}
	}
	
	/**
	 * @author xzd	
	 * @throws Exception 
	 * @description	新增评级-数据库保存
	 */	
	@ResponseBody
	@PostMapping("/doAdd")	
	public JsonResult doAdd(HttpServletRequest request, RateData rateData) {
		//获取当前登录用户信息
		UserBg user = (UserBg) ControllerUtil.getSessionUser(request);
		//添加创建人
		if (null != user) {
			//添加创建人
			rateData.setCreatorName(user.getLoginName());
		}

		//创建时间
		rateData.setCreateTime(new Date());
		
		try {	
			rateDataService.save(rateData);
			//成功标识
			return JsonResult.ok(null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增评级-数据库保存失败", e);
			//失败标识
			return JsonResult.error("新增评级-数据库保存失败!");
		}	
	}
	
	/**
	 * 下载评级模板
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/downloadExcel")
    public void excelStandardTemplateOut(HttpServletRequest request, HttpServletResponse response) throws IOException{
		//Excel模板所在的路径
		String filePath = request.getSession().getServletContext().getRealPath("/")
			+ "WEB-INF/views/rateDataMng/rateDataTemplate.xlsx";
        
        File file = new File(filePath);
        
        //设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        
        try {
        	//下载文件的名称
            response.setHeader("Content-Disposition", 
            		"attachment;filename="+ new String(("评级数据模版" + ".xlsx").getBytes(), "iso-8859-1"));
        } catch (UnsupportedEncodingException e) {
           e.printStackTrace();
        }
        
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            bos = new BufferedOutputStream(out);
            
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            throw e;
        } finally {
            if (bis != null) {
				bis.close();
			}
            if (bos != null) {
				bos.close();
			}
        }
	}
	
	/**
	 * @author xzd	
	 * @description	导入评级数据-页面入口
	 */	
	@GetMapping("/importRateDataExcel")	
	public ModelAndView importRateDataExcel(HttpServletRequest request) {
		return new ModelAndView("rateDataMng/rateDataImport");
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
	@PostMapping("/doImportRateDataExcel")
	public String doImportRateDataExcel(MultipartFile excelFile,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String, Object> resultMap = new HashMap<>();

		//获取当前登录用户信息
		UserBg user = (UserBg) ControllerUtil.getSessionUser(request);

		//Excel导入的评级数据列表
		List<RateData> rateDataList = new ArrayList<>();
		
		if (excelFile.isEmpty()) {
            resultMap.put("code", 500);
            //文件为空
            resultMap.put("msg", 1);
            return JSON.toJSONString(resultMap);
	    }
		
		if (excelFile != null) {
            try {
                // 初始化一个工作簿
                Workbook wb = null;
				try {
					InputStream inputStream = excelFile.getInputStream();
					wb = new HSSFWorkbook(inputStream);
				} catch (Exception e) {
					InputStream inputStream = excelFile.getInputStream();
					wb = new XSSFWorkbook(inputStream);
					e.printStackTrace();
				}
                
                if (wb.getNumberOfSheets() != 0) {
                	Sheet sheet = wb.getSheetAt(0);
                	
                	//导入Excel内容
					Map<String, Object> rateDataMap = ImportRateData.impotExcel(sheet, rateDataService, rateInstitutionService, rateResultService);

					if (rateDataMap != null) {
						if (rateDataMap.get("code") != null) {
							if ("200".equals(String.valueOf(rateDataMap.get("code")))) {
								rateDataList = (List<RateData>) rateDataMap.get("rateDataList");
							} else if ("500".equals(String.valueOf(rateDataMap.get("code")))) {
								resultMap.put("code", 500);
								resultMap.put("msg", rateDataMap.get("msg"));
								return JSON.toJSONString(resultMap);
							}
						}
					}

					//错误数据
					List<RateData> errList = (List<RateData>) rateDataMap.get("errList");

					//存入会话session
					HttpSession session = request.getSession(true);

                	if (rateDataList != null && rateDataList.size()>0) {
						List<RateData> list = new ArrayList<>();
						for (RateData data:rateDataList) {
							//添加创建人
							if (null != user) {
								//添加创建人
								data.setCreatorName(user.getLoginName());
								//添加创建时间
								data.setCreateTime(new Date());
							}
							list.add(data);
						}

						//Excel导入和数据库去重
						List<RateData> databaseList =  rateDataService.getList();

						Map<String, Object> uniqueMap = new HashMap<>();

						for (RateData rateData:databaseList) {
							String key = "";
							//评级时间处理
							String rateTime = "";
							if (rateData.getRateTime() != null){
								//有点的话就进行处理，没有的话就不做处理
								if(rateData.getRateTime().contains(".")){
									rateTime = rateData.getRateTime().substring(0, rateData.getRateTime().indexOf("."));
								} else if (rateData.getRateTime().contains("/")) {
									rateTime = rateData.getRateTime().replaceAll("/", "-");
								} else {
									if (rateData.getRateTime().length() > 10) {
										rateTime = rateData.getRateTime();
									} else {
										rateTime = rateData.getRateTime()+" 00:00:00";
									}
								}
							}
							if (rateData.getRateInstitutionName() == null || "".equals(rateData.getRateInstitutionName())) {
								key = rateData.getName() +""+ rateData.getRateResultName() +""+ rateTime +""+ rateData.getRateInsNameOut();
							} else {
								key = rateData.getName() +""+ rateData.getRateResultName() +""+ rateTime +""+ rateData.getRateInstitutionName();
							}

							uniqueMap.put(key, "1");
						}

						Iterator<RateData> it = list.iterator();

						//重复数据
						List<RateData> repeatList = new ArrayList<>();

						while(it.hasNext()){
							RateData rateData = it.next();
							String key = "";
							if (rateData.getRateInstitutionName() == null || "".equals(rateData.getRateInstitutionName())) {
								key = rateData.getName() +""+ rateData.getRateResultName() +""+ rateData.getRateTime() +""+ rateData.getRateInsNameOut();
							} else {
								key = rateData.getName() +""+ rateData.getRateResultName() +""+ rateData.getRateTime() +""+ rateData.getRateInstitutionName();
							}

							if (uniqueMap.size() > 0 && uniqueMap.get(key) != null){
								if ("1".equals(String.valueOf(uniqueMap.get(key)))) {
									it.remove();

									rateData.setErrType(2);
									repeatList.add(rateData);
								}
							}
						}

						if (repeatList != null && repeatList.size() > 0) {
							for (RateData data : repeatList) {
								errList.add(data);
							}
						}

						//删除以前的
						session.setAttribute("errList", errList);

						resultMap.put("successNum", list.size());
						resultMap.put("errNum", errList.size());

						if (list != null && list.size() > 0) {
							rateDataService.batchInsert(list);
							resultMap.put("code", 200);
						} else {
							//所导入的数据在数据库中已经存在
							resultMap.put("code", 500);
							resultMap.put("msg", 4);
						}
					} else {
						//删除以前的
						session.setAttribute("errList", errList);

						resultMap.put("successNum", 0);
						resultMap.put("errNum", errList.size());

						//不存在正确数据
						resultMap.put("code", 200);
					}
                }
            } catch (Exception e) {
				e.printStackTrace();
				logger.error("文件解析出错！", e);
			}
		}
		
		return JSON.toJSONString(resultMap);
	}

	/**
	 * 导出
	 */
	@GetMapping("/exportRateData")
	public void exportRateData(HttpServletRequest request, HttpServletResponse response){
		//从session中获取错误信息
		HttpSession session = request.getSession();
		List<RateData> errList = (List<RateData>)session.getAttribute("errList");

		String fileName = null;

		//处理IE乱码
		String userAgent = request.getHeader("user-agent");
		if (userAgent != null && userAgent.indexOf("Firefox") >= 0 || userAgent.indexOf("Chrome") >= 0 || userAgent.indexOf("Safari") >= 0) {
			try {
				fileName = new String("评级错误数据".getBytes("UTF-8"),"ISO-8859-1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			try {
				fileName = URLEncoder.encode("评级错误数据", "UTF8"); //其他浏览器
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		if (errList != null && errList.size() > 0) {
			try {
				response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + ".xls\"");
				response.setContentType("application/msexcel;charset=UTF-8");// 设置类型
				OutputStream out = response.getOutputStream();

				ExportExcel<List<RateData>> ex = new ExportExcel<>();
				ex.exportExcel(errList, out);

				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("导出评级错误数据失败！", e);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**	
	 * @author xzd
	 * @description	更新评级
	 */	
	@GetMapping("/update")	
	public ModelAndView update(HttpServletRequest request) {
		//评级id
		String id = request.getParameter("id");
		boolean idflag = id != null && !"".equals(id);
		
		if (idflag) {
			try {
				//通过id查询评级数据
				RateData rateData = rateDataService.getById(Integer.parseInt(id));
				
				request.setAttribute("rateData", rateData);
			} catch (Exception e) {
				logger.error("查询评级数据信息失败", e);
				e.printStackTrace();
			}
		}
		
		//获取字典列表-评级机构，评级结果
		EnterpriseDataUtils.getDictionary(request, true, rateInstitutionService, rateResultService);
		
		return new ModelAndView("rateDataMng/rateDataUpdate");	
	}
	
	/**	
	 * @author xzd 	
	 * @throws Exception 
	 * @description	更新评级-数据库操作
	 */	
	@ResponseBody	
	@PostMapping("/doUpdate")	
	public JsonResult doUpdate(RateData rateData) {
		//更新时间
		rateData.setCreateTime(new Date());
			
		try {	
			rateDataService.update(rateData);
			//成功标识
			return JsonResult.ok(null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("更新评级-数据库操作失败", e);
			//失败标识
			return JsonResult.error("更新评级-数据库操作失败!");
		}
	}
 	
	/**	
	 * @author xzd 	
	 * @description	删除评级
	 */	
	@ResponseBody	
	@PostMapping("/delete")	
	public JsonResult delete(HttpServletRequest request) {
		//评级id
		String id = request.getParameter("id");

		if (!StringUtils.isEmpty(id)) {
			try {	
				rateDataService.deleteById(Integer.parseInt(id));
				//成功标识
				return JsonResult.ok(null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("删除评级-数据库操作失败", e);
				//失败标识
				return JsonResult.error("删除评级-数据库操作失败!");
			}
		} else {
			return JsonResult.error("评级id为空!");
		}
	}
}	
