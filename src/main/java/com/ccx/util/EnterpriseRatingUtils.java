package com.ccx.util;

import com.ccx.businessmng.service.AbsEnterpriseReportTypeService;
import com.ccx.index.model.AbsIndexFormula;
import com.ccx.index.service.AbsIndexFormulaService;
import com.ccx.reportmsg.manager.CommonGainReport;
import com.ccx.reportmsg.manager.ComonReportPakage;
import com.ccx.util.page.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 企业评级工具类
 * @author sunqi
 *
 */
public class EnterpriseRatingUtils {
	@Autowired
	private AbsEnterpriseReportTypeService reportTypeService;

	@Autowired
	private CommonGainReport commonGainReport;

	private static Logger logger = LogManager.getLogger(EnterpriseRatingUtils.class);

	//private static Map<String, String> formulaMap = null;

	private static final String SPILT_OPERATING_PATTEN = "[,()+*/-]";//分隔运算符
	
	private static final String SPILT_COLUMN_BASE = "_";//分割字段
	

	public void validateFormula(AbsIndexFormula formula, Map<String, String> formulaMap) throws Exception {
		int yearLen = formula.getYearLen();
		int reportTypeId = formula.getReportTypeId();
		//报表名称和字段的map
		Map<String, Object> reportModelMap = new HashMap<>();

		List<ComonReportPakage.Modle> modleList = commonGainReport.getModles(reportTypeId);
		if (null == modleList) {
			return;
		}
		for (ComonReportPakage.Modle modle : modleList) {
			String reportName = modle.getSheetName();
			//字段map
			Map<String, Object> reportColumnMap = new HashMap<>();

			List<ComonReportPakage.ModleField> fieldList = modle.getFields();
			if (null == fieldList) {
				continue;
			}
			for (ComonReportPakage.ModleField field : fieldList) {
				String columnName = field.getName();

				//字段map
				reportColumnMap.put(columnName, true);
			}
			//报表name和字段map
			reportModelMap.put(reportName, reportColumnMap);
		}

		//公式验证
		if (UsedUtil.isNotNull(formula.getFormulaContent1())) {
			validateFormulContent(formula.getFormulaContent1(), formulaMap, reportModelMap, 1);
		}
		if (--yearLen <=0) {return;}

		if (UsedUtil.isNotNull(formula.getFormulaContent2())) {
			validateFormulContent(formula.getFormulaContent2(), formulaMap, reportModelMap, 2);
		}
		if (--yearLen <=0) {return;}

		if (UsedUtil.isNotNull(formula.getFormulaContent3())) {
			validateFormulContent(formula.getFormulaContent3(), formulaMap, reportModelMap, 3);
		}
	}

	public Object validateFormulContent(String formulaContent, Map<String, String> formulaMap, Map<String, Object> reportModelMap, int year) throws Exception {
		ArrayList<String> functionList = new ArrayList<>();
		functionList.add("方差");
		functionList.add("根号");
		Double operationValu;
		logger.info("替换前的公式："+formulaContent);

		if (formulaContent.indexOf("+") == 0
				|| formulaContent.indexOf("-") == 0
				|| formulaContent.indexOf("*") == 0
				|| formulaContent.indexOf("/") == 0
				|| formulaContent.indexOf(",") == 0
				) {
			throw new MyRuntimeException("公式最左侧不能为运算符号");
		}

		//将字段换算成数字
		//String[] reportColumn = formulaContent.split(SPILT_OPERATING_PATTEN);
		String[] reportColumn = split(formulaContent);
		for(String c: reportColumn){
			Pattern pBlank = Pattern.compile("\\s*|\t|\r|\n");
			Matcher mBlan = pBlank.matcher(c);
			//c = mBlan.replaceAll("");

			if(!"".equals(c)){
				String column = c.trim();
				Pattern pConstant = Pattern.compile("^\\d+(\\.\\d+)?$");
				Matcher mConstant = pConstant.matcher(column);

				//找到常量引用
				if (mConstant.find()) {
					continue;
				}

				//找到函数引用
				if (functionList.contains(column)) {
					continue;
				}

				//找到公式引用
				boolean continueFlag = false;
				if (formulaMap.containsKey(column)) {
					if (!formulaMap.containsKey(column+":"+year)) {
						throw new MyRuntimeException("引用的公式："+column+" 定义的年份小于当前公式定义的年份："+year+"  年！");
					}
//					int formulaYearLen = new Integer(formulaMap.get(column));
//					if (formulaYearLen < year) {
//						throw new MyRuntimeException("引用的公式："+column+"定义年份："+formulaYearLen+"  小于当前公式定义的年份："+year+"无法评级！");
//					}

					continueFlag = true;
					logger.info("/******************************/进入嵌套公式引用："+column);
					//调用递归计算
					formulaContent = formulaContent.replace(column,
							validateFormulContent(
									formulaMap.get(column+":"+year),
									formulaMap,
									reportModelMap,
									year).toString());
				}
				
				//递归后的无用循环结束
				if (continueFlag) {
					//因为公式虽然被替换了但是当前for循环还停在这一部
					logger.info("/******************************/退出嵌套公式替换："+column);
					continue;
				}

				//正常的取值
				String tableName = getTableName(column);
				Map<String, Object> reportColumnMap = checkTableName(column, tableName, reportModelMap);
				String columnName = getMethodName(column);
				String value =  getColumnValue(column, columnName, reportColumnMap);

				formulaContent = formulaContent.replace(column, value);
				logger.info("替换后的公式："+formulaContent);
			}

		}

		//去空格
		Pattern pBlank = Pattern.compile("\\s*|\t|\r|\n");
		Matcher mBlan = pBlank.matcher(formulaContent);
		formulaContent = mBlan.replaceAll("");
		//函数拦截
		formulaContent = operatingFunctionValueByFormula(formulaContent, functionList);

		//开始计算
		operationValu = calJs(formulaContent);
		logger.info(formulaContent);
		logger.info(operationValu);
		return operationValu;
	}

