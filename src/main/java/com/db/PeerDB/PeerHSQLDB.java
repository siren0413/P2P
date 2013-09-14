package com.db.PeerDB;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class PeerHSQLDB {
	/*
	 * Database for Peer server/client
	 * There is one table in the database called 'PeerFiles'.
	 */

	private static Logger LOGGER = Logger.getLogger(PeerHSQLDB.class);

	public static void initDB() {
		LOGGER.info("initializing PeerHSQLDB...");
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			Connection conn = getConnection();
			String tableName = "PeerFiles";
			String sql = "CREATE TABLE "+tableName + " (" + "id         VARCHAR    NOT NULL primary key,"
					+ "file_path         VARCHAR                  NOT NULL," + "file_name       VARCHAR     NOT NULL,"
					+ "file_size	INT      NOT NULL, " 
					+ " constraint unique_file_and_file_path UNIQUE ( file_path,file_size) )";
			
			try {
				if(!checkTableExists(conn, tableName)) {
					LOGGER.info("Table "+ tableName + " dose not exits.");
					Statement stat = conn.createStatement();
					stat.executeUpdate(sql);
					stat.close();
					LOGGER.info("Table " + tableName + " creates successfully.");
				}
				
			} catch (SQLException e) {
				LOGGER.error("initialization exception:",e);
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
		Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:peerhsqldb/db", "SA", "");
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
