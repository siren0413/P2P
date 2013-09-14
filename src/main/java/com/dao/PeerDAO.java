package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.db.PeerDB.PeerHSQLDB;
import com.util.ID_Generator;

public class PeerDAO {
	/*
	 * To insert, delete and find file of Peer's database.
	 * Peer database has one table named 'PeerFiles'
	 */
	
	Connection conn;
	PreparedStatement stmt;
	ResultSet result;
	Statement statement;
	
		
	public boolean insertFile(String filePath,String fileName,int fileSize) throws SQLException {
		
		try {
			conn = PeerHSQLDB.getConnection();
			String id = ID_Generator.generateID();
			String sql = "insert into PeerFiles values (?,?,?,?)";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, id);
			stmt.setString(2, filePath);
			stmt.setString(3, fileName);
			stmt.setInt(4, fileSize);
			stmt.executeUpdate();
			
			return true;
		} 
		finally {
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
	}
	
	public boolean deleteFile(String fileName) throws SQLException {
		try {
			conn = PeerHSQLDB.getConnection();
			String sql = "delete from PeerFiles where file_name like '?' ";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, fileName);
		
			stmt.executeUpdate();
			
			return true;
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
	}
	
	
	public String findFile(String fileName) throws SQLException {
		
		try {
			conn = PeerHSQLDB.getConnection();
		    statement = conn.createStatement();
			String sql = "select file_path from PeerFiles where file_name like '"+fileName+ "'";
			result = statement.executeQuery(sql);
			while(result.next()) {
				return result.getString(1);
			}
		} finally {
			try {
				statement.close();
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
		return null;
	}
	
	public boolean checkFileAvailable(String fileName) throws SQLException {
		if(findFile(fileName)!=null)
			return true;
		return false;
	}
	
	public List<String> selectAllFiles() throws SQLException {
		List<String> allFiles = new ArrayList<String>();
		try {
			conn = PeerHSQLDB.getConnection();
			statement = conn.createStatement();
			String sql = "select file_name from PeerFiles";
			result = statement.executeQuery(sql);
			while(result.next()) {
				allFiles.add(result.getString(1));
			}
		} finally {
			try {
				statement.close();
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
		return allFiles;
	}
}
