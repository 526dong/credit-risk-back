package com.ccx.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ccx.dictionary.model.EnterpriseNature;
import com.ccx.dictionary.model.RateInstitution;
import com.ccx.dictionary.model.RateResult;
import com.ccx.dictionary.service.RateInstitutionService;
import com.ccx.dictionary.service.RateResultService;
import com.ccx.enterprise.model.Enterprise;
import com.ccx.enterprise.model.EnterpriseIndustry;
import com.ccx.enterprise.model.Region;
import com.ccx.enterprise.service.EnterpriseIndustryService;
import com.ccx.enterprise.service.EnterpriseService;
import com.ccx.enterprise.service.RegionService;
import com.ccx.index.model.AbsIndex;
import com.ccx.index.model.AbsModelElement;
import com.ccx.index.service.AbsIndexService;
import com.ccx.index.service.AbsModelElementService;


/**
 * 加载企业数据 
 * @author xzd
 * @date 2017/7/19
 */
public class EnterpriseDataUtils {

	/**
	 * 加载企业初始化数据：企业性质/一级行业/省 
	 * @param request
	 */
	public static void loadInitData(HttpServletRequest request, 
			EnterpriseService enterpriseService, EnterpriseIndustryService industryService, RegionService regionService){
		//加载企业性质
		List<EnterpriseNature> nature = new ArrayList<>(); 
		
		try {
			nature = enterpriseService.findAllNature();
			
			if (nature != null) {
				request.setAttribute("nature", nature);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//加载企业一级行业
		List<EnterpriseIndustry> industry1 = new ArrayList<>();
		try {
			industry1 = industryService.findAllIndustryByPid(0);
			
			if (industry1 != null) {
				request.setAttribute("industry1", industry1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//加载省
		List<Region> province = new ArrayList<>();
		try {
			province = regionService.findAllRegionByPid(0);
			
			if (province != null) {
				request.setAttribute("province", province);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过一级行业查询二级行业
	 * @param request
	 * @param industryService
	 * @return resultMap
	 */
	public static Map<String, Object> loadIndustry(HttpServletRequest request, EnterpriseIndustryService industryService){
		Map<String, Object> resultMap = new HashMap<>();
		List<EnterpriseIndustry> industry2 = new ArrayList<>();

		//一级行业id
		String pid = request.getParameter("pid");
		//判空处理
		boolean pidflag = (pid != null && !"".equals(pid));
		
		if (pidflag) {
			try {
				//通过一级行业id查询二级行业列表
				industry2 = industryService.findAllIndustryByPid(Integer.parseInt(pid));
			
				if (industry2 != null) {
					resultMap.put("industry2", industry2);
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		return resultMap;
	}
	
	/**
	 * 通过省查询市，通过市查询区域
	 * @param request
	 * @param regionService
	 * @return resultMap
	 */
	public static Map<String, Object> loadRegion(HttpServletRequest request, RegionService regionService){
		Map<String, Object> resultMap = new HashMap<>();
		List<Region> regionCityOrCounty = new ArrayList<>();

		//省份id/城市id
		String pid = request.getParameter("pid");
		//判空处理
		boolean pidflag = (pid != null && !"".equals(pid));
		
		if (pidflag) {
			try {
				//查询
				regionCityOrCounty = regionService.findAllRegionByPid(Integer.parseInt(pid));
				
				if (regionCityOrCounty != null) {
					resultMap.put("regionCityOrCounty", regionCityOrCounty);
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		return resultMap;
	}
	
	/**
	 * 企业指标查询
	 * @param indexApi
	 * @param elementApi
	 * @param params
	 * @return indexList
	 */
	public static List<AbsIndex> loadIndex(EnterpriseIndustryService industryService, 
			AbsIndexService indexService, AbsModelElementService elementService,
			Map<String, Integer> params){
		//二级行业id
		int industry2Id = params.get("industry2Id");
		//企业类型
		int entType = params.get("entType");
		//因素列表
		List<Integer> elementIds = new ArrayList<>();
		//指标列表
		List<AbsIndex> indexList = new ArrayList<>();

		Integer modelId = industryService.getModelIdByIdAndEntType(industry2Id, entType);
		if (null != modelId) {
			//通过modelId查询因素idList
			List<AbsModelElement> elementList =  elementService.getListByModelId(modelId);
			if (elementList.size() > 0) {
				for (AbsModelElement element: elementList) {
					elementIds.add(element.getId());
				}
			}
			//通过因素查询指标
			indexList = indexService.findNatureIndexByElementIds(elementIds);
		}
		
		return indexList;
	}
	
	/**
	 * 加载企业数据:指标规则
	 * @param enterprise request
	 */
	public static void loadIndexData(HttpServletRequest request, Enterprise enterprise, EnterpriseService enterpriseService){
		//指标规则
		Map<String, String> indexMap = new HashMap<>();
		
		try {
			indexMap = enterpriseService.selectEnterpriseIndexAndRules(enterprise.getId());
			
			if (indexMap != null) {
				if (StringUtils.isNotBlank(indexMap.get("indexIds")) && StringUtils.isNotBlank(indexMap.get("ruleIds"))) {
					String indexIds = indexMap.get("indexIds");
					String ruleIds = indexMap.get("ruleIds");
					
					request.setAttribute("indexIds", indexIds);
					request.setAttribute("ruleIds", ruleIds);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从导出目录中找对应的文件路径
	 * @param path 目录路径
	 * @return 文件路径
	 * @throws Exception
	 */
	public static String getFilePath(String path, String exportFileName) throws Exception {
		//需要的文件路径
		String filePath = "";

		//读取目录文件路径
		File file = new File(path);
		if(file.isDirectory()){
			File []files = file.listFiles();
			for(File fileIndex:files){
				String fileIndexName = fileIndex.getName();

				if (!StringUtils.isEmpty(fileIndexName)) {
					String fileName = fileIndexName.substring(0, fileIndexName.indexOf("."));

					if (exportFileName.equals(fileName)) {
						filePath = path + fileIndexName;
						break;
					}
				}
			}
		}

		return filePath;
	}

	/**
	 * 获取字典列表
	 * @param request
	 * @param isHaveIns 是否有查询评级机构
	 */
	public static void getDictionary(HttpServletRequest request, boolean isHaveIns, RateInstitutionService rateInstitutionService, RateResultService rateResultService){
		//评级机构字典列表
		if (isHaveIns) {
			try {
				List<RateInstitution> institutionList = rateInstitutionService.getList();
				request.setAttribute("institutionList", institutionList);
			} catch (Exception e) {
				e.printStackTrace();
				//logger.error("评级机构字典列表查询失败", e);
			}
		}

		//评级结果字典列表
		try {
			List<RateResult> resultList = rateResultService.getList();
			request.setAttribute("resultList", resultList);
		} catch (Exception e) {
			e.printStackTrace();
			//logger.error("评级结果字典列表查询失败", e);
		}
	}

	/**
	 * 获取定价数据反射map
	 * @return map
	 */
	public static Map<Integer, String> getPriceDataMap() {
		//priceDataMap put value
		Map<Integer, String> priceDataMap = new HashMap<>(8);

		//保证金
		priceDataMap.put(0, "CashDepositMin");
		priceDataMap.put(1, "CashDepositMax");
		//手续费
		priceDataMap.put(2, "HandlingChargeMin");
		priceDataMap.put(3, "HandlingChargeMax");
		//利率
		priceDataMap.put(4, "InterestRateMin");
		priceDataMap.put(5, "InterestRateMax");
		//IRR
		priceDataMap.put(6, "IrrMin");
		priceDataMap.put(7, "IrrMax");

		return priceDataMap;
	}
}
