package com.ccx.util.aspect;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ccx.util.ControllerUtil;
import com.ccx.util.UsedUtil;




/**
 * 
* @ClassName: MenuIndexAspect 
* @Description: 菜单脚标定位切面
* @author WXN 
* @date 2017年8月10日 下午1:59:59 
*
 */
@Aspect
@Component
public class MenuIndexAspect {

	private static final Logger logger = LoggerFactory.getLogger(MenuIndexAspect.class);
	@Pointcut("execution(* com.ccx.*.controller..*Controller.*(..))")
	//@Pointcut("execution(* com.ccx.antifraud.controller.*..*.*(..))")
	public void menuIndexAspect() {
	}

	@Before("menuIndexAspect()")
	public void doBefore(JoinPoint joinPoint)  {
		//获取request
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = servletRequestAttributes.getRequest();  
		//获取请求方式
		String method = request.getMethod();
		//获取请求参数
		Map<String,Object> map = ControllerUtil.request2Map(request);
		//获取菜单脚标
		String menuIndex1 = (String) map.get("menuIndex1");
		String menuIndex2 = (String) map.get("menuIndex2");
		if(method.toLowerCase().equals("get") && UsedUtil.isNotNull(menuIndex1) && UsedUtil.isNotNull(menuIndex2)){
			logger.info("菜单脚标1 - "+menuIndex1);
			logger.info("菜单脚标2 - "+menuIndex2);
			request.getSession().setAttribute("menuIndex1", menuIndex1);
			request.getSession().setAttribute("menuIndex2", menuIndex2);
		}
	}
	
	@Around(value="menuIndexAspect()")
	public Object doAround(ProceedingJoinPoint joinPoint)throws Throwable{
	    return joinPoint.proceed();
	}
	
	@After(value = "menuIndexAspect()")
	public void doAfter(JoinPoint joinPoint) {
	}
	
}
