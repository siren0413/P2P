package com.rmi.api.impl;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.util.Arrays;
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
		// update database, both registry info and status
	}

	public boolean signal(byte[] MD5_array) throws RemoteException {
		String clienthost;
		try {
			clienthost = RemoteServer.getClientHost();
			InetAddress ia = java.net.InetAddress.getByName(clienthost);
			String clentIp = ia.getHostAddress();
			MessageDigest md = MessageDigest.getInstance("MD5");
			List<String> fileList = serverDAO.listFiles(clentIp);
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
