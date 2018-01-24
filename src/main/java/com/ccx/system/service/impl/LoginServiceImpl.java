package com.ccx.system.service.impl;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ccx.custom.dao.CustomManageMapper;
import com.ccx.custom.model.Institution;
import com.ccx.custom.model.Module;
import com.ccx.shiro.DbRealm;
import com.ccx.system.dao.UserMapper;
import com.ccx.system.model.PermissionBeanBg;
import com.ccx.system.model.UserBg;
import com.ccx.system.service.LoginService;
import com.ccx.util.MD5;
import com.ccx.util.RedisUtil;
import com.ccx.util.UsedUtil;


@Service
public class LoginServiceImpl implements LoginService {

	Logger log = LogManager.getLogger(LoginServiceImpl.class);
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private DbRealm dbRealm;
	
	@Autowired
	private UserMapper userMap;
	
	@Autowired
	private CustomManageMapper customManageMapper;
	
	@Autowired
	private RedisUtil redis;
	
	private final static String KEY_CCX_RISKCRM_WARNMSG = "ABS:ccx_credit_risk:warnMsg:insName";

	@Override
	public Map<String, Object> checkLogin(Map<String, Object> paramMap,HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//获取登录名和密码
		String account = (String) paramMap.get("account");
		String password = (String)paramMap.get("password");
		String verifyCode = (String)paramMap.get("verifyCode");
		String msg = "请求异常";
		if(UsedUtil.isNotNull(account) && UsedUtil.isNotNull(password)){
			UserBg userBeann = userMapper.getUserByName(account);
			UserBg newUserBean = new UserBg();
			//得到主体
			Subject subject=SecurityUtils.getSubject();
			try {
				dbRealm.clearAuthenticationInfo();
				password = MD5.encryption(password);
				//获取用户凭证信息
				UsernamePasswordToken token = new UsernamePasswordToken(account, password);
				//"已记住"和"已认证"是有区别的,并非是认证通过的用户
//				token.setRememberMe(true);
				try {
					//先进行图片验证码验证======图片验证码取消
					String verCode = (String) request.getSession().getAttribute("verCode");//系统生成的验证码
					if(null == verifyCode || "" == verifyCode){
						resultMap.put("msg", "请输入图片验证码!");
						return resultMap;
					}
					verifyCode = verifyCode.toLowerCase();
					if(verifyCode != verCode && !verifyCode.equals(verCode) ){
						resultMap.put("msg", "图片验证码输入有误!");
						return resultMap;
					}
					
					if(userBeann.getStatus() == 1){
						msg = "用户被冻结!";
						resultMap.put("msg", msg);
						return resultMap;
					}
					if(userBeann.getStatus() == 2){
						msg = "用户被锁定,请联系管理员!";
						resultMap.put("msg", msg);
						return resultMap;
					}
					
					//进行登录认证
					subject.login(token);
					//验证是否登录成功
					if(subject.isAuthenticated()){
						
						//根据角色查访问路径,用户菜单权限、用户功能权限
						//String url = xxxxxxxxxxxxxxxx;
						String url = "index";
						
						newUserBean.setId(userBeann.getId());
						newUserBean.setLoginNum(0);
						newUserBean.setLoginTime(new Date());
						userMap.updateUser(newUserBean);
						//查询当前用户所拥有的权限信息---type=1，只查询二级菜单
						List<PermissionBeanBg> permissionList = userMapper.findUserMenuPermission(userBeann.getId());
						
						//执行消息提醒操作
						warnMessage(userBeann,request);
						resultMap.put("code", 200);
						resultMap.put("url", url);
						//将用户信息、用户菜单权限、用户功能权限存入session中
						request.getSession().setAttribute("risk_crm_user", userBeann);
						request.getSession().setAttribute("permissionList", permissionList);
						request.getSession().setAttribute("permissionList1", JSON.toJSON(permissionList));
					}else{
						token.clear();
					}
					
				} catch (UnknownAccountException e) {
					msg = "账号不存在！";
					throw new RuntimeException("账号不存在!", e);
				} catch (DisabledAccountException e) {
					msg = "账号未启用！";
					throw new RuntimeException("账号未启用或被锁定!", e);
				} catch (ExcessiveAttemptsException e) {
					//msg = "密码输入错误5次以上,账号被锁定！请联系管理员";
					//userMap.lockUser(userBean.getId());
					throw new RuntimeException("账号被锁定!", e);
				} catch (IncorrectCredentialsException e) {
					int loginNum = userBeann.getLoginNum();
					//密码输错超过五次将被锁定，即第六次会被锁定
					if(loginNum>=5){
						loginNum += 1;
						newUserBean.setId(userBeann.getId());
						newUserBean.setStatus(2);
						newUserBean.setLoginNum(loginNum);
						newUserBean.setLoginTime(new Date());
						userMap.updateUser(newUserBean);
						msg = "密码输入错误5次以上,账号被锁定!请联系管理员";
						throw new RuntimeException("密码错误!", e);
					}else if(loginNum<5){
						loginNum += 1;
						newUserBean.setId(userBeann.getId());
						newUserBean.setLoginNum(loginNum);
						newUserBean.setLoginTime(new Date());
						userMap.updateUser(newUserBean);
						msg = "密码错误,再错"+(6-loginNum)+"次将被锁定!";
						throw new RuntimeException("密码错误!", e);
					}
				}
			} catch (Exception e) {
				log.error("用户登录失败：", msg);
				resultMap.put("code", 400);
				resultMap.put("msg", msg);
			}
		}else{
			resultMap.put("code", 400);
			resultMap.put("msg", "用户名或密码不能空!");
		}
		return resultMap;
	}
	
