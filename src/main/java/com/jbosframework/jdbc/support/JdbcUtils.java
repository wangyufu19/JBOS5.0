package com.jbosframework.jdbc.support;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * JdbcUtils
 * @author youfu.wang
 * @version 1.0
 */
public class JdbcUtils {
	private static final Log logger = LogFactory.getLog(JdbcUtils.class);
	/**
	 * 关闭PreparedStatement
	 * @param pstmt
	 */
	public static void closeStatement(PreparedStatement pstmt){
		if(pstmt!=null){
			try {
				pstmt.close();
			} catch (SQLException e) {
				logger.debug("Could not close JDBC PreparedStatement", e);
			}
		}
	}
	/**
	 * 关闭Statement
	 * @param stmt
	 */
	public static void closeStatement(Statement stmt){
		if(stmt!=null){
			try {
				stmt.close();
			} catch (SQLException e) {
				logger.debug("Could not close JDBC Statement", e);
			}
		}
	}
	/**
	 * 关闭CallableStatement
	 * @param cstmt
	 */
	public static void closeStatement(CallableStatement cstmt){
		if(cstmt!=null){
			try {
				cstmt.close();
			} catch (SQLException e) {
				logger.debug("Could not close JDBC CallableStatement", e);
			}
		}
	}
	/**
	 * 关闭ResultSet
	 * @param rst
	 */
	public static void closeResultSet(ResultSet rst){
		if(rst!=null){
			try {
				rst.close();
			} catch (SQLException e) {
				logger.debug("Could not close JDBC ResultSet", e);
			}
		}
	}
}
