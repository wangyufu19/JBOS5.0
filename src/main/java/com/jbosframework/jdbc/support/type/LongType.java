package com.jbosframework.jdbc.support.type;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * 长整型类型对象
 * @author youfu.wang
 * @version 1.0
 */
public class LongType {
	/**
	 * 从数据集中得到指定列的值
	 * @param rst
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public java.lang.Long get(ResultSet rst,String name) throws SQLException{
		if(rst.getObject(name)==null) return null;
		return rst.getLong(name);
	}

}
