package com.ccx.shiro;


import com.alibaba.fastjson.JSON;
import com.ccx.system.model.PermissionBeanBg;
import com.ccx.system.model.RoleBg;
import com.ccx.system.model.UserBg;
import com.ccx.system.service.ResourceService;
import com.ccx.system.service.UserService;
import com.ccx.util.UsedUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by zhaotm on 2017/6/30.
 */
public class DbRealm extends AuthorizingRealm  {

    /**
     *
     * @ClassName: ControlPermissions
     * @Description: shiro(这里用一句话描述这个类的作用)
     * @author wuXiangNan
     * @date 2017年3月22日 下午5:14:32
     *
     */
    private static final Logger logger = LogManager.getLogger(DbRealm.class);

    @Autowired
    private UserService userService;
    @Autowired
    private ResourceService resourceService;

    /**
     * 权限认证
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取登录时输入的用户名
        String account=(String) principalCollection.fromRealm(getName()).iterator().next();
        //到数据库查是否有此对象
        UserBg user= userService.getUserByName(account);
        if(UsedUtil.isNotNull(user)){
            //权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
            SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
            //根据用户id获取角色实体
            RoleBg roleBg = userService.getRoleByUserId(user.getId());
            //根据用户id获取资源id
            List<Long> resIds = resourceService.getResByUserId(user.getId());
            //根据角色id获取资源实体
            Set<String> urlSet = new HashSet<>();
            for (Long resId : resIds) {
            	PermissionBeanBg res = resourceService.selectResourceById(resId);
                if(res != null){
                    String url = res.getPathUrl();
                    if(null!=url&&!"".equals(url.trim())){
                        urlSet.add(url);
                      //  urlSet.add(RolesOrAuthorizationFilter.getPermittedUrl(url));
                    }
                }
            }
            info.addRole(roleBg.getName());
            info.addStringPermissions(urlSet);
            logger.info("拥有权限："+JSON.toJSONString(info));
            return info;
        }
        return null;
    }

    /**
     * 登录认证;
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        if(UsedUtil.isNotNull(token.getUsername())){
            UserBg user= userService.getUserByName(token.getUsername());
            if(UsedUtil.isNotNull(user)){
                return new SimpleAuthenticationInfo(
                        user.getLoginName(),
                        user.getPassword(),
                        this.getName());
            }
        }
        return null;
    }

    /**
     * 缓存
     */
    public void clearAuthenticationInfo(){
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }
}
