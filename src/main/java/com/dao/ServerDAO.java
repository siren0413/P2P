package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.db.ServerDB.ServerHSQLDB;

import java.util.ArrayList;
import java.util.List;

import com.util.ID_Generator;

public class ServerDAO {
	/*
	 * Implements activities to index server database, such as register a peer,
	 * delete a peer, add a peer's file etc.
	 */

	PreparedStatement stmt;
	Connection conn;
	ResultSet result;

	// register a peer
	public boolean addPeer(String ip, String port) {

		try {
			conn = ServerHSQLDB.getConnection();
			String id = ID_Generator.generateID();
			String sql = "insert into PeerInfo values (?,?,?)";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, id);
			stmt.setString(2, ip);
			stmt.setString(3, port);

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

	// delete a peer
	public boolean deletePeer(String clientIp) {

		if (!deletePeerFiles(clientIp))
			return false;
		Statement statement = null;
		
		try {
			conn = ServerHSQLDB.getConnection();
			statement = conn.createStatement();
			String sql = "delete from PeerInfo where ip like '"+clientIp+"'";
			statement.executeUpdate(sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
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

		return false;
	}

	// register a peer's file
	public boolean addFile(String clientIp, String fileName) {
		String peer_id = getPeerID(clientIp);
		if (peer_id == null)
			return false;
		try {
			conn = ServerHSQLDB.getConnection();
			String insertFile = "insert into FileInfo values (?,?)";
			stmt = conn.prepareStatement(insertFile);
			stmt.setString(1, peer_id);
			stmt.setString(2, fileName);

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

	// delete a peer's file
	public boolean deleteFile(String clientIp, String fileName) {
		String peerId = getPeerID(clientIp);
		Statement statement = null;
		if (peerId == null) {
			return false;
		}

		try {
			conn = ServerHSQLDB.getConnection();
			statement = conn.createStatement();
			String sql = "delete from FileInfo where peer_id like '" + peerId + "' and file_name like '" + fileName + "'  ";
			statement.executeUpdate(sql);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
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

		return false;
	}

	// return a peer's id
	public String getPeerID(String clientIP) {
		Statement stmt = null;
		try {
			conn = ServerHSQLDB.getConnection();
			String getHostId = "select id from PeerInfo where ip like '" + clientIP + "'";
			stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(getHostId);

			while (result.next()) {
				return result.getString(1);
			}

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
		return null;
	}

	// remove all the files belongs to a peer
	private boolean deletePeerFiles(String clientIP) {

		Statement statement = null;
		try {
			conn = ServerHSQLDB.getConnection();
			String peer_id = getPeerID(clientIP);
			
			if (peer_id == null)
				return false;
			statement = conn.createStatement();
			String sql = "delete from FileInfo where peer_id like '"+clientIP+"' ";
			statement.executeUpdate(sql);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
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

		return false;
	}

	// search the list of peer with a specific file
	public List<String> searchPeerwithFile(String fileName) {
		Statement stmt = null;
		List<String> peerList = new ArrayList<String>();
		try {
			conn = ServerHSQLDB.getConnection();
			String sql = "select ip, port from PeerInfo where id = (select peer_id from FileInfo where file_name like '"
					+ fileName + "' )";
			stmt = conn.createStatement();
			result = stmt.executeQuery(sql);
			while (result.next()) {
				String ip = result.getString(1);
				String port = result.getString(2);
				String address = ip + ":" + port;
				peerList.add(address);
			}

			if (peerList.size() != 0)
				return peerList;
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

		return null;
	}

	// return the list of files which a peer has registered on index server
	public List<String> listFiles(String peer_ip) {
		Statement stmt = null;
		List<String> fileList = new ArrayList<String>();
		try {
			conn = ServerHSQLDB.getConnection();
			String sql = "select file_name from FileInfo where peer_id = (select id from PeerInfo where ip like '" + peer_ip
					+ "' )";
			stmt = conn.createStatement();
			result = stmt.executeQuery(sql);
			while (result.next()) {
				String fileName = result.getString(1);
				fileList.add(fileName);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return fileList;
	}

	// get all available files from the index server
	public List<String> listAllFiles() {
		Statement stmt = null;
		List<String> fileList = new ArrayList<String>();
		try {
			conn = ServerHSQLDB.getConnection();
			String sql = "select file_name from FileInfo";
			stmt = conn.createStatement();
			result = stmt.executeQuery(sql);
			while (result.next()) {
				String fileName = result.getString(1);
				fileList.add(fileName);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return fileList;
	}

}
