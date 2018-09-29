package com.pms.app.config;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pms.web.util.PropertiesUtil;

public class ConnectionManager {

	/**
	 * Logger to log statements.
	 */
	final static Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);
	
	/**
	 * Holds DataSource object
	 */
	private static DataSource dataSource;

	private static DataSource tenantDataSource;
	/**
	 * Holds ConnectionManager object
	 */
	private static ConnectionManager connectionManagerObj;
	
	private static String dbInstance;
	

	public ConnectionManager() {
		BasicDataSource dataSource  = new BasicDataSource();
		dataSource.setDriverClassName(PropertiesUtil.getPropertyValue("common.db.driver.class"));
		dataSource.setUrl(PropertiesUtil.getPropertyValue("common.db.jdbc.url"));
		dataSource.setUsername(PropertiesUtil.getPropertyValue("common.db.username"));
		dataSource.setPassword(PropertiesUtil.getPropertyValue("common.db.password"));
		ConnectionManager.setTenantDataSource(dataSource);
	}
	
	private ConnectionManager(String userConfig) {
		BasicDataSource dataSource  = new BasicDataSource();
		dataSource.setDriverClassName(PropertiesUtil.getPropertyValue("user.db.driver.class"));
		dataSource.setUrl(PropertiesUtil.getPropertyValue("user.db.jdbc.url")+"/"+userConfig);
		dataSource.setUsername(PropertiesUtil.getPropertyValue("user.db.username"));
		dataSource.setPassword(PropertiesUtil.getPropertyValue("user.db.password"));
		ConnectionManager.setDataSource(dataSource);
	}
	
	public static DataSource getDataSource() {
		return dataSource;
	}

	public static void setDataSource(DataSource dataSource) {
		ConnectionManager.dataSource = dataSource;
	}

	/**
	 * Method to return the ConnectionManager instance.
	 * 
	 * @return ConnectionManager
	 */
	public static synchronized ConnectionManager getInstance(String userDB) {
		
		if(connectionManagerObj!=null && !StringUtils.isBlank(dbInstance) && getDbInstance().equalsIgnoreCase(userDB)){
			LOGGER.info("Connection already exisits for :"+ userDB);
			return connectionManagerObj;
		}
		else {
			LOGGER.info("Creating new connection to --"+ userDB);
			LOGGER.info("Connected to DB - "+ userDB);
			
			setDbInstance(userDB);
			connectionManagerObj = new ConnectionManager(userDB);
		}
		return connectionManagerObj;
		
	}

	/**
	 * Method to connect to DB.
	 * 
	 * @return Connection
	 */
	public Connection getConnection() {
		LOGGER.info("Inside  getConnection.");
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			LOGGER.info("Connection created to DB.");
		} catch (SQLException e) {
			LOGGER.info("SQLException while getting connection : " );
			LOGGER.error(e.getMessage());
		} catch (Exception e) {
			LOGGER.info("Exception while getting connection : " );
			LOGGER.error(e.getMessage());
		}
		LOGGER.info("Exit getConnection.");
		return connection;
	}
	
	public Connection getTenantConnection() {
		LOGGER.info("Inside  getConnection.");
		Connection connection = null;
		try {
			connection = tenantDataSource.getConnection();
			LOGGER.info("Connection created to DB.");
		} catch (SQLException e) {
			LOGGER.info("SQLException while getting connection : " );
			LOGGER.error(e.getMessage());
		} catch (Exception e) {
			LOGGER.info("Exception while getting connection : " );
			LOGGER.error(e.getMessage());
		}
		LOGGER.info("Exit getConnection.");
		return connection;
	}

	/**
	 * Method to close connection.
	 * 
	 * @param connection
	 */
	public static void closeConnection(Connection connection) {
		LOGGER.info("Closing connection.");
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.info("SQLException while closing connection : " );
				LOGGER.error(e.getMessage());
			}
		}
	}

	public static DataSource getTenantDataSource() {
		return tenantDataSource;
	}

	public static void setTenantDataSource(DataSource tenantDataSource) {
		ConnectionManager.tenantDataSource = tenantDataSource;
	}

	public static String getDbInstance() {
		return dbInstance;
	}

	public static void setDbInstance(String dbInstance) {
		ConnectionManager.dbInstance = dbInstance;
	}
}