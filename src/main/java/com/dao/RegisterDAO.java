package com.dao;

import java.sql.Connection;
import java.sql.Statement;

import com.db.HSQLDB;

public class RegisterDAO {

	public boolean addClient(String ip, String port, String fileName) {
		
		try {
			Connection conn = HSQLDB.getConnection();
			Statement stmt = conn.createStatement();
			String sql;
			//stmt.executeUpdate(sql);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return false;
	}

}
