package com.jbosframework.jdbc.support.type;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 字符串类型对象
 * @author youfu.wang
 * @version 1.0
 */
public class StringType {
	/**
	 * 从数据集中得到指定列的值
	 * @param rst
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public Object get(ResultSet rst,String name) throws SQLException{
		return rst.getString(name);
	}

}