	/*
	* 根据字段获取表名
	*/
	public String getTableName(String column){
		String tableName = null;
		int begin = -1;
		int end1 = column.indexOf("_上期");
		int end2 = column.indexOf("_本期");

		begin = column.indexOf("_");
		if (begin <= 1) {
			throw new MyRuntimeException("公式中现年份字段不正常！");
		}

		//开始字段
		if(end1 > -1) {
			tableName = column.substring(begin+1, end1);
		} else if(end2 > -1) {
			//结束字段
			tableName = column.substring(begin+1, end2);
		} else if(end1 == -1 && end2 == -1) {
			throw new MyRuntimeException("公式中未发现时间字段上期或上期！");
		}

		return tableName;
	}

	/*检测表名*/
	public Map<String, Object> checkTableName(String column, String tableName, Map<String, Object> reportModelMap) {
		Object o = reportModelMap.get(tableName);

		if (null == o) {
			throw new MyRuntimeException("公式["+column+"]中表["+ tableName+"]未找到！");
		}

		return (Map<String, Object>) o;
	}

	/**
	 * 整理数据库存储公式里字段 解析成get方法名
	 */
	public String getMethodName(String column){
		int begin = column.lastIndexOf("_");

		if (begin <= 2) {
			throw new MyRuntimeException("公式["+column+"]不正常");
		}

		return column.substring(begin+1);
	}

	/**
	 *  根据年，表名，字段名，条件获取字段的值
	 */
	public String getColumnValue(String column, String columnName, Map<String, Object> reportColumnMap){
		Object o = reportColumnMap.get(columnName);

		if (null == o) {
			throw new MyRuntimeException("公式["+column+"]中字段["+ columnName+"]未找到！ 请确认标点符号是否正确使用！");
		}

		return "1";
	}

	/**
	 * 计算公式中的函数的值
	 */
	private static String operatingFunctionValueByFormula(String formula, List<String> functionList) throws Exception {
		FindFunction find = null;
		String content = null;

		try {
			//遍历每个函数
			for (String fun : functionList) {
				//查找公式中是否出现次函数
				while (null != (find = getFunctionIndex(formula, fun, fun.length()))) {
					String functionName = null;
					String functionValue = null;
					Double value = null;

					functionName = find.getFuntionName();
					content = formula.substring(find.getFunctioinBegin() + fun.length() + 1, find.getFunctionEnd() - 1);
					for (String f : functionList) {
						int n = content.indexOf(f);
						if (n > -1) {
							//公式递归
							logger.info("进入嵌套函数引用："+content.substring(n));
							content = operatingFunctionValueByFormula(content, functionList);
						}
					}

					if ("MATH_POW_2".equals(fun)) {
						value = calJs(content);
						functionValue = new Double(Math.pow(value.doubleValue(), 1 / 2.0)).toString();
					} else if ("MATH_RAN".equals(fun)) {
						functionValue = calMathVan(content);
					}

					formula = formula.replace(functionName,functionValue);
					logger.info("函数替换后的公式："+formula);
				}

			}
		} catch (Exception e) {
			//logger.error("计算函数："+functionList+"异常:，参数", e, content);
			throw new MyRuntimeException("函数值异常:"+ formula+"    [请忽略1.0，1.0为校验值]");
		}
		return formula;
	}

