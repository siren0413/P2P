package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Generated;

import com.db.HSQLDB;
import com.util.ID_Generator;

public class RegisterDAO {

	PreparedStatement stmt;
	Connection conn;
	ResultSet result;
	
	
	public boolean addClient(String ip, String port, String fileName) {
		
		try {
		    conn = HSQLDB.getConnection();
			String id = ID_Generator.generateID();
			String sql = "insert into RegistryInfo values (?,?,?,?)";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, id);
			stmt.setString(2, ip);
			stmt.setString(3,port);
			stmt.setString(4, fileName);
			stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return false;
	}

}
