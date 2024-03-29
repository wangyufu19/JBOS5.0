package com.application.sys.controller;
import com.application.common.UserObject;
import com.application.common.utils.Return;
import com.application.sys.pojo.UserInfo;
import com.application.sys.service.UserMgrService;
import com.jbosframework.beans.annotation.Autowired;
import com.jbosframework.web.mvc.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理控制器类
 * @author youfu.wang
 * @date 2019-03-01
 */
@RestController
@RequestMapping("/sys/user")
public class UserMgrController extends  BaseController{
    @Autowired
    private UserMgrService userMgrService;

    /**
     * 得到用户登录信息
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/info", method = RequestMethod.GET)
    //@ApiOperation("得到用户信息")
    public Return getUserInfo(){
        Return ret=Return.ok();
        UserObject userObject=this.getUserObject();
        ret.setData(userObject);
        return ret;
    }
    /**
     * 查询用户数据列表
     * @param params
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/getUserList", method = RequestMethod.GET)
    //@ApiOperation("查询用户数据列表")
    public Return getUserList(@RequestParam Map<String, Object> params){
        Return ret=Return.ok();
        try {
            this.doStartPage(params);
            List<UserInfo> userInfos = userMgrService.getUserList(params);
            this.doFinishPage(ret, userInfos);
        }catch (Exception e){
            ret=Return.error(e.getMessage());
            e.printStackTrace();
        }finally {
            this.clearPage();
        }
        return ret;
    }
}