	/**
	 * 
	 * @Title: UserServiceImpl   
	 * @Description: 根据模块获取左侧导航栏 
	 * @param: @param params
	 * @param: @return
	 * @throws
	 */
	@Override
	public List<PermissionBeanBg> getLeftNavigation(Map<String,Object> params){
		return userMap.getLeftNavigation(params);
	}
	
	
	/**
	 * 
	 * @描述：机构模块消息操作
	 * @创建时间：  2017年8月30日
	 * @作者：武向楠
	 * @参数： @param userbean
	 * @参数： @param request      
	 * @return void     
	 * @throws
	 */
	public void warnMessage(UserBg userbean,HttpServletRequest request) {
		//先清除redis的消息缓存，在根据登陆人是否需要消息提醒进行提示
		redis.delRedis(KEY_CCX_RISKCRM_WARNMSG);
		//当前登陆人是否需要提醒标识 isNeedWarnFlag
		request.getSession().setAttribute("isNeedWarnFlag", "noNeed");
		//获取当前登录时间，用于与模块到期时间对比
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String currentDate = df.format(new Date());
        //定义新的list集合，存储结果集
        List<String> resultList = new ArrayList<String>();
		try {
			//模块分配的url
			String permission = "/custom/toModuleAllocationPage";
			boolean permissionFlag = false;
			//判断当前登陆人是否拥有模块分配的权限
			List<PermissionBeanBg> permissionList = userMapper.findUserFunPermission(userbean.getId());
			if(null != permissionList && permissionList.size()>0){
				for (int i = 0; i < permissionList.size(); i++) {
					String userPermission = permissionList.get(i).getPathUrl();
					if(UsedUtil.isNotNull(userPermission)){
						//如果拥有的话跳出循环
						if(permission == userPermission || userPermission.equals(permission)){
							permissionFlag = true;
							break;
						}
					}
				}
			}
			if(permissionFlag){
				boolean insWarnFlag = false;
				List<Institution> institutionList = customManageMapper.findAllExportIns();
				if(null != institutionList && institutionList.size()>0){
					for (int i = 0; i < institutionList.size(); i++) {
						long insId = institutionList.get(i).getId();
						String insName = institutionList.get(i).getName();
						List<Module> moduleList = customManageMapper.getMyModuleByInsId(insId);
						//设置标识，如果改机构只要有一个模块即将到期，就提示，不具体到模块信息
						boolean moduleFlag = false;
						if(null != moduleList && moduleList.size()>0){
							for (int j = 0; j < moduleList.size(); j++) {
								String endDate = moduleList.get(j).getEndDate();
				        		Date fDate = df.parse(currentDate);
				        		Date oDate = df.parse(endDate);
				        		//计算当前时间与模块到期时间差
				        		int days = daysOfTwo(oDate, fDate);
				        		//距离到期时间15天内进行提醒操作
				        		if(days>=0 && days <=15){
				        			//只要有一个模块即将到期，则进行提示；无需再次循环
				        			moduleFlag = true;
				        			insWarnFlag = true;
				        			break;
				        		}
							}
						}
						//如果moduleFlag==true，则标识该机构需要提醒
						if(moduleFlag){
							resultList.add(insName);
						}
					}
				}
				//只要有一个机构的一个模块需要提醒时，则开始判断当前登陆人是否需要提醒
		        if(insWarnFlag){
		        	System.err.println("后台消息提醒===============以下机构有模块即将到期============"+resultList);
					//查询是否当天已经提醒过
					int warnFlag = userbean.getWarnFlag();
					Date warnTime1 = userbean.getWarnTime();
					String warnTime = "";
					if(null != warnTime1){
						warnTime = df.format(userbean.getWarnTime());
					}
					//如果上次提醒时间为空且标识为0，说明此次为第一次提醒
					if(!UsedUtil.isNotNull(warnTime) && 0==warnFlag){
						//将resultList放入redis缓存
						redis.set(KEY_CCX_RISKCRM_WARNMSG,resultList);
						//当前登陆人是否需要提醒标识 isNeedWarnFlag
						request.getSession().setAttribute("isNeedWarnFlag", "need");
						//更新userbean warnFlag=1 warnTime=当前时间
						UserBg user = new UserBg();
						user.setId(userbean.getId());
						user.setWarnFlag(1);
						user.setWarnTime(new Date());
						userMap.updateUser(user) ;
					}else{
						//如果上次提醒标识为1，那么判断提醒时间是否时当天，如果提醒时间不为当天（只能是当天之前），则说明当天未提示
						if(warnTime != currentDate && !warnTime.equals(currentDate) && 1 == warnFlag){
							//将resultList放入redis缓存
							redis.set(KEY_CCX_RISKCRM_WARNMSG,resultList);
							//当前登陆人是否需要提醒标识 isNeedWarnFlag
							request.getSession().setAttribute("isNeedWarnFlag", "need");
							//更新userbean warnFlag=1 warnTime=当前时间
							UserBg user = new UserBg();
							user.setId(userbean.getId());
							user.setWarnFlag(1);
							user.setWarnTime(new Date());
							userMap.updateUser(user) ;
						}else{
							//置空
							redis.set(KEY_CCX_RISKCRM_WARNMSG,"");
							//当前登陆人是否需要提醒标识 isNeedWarnFlag
							request.getSession().setAttribute("isNeedWarnFlag", "noNeed");
						}
					}
		        }else{
					//置空
					redis.set(KEY_CCX_RISKCRM_WARNMSG,"");
					//当前登陆人是否需要提醒标识 isNeedWarnFlag
					request.getSession().setAttribute("isNeedWarnFlag", "noNeed");
				}
			}else{
				//置空
				redis.set(KEY_CCX_RISKCRM_WARNMSG,"");
				//当前登陆人是否需要提醒标识 isNeedWarnFlag
				request.getSession().setAttribute("isNeedWarnFlag", "noNeed");
			}
		} catch (Exception e) {
			//置空
			redis.set(KEY_CCX_RISKCRM_WARNMSG,"");
			//当前登陆人是否需要提醒标识 isNeedWarnFlag
			request.getSession().setAttribute("isNeedWarnFlag", "noNeed");
			e.printStackTrace();
		}
//		System.err.println("redis缓存的消息信息==="+(ArrayList<String>)(redis.get(KEY_CCX_CREDITRISK_WEB_REG_SMS)));
    }
	
	/**
	 * 
	 * @描述：获取消息提醒信息
	 * @创建时间：  2017年8月31日
	 * @作者：武向楠
	 * @参数： @return      
	 * @return List<String>     
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getWarnMsg(){
		return (List<String>)(redis.get(KEY_CCX_RISKCRM_WARNMSG));
	}
	
	/**
	 * 
	 * @描述：计算日期相差天数
	 * @创建时间：  2017年8月30日
	 * @作者：武向楠
	 * @参数： @param fDate
	 * @参数： @param oDate
	 * @参数： @return      
	 * @return int     
	 * @throws
	 */
	public static int daysOfTwo(Date fDate, Date oDate) {
		if (fDate == null || oDate == null){
			return 0;
		}
		long baseNum = 3600 * 1000 * 24;
		long chashu = fDate.getTime() - oDate.getTime();
		if (chashu<0){
			return -1;
		}
		long absNum = Math.abs(chashu);
		if(absNum<0){
			return 0;
		}
//			long mod = absNum % baseNum;
		int num = (int) (absNum / baseNum);
//			if (mod > 0)
//				num++;
		return num;
    }

}
