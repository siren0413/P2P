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
	 * To insert, delete and find file from Peer's database.
	 * Peer database has one table named 'PeerFiles' to store 
	 * the files. 
	 */
	
	Connection conn;
	PreparedStatement stmt;
	ResultSet result;
	Statement statement;
	
	// insert into 'PeerFiles' table with file path,file name and file size	
	public boolean insertFile(String filePath,String fileName,int fileSize) {
		
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
	
	// delete a specific file from PeerFiles table 
	public boolean deleteFile(String fileName) {
		try {
			conn = PeerHSQLDB.getConnection();
			String sql = "delete from PeerFiles where file_name like '?' ";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, fileName);
		
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
	
	// find the file path of a specific file from 'PeerFiles'
	public String findFile(String fileName) {
		
		try {
			conn = PeerHSQLDB.getConnection();
		    statement = conn.createStatement();
			String sql = "select file_path from PeerFiles where file_name like '"+fileName+ "'";
			result = statement.executeQuery(sql);
			while(result.next()) {
				return result.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
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
	
	// check whether a file is in the database
	public boolean checkFileAvailable(String fileName) {
		if(findFile(fileName)!=null)
			return true;
		return false;
	}
	
	// get all files in the database
	public List<String> selectAllFiles() {
		List<String> allFiles = new ArrayList<String>();
		try {
			conn = PeerHSQLDB.getConnection();
			statement = conn.createStatement();
			String sql = "select file_name from PeerFiles";
			result = statement.executeQuery(sql);
			while(result.next()) {
				allFiles.add(result.getString(1));
			}
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allFiles;
	}
}
