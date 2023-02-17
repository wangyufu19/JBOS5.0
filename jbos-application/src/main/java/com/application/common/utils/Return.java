package com.application.common.utils;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
/**
 * ResponseData
 * @author youfu.wang
 * @date 2021-08-19
 */
@Setter
@Getter
public class Return {
	public static final String RETCODE_SUCCESS="0000";
	public static final String RETCODE_FAILURE="9999";
	public static final String RETMSG_SUCCESS="操作成功";
	public static final String RETMSG_FAILURE="操作失败";
	private String retCode;
	private String retMsg;
	private Object data=new HashMap<String,Object>();


	public Return(){
		this.retCode=RETCODE_SUCCESS;
		this.retMsg=RETMSG_SUCCESS;
	}
	public Return(String retCode,String rerMsg){
		this.retCode=retCode;
		this.retMsg=rerMsg;
	}
	public static Return ok() {
		return new Return();
	}
	public static Return ok(String retMsg) {
		return new Return(RETCODE_SUCCESS,retMsg);
	}
	public static Return ok(String retCode, String retMsg) {
		return new Return(retCode,retMsg);
	}
	public static Return error() {
		return new Return(RETCODE_FAILURE, RETMSG_FAILURE);
	}
	public static Return error(String retMsg) {
		return new Return(RETCODE_FAILURE,retMsg);
	}
	public static Return error(String retCode, String retMsg) {
		return new Return(retCode,retMsg);
	}
}
