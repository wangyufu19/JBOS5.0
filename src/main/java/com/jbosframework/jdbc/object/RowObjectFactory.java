package com.jbosframework.jdbc.object;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import com.jbosframework.beans.access.BeanFactory;
import com.jbosframework.beans.access.Setter;
import com.jbosframework.jdbc.annotation.Property;
/**
 * RowObjectFactory
 * @author youfu.wang
 * @version 1.0
 * @date 2016-11-18
 */
public class RowObjectFactory {
	/**
	 * 得到数据集对象
	 * @param rst
	 * @return
	 * @throws SQLException
	 */
	public static Object[] getRowObject(ResultSet rst) throws SQLException{
		Object[] obj=null;		
		ResultSetMetaData rsmd = rst.getMetaData();
		obj = new Object[rsmd.getColumnCount()];
		for (int i = 0; i < obj.length; i++) {
			obj[i] = rst.getObject(i + 1);
		}
		return obj;
	}
	/**
	 * 得到数据集对象
	 * @param <T>
	 * @param rst
	 * @param requiredType
	 * @return
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getRowObject(ResultSet rst,Class<T> requiredType){
		Object obj=null;
		if(requiredType==null){
			return null;
		}
		try {
			obj=requiredType.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		Field[] fields=obj.getClass().getDeclaredFields();			
		if(fields!=null){			
			Object value=null;
			for(int i=0;i<fields.length;i++){		
				Property proAnnotation=fields[i].getAnnotation(Property.class);
				if(proAnnotation==null)
					continue;
				try {
					value=rst.getObject(proAnnotation.column());
				} catch (SQLException e) {
					continue;
				}
				Setter setter=BeanFactory.getSetter(obj.getClass(), fields[i].getName());	
				setter.set(obj, value);
			}
		}
		return (T)obj;
	}
	
}
