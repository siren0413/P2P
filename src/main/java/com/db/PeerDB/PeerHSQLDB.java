package com.db.PeerDB;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;



public class PeerHSQLDB {

	private static Logger LOGGER = Logger.getLogger(PeerHSQLDB.class);

	public static void initDB() {
		LOGGER.info("initializing PeerHSQLDB...");
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			Connection conn = getConnection();
			String tableName = "PeerInfo";
			String sql = "CREATE TABLE "+tableName + " (" + "id         VARCHAR    NOT NULL primary key,"
					+ "ip         VARCHAR                  NOT NULL," + "port       VARCHAR                   NOT NULL,"
					+ "file_name  VARCHAR                  NOT NULL" + ")";
			
			try {
				if(!checkTableExists(conn, tableName)) {
					Statement stat = conn.createStatement();
					stat.executeUpdate(sql);
					stat.close();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			conn.close();
			LOGGER.info("initializing PeerHSQLDB Successfully!");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
	}
	public static Connection getConnection() throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:hsqldb:file:peerhsqldb/db", "SA", "");
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
