package com.jbosframework.jdbc.datasource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据源驱动类
 * @author youfu.wang
 * @version 1.0
 */
public class DriverManagerDataSource extends JDBCDataSource {
	private String driverClass;
	private String url;
	private String username;
	private String password;


	public DriverManagerDataSource() {
		
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public Connection getConnection() throws SQLException {
		Connection connection=null;
		try {
			Class.forName(this.getDriverClass());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			connection = DriverManager.getConnection(this.getUrl(), this.getUsername(),this.getPassword());
		} catch (SQLException e) {
			throw e;
		}
		return connection;
	}
}
