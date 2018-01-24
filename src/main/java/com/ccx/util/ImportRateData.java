package com.ccx.util;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ccx.dictionary.model.RateInstitution;
import com.ccx.ratedata.model.RateDataHasHashCode;
import com.ccx.ratedata.service.RateDataService;
import com.ccx.system.model.UserBg;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.ccx.dictionary.service.RateInstitutionService;
import com.ccx.dictionary.service.RateResultService;
import com.ccx.ratedata.model.RateData;

/**
 * 导入评级数据Excel
 * @author xzd
 * @date 2017/7/14
 */
public class ImportRateData {
	/*错误类型*/
	//必填项未填
	private static final Integer ERROR_TYPE_MUST_WRITE = 0;
	//格式错误
	private static final Integer ERROR_TYPE_FORMAT = 1;
	//数据重复
	private static final Integer ERROR_TYPE_REPEAT = 2;
	//评级结果不符合填写规范
	private static final Integer ERROR_TYPE_MUST_RULE = 3;

	//校验统一社会信用代码
	//统一社会信用代码18位数字或字母
	private static final String creditCodeReg = "^[A-Za-z0-9]{18}$";
	//事证号12位数字
	private static final String certificateCodeReg = "^[a-zA-Z\\d]{8}\\-[a-zA-Z\\d]$";
	//组织机构代码8位数字(或大写拉丁字母)和1位数字(或大写拉丁字母)
	private static final String orgCodeReg = "^\\d{12}$";

	//获取评级数据map
	public static Map<Integer, String> getRateDataMap() {
		Map<Integer, String> rateDataMap = new HashMap<>(8);
		//rateDataMap put value
		//统一社会信用代码
		rateDataMap.put(0, "CreditCode");
		//组织机构代码
		rateDataMap.put(1, "OrganizationCode");
		//事证号
		rateDataMap.put(2, "CertificateCode");
		//公司名称
		rateDataMap.put(3, "Name");
		//最新评级结果
		rateDataMap.put(4, "RateResultName");
		//最新评级日期
		rateDataMap.put(5, "RateTime");
		//评级机构
		rateDataMap.put(6, "RateInstitutionName");
		return rateDataMap;
	}

