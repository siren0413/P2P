package com.db.ServerDB;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;



public class ServerHSQLDB {

	private static Logger LOGGER = Logger.getLogger(ServerHSQLDB.class);

	public static void initDB() {
			LOGGER.info("initializing ServerHSQLDB...");
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			Connection conn = getConnection();
			String tableName = "RegistryInfo";
			String sql = "CREATE TABLE "+tableName + " (" + "id         VARCHAR    NOT NULL primary key,"
					+ "ip         VARCHAR                  NOT NULL," + "port       VARCHAR                   NOT NULL,"
					+ "file_name  VARCHAR                  NOT NULL" + ")";
			
			if(!checkTableExists(conn, tableName)) {
				LOGGER.info("Table "+ tableName + " dose not exits.");
				Statement stat = conn.createStatement();
				stat.executeUpdate(sql);
				stat.close();
				LOGGER.info("Table " + tableName + " creates successfully.");
				
			}

			conn.close();
			LOGGER.info("initializing ServerHSQLDB Successfully!");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
	}
	public static Connection getConnection() throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:serverhsqldb/db", "SA", "");
		return conn;
	}
	
	public static boolean checkTableExists (Connection conn, String tableName) {
		boolean checkTable = false;
		
		try {
			DatabaseMetaData metaData = conn.getMetaData();
			ResultSet resultSet = metaData.getTables(null, null, tableName,null);
			while(resultSet.next()) {
				checkTable = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return checkTable;
	}

}
