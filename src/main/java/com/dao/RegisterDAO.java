package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Generated;

import com.db.ServerDB.ServerHSQLDB;
import com.util.ID_Generator;

public class RegisterDAO {

	PreparedStatement stmt;
	Connection conn;
	ResultSet result;
	
	
	public boolean addPeer(String ip, String port) {
		
		try {
		    conn = ServerHSQLDB.getConnection();
			String id = ID_Generator.generateID();
			String sql = "insert into PeerInfo values (?,?,?)";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, id);
			stmt.setString(2, ip);
			stmt.setString(3,port);
		
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
	
	public boolean deletePeer(String clientIp) {
		
		 if (!deletePeerFiles(clientIp))
			 return false;
		
		try {
		    conn = ServerHSQLDB.getConnection();
			String sql = "delete from PeerInfo where ip like '?'";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, clientIp);
		
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

	public boolean addFile(String clientIp,String fileName) {
		try {
			conn = ServerHSQLDB.getConnection();
			String peer_id = getPeerID(clientIp);
			
			if (peer_id==null)
				return false;
			String insertFile = "insert into FileInfo values (?,?)"; 
			stmt = conn.prepareStatement(insertFile);
			stmt.setString(1, peer_id);
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
	
	private String getPeerID(String clientIP) {
		try {
			conn = ServerHSQLDB.getConnection();
			String getHostId = "select id from PeerInfo where ip like '"+clientIP+"'";
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(getHostId);
		
			while(result.next()) {
				return result.getString(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	} 
	
	private boolean deletePeerFiles(String clientIP) {
		
		try {
			conn = ServerHSQLDB.getConnection();
			String peer_id = getPeerID(clientIP);
			
			if (peer_id==null)
				return false;
			String sql = "delete from FileInfo where peer_id like '?' "; 
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, peer_id);
					
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
