package com.ccx.businessmng.controller;
	
import org.springframework.beans.factory.annotation.Autowired;	
import org.springframework.stereotype.Controller;	
import org.springframework.web.bind.annotation.RequestMapping;	
import org.springframework.web.bind.annotation.RequestMethod;	
import org.springframework.web.bind.annotation.RequestParam;	
import org.springframework.web.bind.annotation.ResponseBody;	
import org.springframework.web.servlet.ModelAndView;	
import javax.servlet.http.HttpServletRequest;		
import com.ccx.businessmng.model.AbsEnterpriseReportType;
import com.ccx.businessmng.service.AbsEnterpriseReportTypeService;
import java.util.*;	
	
@Controller	
@RequestMapping("/absEnterpriseReportType")	
public class AbsEnterpriseReportTypeController {	
		
	@Autowired	
	private AbsEnterpriseReportTypeService absEnterpriseReportTypeService;	
		
	/*	
	 * @author 	
	 * @description	
	 * @date	
	*/	
	@RequestMapping(value="/absEnterpriseReportTypeindex", method=RequestMethod.GET)	
	public ModelAndView absEnterpriseReportTypeIndex(HttpServletRequest request) {	
		ModelAndView mnv = new ModelAndView("");	
		try {	
				
		} catch (Exception e){	
			e.printStackTrace();	
		}	
		return mnv;	
	}	
		
	/*	
	 * @author 	
	 * @description	
	 * @date	
	*/	
	@RequestMapping(value="/absEnterpriseReportTypeAddOrEdit", method=RequestMethod.GET)	
	public ModelAndView absEnterpriseReportTypeAddOrEdit(HttpServletRequest request) {	
		ModelAndView mnv = new ModelAndView("");	
		try {	
				
		} catch (Exception e){	
			e.printStackTrace();	
		}	
		return mnv;	
	}	
		
	/*	
	 * @author 	
	 * @description	
	 * @date	
	*/	
	@RequestMapping(value="/absEnterpriseReportTypeAddOrEditSave", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody	
	public Map<String, Object> absEnterpriseReportTypeAddOrEditSave(AbsEnterpriseReportType model) {	
		Map<String, Object> resultMap = new HashMap<>();	
			
		try {	
			absEnterpriseReportTypeService.save(model);	
			resultMap.put("code", 200);	
		} catch (Exception e) {	
			resultMap.put("code", 500);	
		}	
		return resultMap;	
	}	
		
		
}	
