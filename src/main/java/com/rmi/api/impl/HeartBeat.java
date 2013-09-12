package com.rmi.api.impl;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

import com.dao.ServerDAO;
import com.rmi.api.IHeartBeat;

@SuppressWarnings("serial")
public class HeartBeat extends UnicastRemoteObject implements IHeartBeat {

	private final Logger LOGGER = Logger.getLogger(HeartBeat.class);
	private ServerDAO serverDAO = new ServerDAO();
	
	
	public HeartBeat() throws RemoteException{
		
	}

	public void report(List<String> fileList) {
		String clienthost;
		try {
			clienthost = RemoteServer.getClientHost();
			InetAddress ia = java.net.InetAddress.getByName(clienthost);
			String clentIp = ia.getHostAddress();
			LOGGER.info("sync peer["+clentIp+"] with server");
			List<String> serverFileList = serverDAO.listFiles(clentIp);
			HashSet<String> set = new HashSet<String>(serverFileList);
			for(String peerFile:fileList) {
				if(set.contains(peerFile)) {
					set.remove(peerFile);
				}else {
					serverDAO.addFile(clentIp, peerFile);
				}
			}
			
			if(set.size()>0) {
				for(String trashFile:set) {
					serverDAO.deleteFile(clentIp, trashFile);
				}
			}
			
			LOGGER.info("sync peer["+clentIp+"] successfully!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean signal(byte[] MD5_array, String peer_service_port) throws RemoteException {
		String clienthost;
		try {
			clienthost = RemoteServer.getClientHost();
			InetAddress ia = java.net.InetAddress.getByName(clienthost);
			String clentIp = ia.getHostAddress();
			MessageDigest md = MessageDigest.getInstance("MD5");
			List<String> fileList = serverDAO.listFiles(clentIp);
			String peer_ip = serverDAO.getPeerID(clentIp);
			if(peer_ip==null) {
				if(!serverDAO.addPeer(clentIp, peer_service_port)) {
					return false;
				}
			}
			Collections.sort(fileList);
			byte[] byteArray = fileList.toString().getBytes();
			byte[] md_byteArray = md.digest(byteArray);
			LOGGER.debug("server list:"+fileList.toString());
			LOGGER.debug("peer MD5:"+Arrays.toString(MD5_array)+" server MD5:"+Arrays.toString(md_byteArray));
			if(Arrays.equals(MD5_array, md_byteArray)) {
				LOGGER.info("MD5 verified!");
				return true;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		LOGGER.info("MD5 verification fail, need peer to report!");
		return false;
	}

}
