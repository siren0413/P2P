package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.db.PeerDB.PeerHSQLDB;
import com.util.ID_Generator;

import org.apache.log4j.Logger;

public class PeerDAO {
	/*
	 * To insert, delete and find file of Peer's database.
	 * Peer database has one table named 'PeerFiles'
	 */
	
	Connection conn;
	PreparedStatement stmt;
	ResultSet result;
		
	public boolean insertFile(String filePath,String fileName,Long fileSize) {
		
		try {
			conn = PeerHSQLDB.getConnection();
			String id = ID_Generator.generateID();
			String sql = "insert into PeerFiles values (?,?,?,?)";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, id);
			stmt.setString(2, filePath);
			stmt.setString(3, fileName);
			stmt.setLong(4, fileSize);
		
			stmt.executeUpdate();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
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
	
	public boolean deleteFile(String filePath ,String fileName) {
		try {
			conn = PeerHSQLDB.getConnection();
			String sql = "delete from PeerFiles where file_path like '?' and file_name like '?' ";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, filePath);
			stmt.setString(2, fileName);
		
			stmt.executeUpdate();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
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
	
	
	public boolean findFile(String filePath,String fileName) {
		try {
			conn = PeerHSQLDB.getConnection();
			String sql = "select * from PeerFiles where file_path like '?' and file_name like '?' ";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, filePath);
			stmt.setString(2, fileName);
		
			stmt.executeUpdate();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
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
