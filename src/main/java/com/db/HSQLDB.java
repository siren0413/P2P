package com.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;



public class HSQLDB {

	private static Logger LOGGER = Logger.getLogger(HSQLDB.class);

	public static void initDB() {
		LOGGER.info("initializing HSQLDB...");
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			Connection conn = getConnection();
			String sql = "CREATE TABLE RegistryInfo" + "(" + "id         VARCHAR    NOT NULL primary key,"
					+ "ip         VARCHAR                  NOT NULL," + "port       VARCHAR                   NOT NULL,"
					+ "file_name  VARCHAR                  NOT NULL" + ")";
			Statement stat = conn.createStatement();
			stat.executeUpdate(sql);
			stat.close();
			conn.close();
			LOGGER.info("initializing HSQLDB Successfully!");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
	}
	public static Connection getConnection() throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:hsqldb/db", "SA", "");
		return conn;
	}

}
