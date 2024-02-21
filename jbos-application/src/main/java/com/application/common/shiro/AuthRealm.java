package com.application.common.shiro;

import com.application.common.UserObject;
import com.application.sys.pojo.UserToken;
import com.application.sys.service.UserAuthService;
import com.application.sys.service.UserTokenService;
import com.jbosframework.beans.annotation.Autowired;
import com.jbosframework.beans.annotation.Bean;
import com.jbosframework.beans.annotation.Component;
import com.jbosframework.utils.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import java.util.Map;

/**
 * AuthRealm
 * @author youfu.wang
 * @date 2020-12-09
 */
@Component
public class AuthRealm extends AuthorizingRealm {
    @Autowired
    private UserTokenService userTokenService;
    @Autowired
    private UserAuthService userAuthService;

    public boolean supports(AuthenticationToken token) {
        return token instanceof AuthToken;
    }
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String accessToken = (String) authenticationToken.getPrincipal();
        //查询用户Token信息
        UserToken userToken=userTokenService.getUserTokenByAccessToken(accessToken);
        if(userToken==null|| userToken.getExpireTime().getTime() < System.currentTimeMillis()) {
            throw new IncorrectCredentialsException("token失效，请重新登录");
        }
        //查询用户认证信息
        UserObject userObject=new UserObject();
        Map<String,Object> userAuthInfo=userAuthService.getUserAuthInfoById(userToken.getUserId());
        if(null!=userAuthInfo){
            userObject.setUserId(userToken.getUserId());
            userObject.setUsername(StringUtils.replaceNull(userAuthInfo.get("USERNAME")));
            userObject.setLoginName(StringUtils.replaceNull(userAuthInfo.get("LOGINNAME")));
            userObject.setUserStatus(StringUtils.replaceNull(userAuthInfo.get("USERSTATUS")));
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userObject,accessToken,getName());

        return info;
    }
}