	/**
	 * 导入评级数据
	 * @param sheet
	 * @return
	 */
	public static Map<String, Object> impotExcel(Sheet sheet, RateDataService rateDataService,
			RateInstitutionService rateInstitutionService, RateResultService rateResultService) throws Exception{
		//评级数据列表
		List<RateData> rateDataList = new ArrayList<>();
		//error data
		List<RateData> errorList = new ArrayList<>();
		//result jsp map
		Map<String, Object> resultMap = new HashMap<>();

		if (sheet.getLastRowNum()>0) {
			Row row0 = sheet.getRow(0);

			//判断表头不为空
			boolean flag1 = getFlag(row0, 0);
			boolean flag2 = getFlag(row0, 1);
			boolean flag3 = getFlag(row0, 2);
			boolean flag4 = getFlag(row0, 3);
			boolean flag5 = getFlag(row0, 4);
			boolean flag6 = getFlag(row0, 5);

			Class<?> clazz = Class.forName("com.ccx.ratedata.model.RateData");

			if (flag1 && flag2 && flag3 && flag4 && flag5 && flag6) {
				for (int i = 1;i <= sheet.getLastRowNum();i++) {
					RateData rateData = new RateData();
					Row row = sheet.getRow(i);
					if (row != null && !"".equals(row.toString().trim())) {
						//列数量
						int cellNum = row.getLastCellNum();

						for (int j = 0;j < cellNum;j++) {
							try {
								Method method = clazz.getDeclaredMethod("set" + getRateDataMap().get(j), String.class);
								Cell cell = row.getCell(j);
								if (cell == null || "".equals(cell.toString().trim())) {
									//必填项错误
									if (j == 3 || j == 4 || j == 5 || j == 6) {
										if (rateData.getErrType() == null) {
											//错误类型-必填项为空
											rateData.setErrType(ERROR_TYPE_MUST_WRITE);
											errorList.add(rateData);
										}
									}
									continue;
								}
								//数值类型
								if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
									method.invoke(rateData, cell.getStringCellValue());
								} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
									if (DateUtil.isCellDateFormatted(cell)) {
										SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//非线程安全
										method.invoke(rateData, sdf.format(cell.getDateCellValue()));
									} else {
										method.invoke(rateData, String.valueOf(cell.getNumericCellValue()));
									}
								}else {
									if (rateData.getErrType() == null) {
										//错误类型-数据格式
										rateData.setErrType(ERROR_TYPE_FORMAT);
										errorList.add(rateData);
									}
								}
							} catch (Exception e) {
								resultMap.put("code", 500);
								//反射异常
								resultMap.put("msg", 3);
								e.printStackTrace();
								return resultMap;
							}
						}

						if (cellNum < 7) {
							if (rateData.getErrType() == null) {
								//错误类型-必填项为空
								rateData.setErrType(ERROR_TYPE_MUST_WRITE);
								errorList.add(rateData);
							}
						}
					}

					//评级机构-Excel读到的中文转化为字典表中的code
					Map<String, Integer> rateInstitutionMap = RateDataUtil.getRateInstitutionMap(rateInstitutionService);

					String insName = rateData.getRateInstitutionName();

					if (insName != null ) {
						if (rateInstitutionMap != null && rateInstitutionMap.get(insName) != null) {
							rateData.setRateInstitution(rateInstitutionMap.get(insName));
						} else {
							rateData.setRateInsNameOut(rateData.getRateInstitutionName());

							//插入新的评级机构
							insertNewRateIns(rateInstitutionService, insName);
						}
					}

					//评级结果-Excel读到的中文转化为字典表中的code
					Map<String, Integer> rateResultMap = RateDataUtil.getRateResultMap(rateResultService);

					String resultName = rateData.getRateResultName();

					if (resultName != null) {
						if (rateResultMap != null && rateResultMap.get(resultName) != null) {
							rateData.setRateResult(rateResultMap.get(resultName));
						} else {
							//错误类型有先后顺序
							if (rateData.getErrType() == null) {
								rateData.setRateResult(null);

								//错误类型-评级结果必填项填写不符合规范
								rateData.setErrType(ERROR_TYPE_MUST_RULE);
								errorList.add(rateData);
							}
						}
					}

					rateData = validateCode(rateData, rateDataService);

					//判断必填项是否都填写
					boolean flag = judgeNull(rateData);

					//必填项已经填写完
					if (flag) {
						rateDataList.add(rateData);
					}
				}

				//去重处理
				Map<String, Object> dataMap = removeCopy(rateDataList);

				if (dataMap != null && dataMap.get("importList") != null) {
					resultMap.put("rateDataList", dataMap.get("importList"));

					//重复数据
					if (dataMap.get("repeatList") != null) {
						List<RateData> repeatList = (List<RateData>)dataMap.get("repeatList");

						if (repeatList != null && repeatList.size() > 0) {
							for (RateData rateData:repeatList) {
								if (rateData != null) {
									errorList.add(rateData);
								}
							}
						}
					}
				}
				resultMap.put("errList", errorList);
				resultMap.put("code", 200);
			} else {
				resultMap.put("code", 500);
				//只有标题，没有数据
				resultMap.put("msg", 2);
				return resultMap;
			}
		}
		
		return resultMap;
	}

	/**
	 * 去重
	 */
	public static Map<String, Object> removeCopy(List<RateData> importList) {
		//重复数据
		List<RateData> repeatList = new ArrayList<>();

		Map<String, Object> resultMap = new HashMap<>();

		//过渡map
		Map<String, Object> uniqueMap = new HashMap<>();

		//通过Iterator去重
		Iterator<RateData> it = importList.iterator();

		while(it.hasNext()){
			RateData rateData = it.next();
			String key = "";
			if (rateData.getRateInstitutionName() == null || "".equals(rateData.getRateInstitutionName())) {
				key = rateData.getName() +""+ rateData.getRateResultName() +""+ rateData.getRateTime() +""+ rateData.getRateInsNameOut();
			} else {
				key = rateData.getName() +""+ rateData.getRateResultName() +""+ rateData.getRateTime() +""+ rateData.getRateInstitutionName();
			}

			if (uniqueMap.size()>0 && uniqueMap.get(key) != null && "1".equals(String.valueOf(uniqueMap.get(key)))) {
				it.remove();

				//错误类型-数据重复
				rateData.setErrType(ERROR_TYPE_REPEAT);
				repeatList.add(rateData);
			} else {
				uniqueMap.put(key, "1");
			}
		}

		//去重之后的list
		resultMap.put("importList", importList);
		//重复数据
		resultMap.put("repeatList", repeatList);

		return resultMap;
	}

	/**
	 * 获取错误map-errMap
	 */
	public static Map<String, Object> getErrMap(Cell cell, int row, int column){
		Map<String, Object> errMap = new HashMap<>();
		//列头内容
		errMap.put("errItem", cell.getStringCellValue());
		//行数
		errMap.put("errRow", row);
		//列头内容
		errMap.put("errColumn", column);

		return errMap;
	}

	/**
	 * 保存新的机构
	 * @param rateInstitutionService
	 * @param insName 机构名称
	 */
	public static void insertNewRateIns(RateInstitutionService rateInstitutionService, String insName) {
		//数据库中没有该机构名称
		int countByName = rateInstitutionService.findCountByName(insName);

		if (countByName == 0) {
			RateInstitution ins = new RateInstitution();

			ins.setName(insName);
			ins.setCreateTime(new Date());

			//调用非事务方法，来保证增加和查看业务逻辑在同一事务中
			rateInstitutionService.mySaveRateIns(ins);
		}
	}

	/**
	 * 判断必填项是否为空
	 */
	public static boolean judgeNull(RateData rateData){
		if (!StringUtils.isEmpty(rateData.getName()) && !StringUtils.isEmpty(rateData.getRateResult())
				&& !StringUtils.isEmpty(rateData.getRateTime()) && !StringUtils.isEmpty(rateData.getRateInstitutionName())) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断行头列是否为空
	 * @param row
	 * @param i
	 * @return
	 */
	public static boolean getFlag(Row row, int i){
		boolean flag = false;
		
		if (row.getCell(i) != null && row.getCell(i).getStringCellValue() != null) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 校验统一信用代码-校验格式、保证只有一个code
	 * @param rateData
	 * @param rateDataService
	 * @return rateData
	 */
	public static RateData validateCode(RateData rateData, RateDataService rateDataService){
		//deal and validate rateData code
		//统一信用代码
		Pattern creditCodePattern = Pattern.compile(creditCodeReg);
		String creditCode = rateData.getCreditCode();

		//组织机构代码
		Pattern orgCodePattern = Pattern.compile(orgCodeReg);
		String orgCode = rateData.getCertificateCode();

		//事证号
		Pattern certificateCodePattern = Pattern.compile(certificateCodeReg);
		String certificateCode = rateData.getOrganizationCode();

		if (creditCode != null) {
			boolean creditCodeFlag = creditCodePattern.matcher(creditCode).matches();

			if (creditCodeFlag) {
				int byCode = rateDataService.findByCreditCode(creditCode);

				if (byCode > 0) {
					rateData.setCreditCode(null);
				}
			} else {
				rateData.setCreditCode(null);
			}
			rateData.setOrganizationCode(null);
			rateData.setCertificateCode(null);
		} else {
			if (orgCode != null) {
				boolean orgCodeFlag = orgCodePattern.matcher(orgCode).matches();

				if (orgCodeFlag) {
					int byCode = rateDataService.findByOrganizationCode(orgCode);

					if (byCode > 0) {
						rateData.setOrganizationCode(null);
					}
				} else {
					rateData.setOrganizationCode(null);
				}
				rateData.setCreditCode(null);
				rateData.setCertificateCode(null);
			} else {
				if (certificateCode != null) {
					boolean certificateCodeFlag = certificateCodePattern.matcher(certificateCode).matches();

					if (certificateCodeFlag) {
						int byCode = rateDataService.findByCertificateCode(certificateCode);

						if (byCode > 0) {
							rateData.setCertificateCode(null);
						}
					} else {
						rateData.setCertificateCode(null);
					}
					rateData.setCreditCode(null);
					rateData.setOrganizationCode(null);
				}
			}
		}

		return rateData;
	}

	/**
	 * 测试校验统一信用代码
	 * @param args
	 */
	public static void main(String[] args) {
		String test = "123456781234";

		String creditCodeReg = "^[A-Za-z0-9]{18}$";
		String certificateCodeReg = "^[a-zA-Z\\d]{8}\\-[a-zA-Z\\d]$";
		String orgCodeReg = "^\\d{12}$";

		Pattern compile = Pattern.compile(orgCodeReg);
		Matcher matcher = compile.matcher(test);
		System.out.println(matcher.matches());
	}
	
}
