package com.jbosframework.jdbc.core;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * RowMapper
 * @author youfu.wang
 * @version 1.0
 * @param <T>
 */
public interface RowMapper<T> {

	public T mapRow(ResultSet rs, int rowNum) throws SQLException;
}
