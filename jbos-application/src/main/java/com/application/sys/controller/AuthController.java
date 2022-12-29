package com.application.sys.controller;

import com.application.common.UserObject;
import com.application.common.shiro.ShiroUtils;
import com.application.common.utils.Return;
import com.application.sys.service.UserAuthService;
import com.application.sys.service.UserTokenService;
import com.jbosframework.beans.annotation.Autowired;
import com.jbosframework.utils.StringUtils;
import com.jbosframework.web.mvc.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;

/**
 * 用户认证类
 * @author youfu.wang
 * @date 2019-01-29
 */
@RestController
@RequestMapping("/sys/auth")
@Slf4j
//@Api("用户认证接口")
public class AuthController extends BaseController{
	@Autowired
	private UserAuthService userAuthService;
	@Autowired
	private UserTokenService userTokenService;
	/**
	 * 用户登录
	 * @return
	 */
    @ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	//@ApiOperation("用户登录")
	public Return login(@RequestBody Map<String, Object> params) {
		String username= StringUtils.replaceNull(params.get("username"));
		String password=StringUtils.replaceNull(params.get("password"));
		String accessToken="";
		try{
			//用户认证
			Map<String, Object> loginInfo=userAuthService.login(username);
			if(loginInfo==null){
				return Return.error("用户名称或密码不正确");
			}
			String userid=StringUtils.replaceNull(loginInfo.get("ID"));
			String authedPwd=StringUtils.replaceNull(loginInfo.get("PASSWORD"));
			String salt=StringUtils.replaceNull(loginInfo.get("SALT"));
			String userStatus=StringUtils.replaceNull(loginInfo.get("USER_STATUS"));
			String saltPwd=ShiroUtils.md5(password,salt);
			if(!authedPwd.equals(ShiroUtils.md5(password,salt))){
				return Return.error("用户名称或密码不正确");
			}
			if("02".equals(userStatus)){
				return Return.error("账号已被锁定,请联系管理员");
			}
			accessToken=userTokenService.createToken(userid);
		}catch (Exception e){
			log.error(e.getMessage(),e);
			return Return.error("用户认证失败");
		}
		return Return.ok().put("accessToken",accessToken);
	}
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	//@ApiOperation("用户登出")
	public Return logout() {
        UserObject u=this.getUserObject();
		try{
		    if(null!=u){
                userTokenService.invalidToken(u.getUserId());
            }
		}catch (Exception e){
			log.error(e.getMessage(),e);
			return Return.error("用户登出失败");
		}
		return Return.ok();
	}
}