	/*
	* 返回函数出现的后的函数的位置fun，【（】的位置，【）】的位置的
	*/
	private static FindFunction getFunctionIndex(String formula, String split, int offset) {
		FindFunction find = null;
		int i = formula.indexOf(split);

		if (i > -1) {
			find = new FindFunction();
			find.setFunctioinBegin(i);
			//查找括号的计数，
			int n = 0;
			for (int j=i+offset;j<formula.length(); j++) {
				char c = formula.charAt(j);
				if ("(".equals(String.valueOf(c))) {
					n++;
				}
				char c1 = formula.charAt(j);
				if (")".equals(String.valueOf(c1))) {
					n--;
					if (0 == n) {
						find.setFuntionName(formula.substring(i, j+1));
						find.setFunctionEnd(j+1);
						break;
					}
				}
			}
		}

		return find;
	}

	/*
	* 返回一段数学表达最终的表达式
	*/
	private static String getElementsByformula(String formula) {
		FindFunction find = null;
		String content = null;

		//括号优先级最高，先算括号
		while (null != (find = getFunctionIndex(formula, "(", 0))) {
			content = formula.substring(find.getFunctioinBegin() + 1, find.getFunctionEnd() - 1);

			boolean flag = false;
			if (content.indexOf("(") > -1) {
				//内容中还包含（
				flag = true;
				logger.info("进入括号嵌套/******************/");
				formula = formula.replace(find.getFuntionName(), getElementsByformula(content));
			}
			if (flag) {
				continue;
			}


			int i;
			for (i=0; i<content.length(); i++) {
				//乘除法优先级最高
				if ("*".equals(String.valueOf(content.charAt(i))) || "/".equals(String.valueOf(content.charAt(i)))) {
					String front = null;
					String after = null;
					String opt = null;

					opt = content.substring(i, i+1);
					//找到前一个数字
					int j;
					for (j=i-1; j>=0; j--) {
						if ("+".equals(String.valueOf(content.charAt(j)))
								|| "-".equals(String.valueOf(content.charAt(j)))
								|| "*".equals(String.valueOf(content.charAt(j)))
								|| "/".equals(String.valueOf(content.charAt(j)))) {
							front = content.substring(j+1, i);
							break;
						}
					}
					if (j <= -1) {
						front = content.substring(0, i);
					}
					System.out.print("\n合并运算："+front);

					//找到后一个数字
					for (j=i+1; j<content.length(); j++) {
						if ("+".equals(String.valueOf(content.charAt(j)))
								|| "-".equals(String.valueOf(content.charAt(j)))
								|| "*".equals(String.valueOf(content.charAt(j)))
								|| "/".equals(String.valueOf(content.charAt(j)))) {
							after = content.substring(i+1, j);
							break;
						}
					}
					if (j >= content.length()) {
						after = content.substring(i+1, content.length());
					}
					System.out.print(" "+opt+" "+after);

					String cal = front+opt+after;
					formula = formula.replace(cal, calJs(cal).toString());
					logger.info("\n合并后的公式："+formula);

				}
			}
			//没有优先级的替换括号,找到和外层相同的括号
			FindFunction f = getFunctionIndex(formula, "(", 0);
			if (null != f) {
				formula = formula.replace(f.getFuntionName(), calJs(content).toString());
			}
			logger.info("合并后的公式："+formula);
		}

		return formula;
	}

	/*
	* 计算方差
	*/
	private static String calMathVan(String formula) {
		double sum = 0.0;
		double avg = 0.0;
		double avn = 0.0;
		List<Double> list = new ArrayList<>();
		String[] elements = formula.split(",");

		for (String e: elements) {
			String s = calJs(e).toString();
			sum += Double.parseDouble(s);
			list.add(new Double(s));
		}

		avg = sum / list.size();
		for (Double d: list) {
			avn += Math.pow(Math.abs(avg - d.doubleValue()), 2);
		}

		return avn/list.size()+"";
	}

	/*
	* 计算js值
	*/
	private static Double calJs(String content) {
		Double operationValu = null;

		//开始计算
		try {
			//add lilong
			content= content.replace("次","");
			ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
			String sum = jse.eval(content).toString();
			if ("Infinity".equals(sum)) {
				throw new MyRuntimeException("分母为零！");
			}
			if ("NaN".equals(sum)) {
				throw new MyRuntimeException("结果不正常！");
			}
			operationValu = new Double(sum);
		} catch (Exception e) {
			//logger.error("公式运算非法:", e);
			if (e instanceof MyRuntimeException) {
				throw new MyRuntimeException("公式运算非法,请检查（）是否对其等个数，+-*/是否正常，错误详细："+e.getMessage()+content+"   [请忽略1.0，1.0为校验值]");
			} else {
				throw new MyRuntimeException("公式运算非法,请检查（）是否对其等个数，+-*/是否正常，错误详细："+content+"   [请忽略1，1为校验值]");
			}
		}
		return operationValu;
	}

