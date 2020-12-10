package com.application.sys.controller;
import com.application.common.UserObject;
import com.application.common.utils.Return;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jbosframework.utils.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import java.util.List;
import java.util.Map;

/**
 * BaseController
 * @author youfu.wang
 * @date 2019-01-31
 */
public class BaseController {
	/**
	 * 得到Shiro用户对象
	 * @return
	 */
	protected UserObject getUserObject() {
		Subject subject=SecurityUtils.getSubject();
		if(null==subject){
			return null;
		}
		return (UserObject)subject.getPrincipal();
	}

	/**
	 * 得到Shiro会话对象
	 * @return
	 */
	protected Session getSession() {
		return SecurityUtils.getSubject().getSession();
	}

	/**
	 * 开始分页
	 * @param page
	 */
	public void doStargPage(Map<String, Object> page){
		int pageNum=Integer.parseInt(StringUtils.replaceNull(page.get("page")));
		int pageSize=Integer.parseInt(StringUtils.replaceNull(page.get("limit")));
		PageHelper.startPage(pageNum,pageSize);
	}

	/**
	 * 完成分页
	 * @param ret
	 * @param datas
	 */
	public void doFinishPage(Return ret, List datas){
		PageInfo pageInfo=new PageInfo(datas);
		ret.put("page",pageInfo);
	}
}