	/*
	* 查找函数返回类
	*/
	private static class FindFunction {
		//函数的内容
		String funtionName;

		//函数的开始
		int functioinBegin;

		//函数的结束
		int functionEnd;

		public String getFuntionName() {
			return funtionName;
		}

		public void setFuntionName(String funtionConten) {
			this.funtionName = funtionConten;
		}

		public int getFunctioinBegin() {
			return functioinBegin;
		}

		public void setFunctioinBegin(int functioinBegin) {
			this.functioinBegin = functioinBegin;
		}

		public int getFunctionEnd() {
			return functionEnd;
		}

		public void setFunctionEnd(int functionEnd) {
			this.functionEnd = functionEnd;
		}
	}

	//获取所有公式(获取时不能置为null)
	/*public synchronized Map<String, String> getAllCacheFormula( AbsIndexFormulaService indexFormulaService) {
		if (null == formulaMap) {
			formulaMap = new HashMap<>();
			pageGet(1, formulaMap, indexFormulaService);
			return new HashMap<>(formulaMap);
		} else {
			return new HashMap<>(formulaMap);
		}
	}*/
	public Map<String, String> getAllCacheFormula(AbsIndexFormulaService indexFormulaService, Integer reportTypeId) {
		HashMap<String, String> formulaMap = new HashMap<>();
		try {
			pageGet(1, formulaMap, indexFormulaService, reportTypeId);
		} catch (Exception e) {
			logger.error("公式获取失败", e);
		}
		return  formulaMap;
	}

	//删除缓存(获取时不能置为null)
	/*public synchronized void deleteCacheFormula() {
		if (null != formulaMap) {
			formulaMap.clear();
			formulaMap = null;
		}
	}*/

	//查询公式
	public void pageGet(int pageNo, Map<String, String> formulaMap, AbsIndexFormulaService indexFormulaService, Integer reportTypeId) {
		Page<AbsIndexFormula> page = new Page<>();
		page.setPageNo(pageNo);
		page.setPageSize(1000);
		Map<String, Object> params = new HashMap<>();
		params.put("reportTypeId", reportTypeId);
		page.setParams(params);
		page = indexFormulaService.getPageList(page);

		List<AbsIndexFormula> formulaList = page.getRows();
		for (AbsIndexFormula absIndexFormula: formulaList) {
			//父公式
			formulaMap.put(absIndexFormula.getFormulaName(), absIndexFormula.getYearLen()+"");
			for (AbsIndexFormula formula : absIndexFormula.getFormulaList()) {
				//子公式
				formulaMap.put(formula.getFormulaName()+":"+formula.getYear(), formula.getFormulaContent());
				System.out.println(formula.getFormulaName()+":"+formula.getYear());
			}
		}
		if (page.getPageNo() < page.getTotalPage()) {
			pageGet(++pageNo, formulaMap, indexFormulaService, reportTypeId);
		}

	}

	/**
	 * 公式切分
	 * @param formulaContent
	 * @return
	 */
	private static String[] split(String formulaContent){
		List<String > res=new ArrayList<>();
		//String[] strs = formulaContent.split(" , | ( | ) |\\ + |\\ * | / | - ");
		String[] strs = formulaContent.split("( , )|( \\+ )|( - )|( / )|( \\* )|( \\( )|( \\) )");
		for(String str :strs){
			if(",".equals(str)||"(".equals(str)||")".equals(str)||"+".equals(str)||"-".equals(str)||"*".equals(str)||"/".equals(str)){
				continue;
			}
			res.add(str);
		}
		String[] d=new String[res.size()];
		return res.toArray(d);
	}
	public static void main(String[] args) {
		String formulaContent=" 次新_损*益类 数据_上期_其中: 营业收入 + xx(xx , ee ee -  ( aa / cc ) ";
			//将字段换算成数字
			String[] reportColumn = formulaContent.split("(\\s,\\s)|(\\s\\+\\s)|(\\s-\\s)|(\\s/\\s)|(\\s\\*\\s)|(\\s\\(\\s)|(\\s\\)\\s)");
			//String[] reportColumn = formulaContent.split(" , | ( | ) |\\ + |\\ * | / | - ");

			for(String str:reportColumn){
				System.out.println(str);
		}
		System.out.println(":::::"+"aa bb".split("\\s")[0]);
	}



}
