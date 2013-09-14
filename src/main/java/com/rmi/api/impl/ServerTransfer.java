package com.rmi.api.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import org.apache.log4j.Logger;

import com.dao.ServerDAO;
import com.rmi.api.IServerTransfer;

@SuppressWarnings("serial")
public class ServerTransfer extends UnicastRemoteObject implements IServerTransfer {
	/*
	 * Implementation of remote method to search available peer of given file.
	 */

	public ServerTransfer() throws RemoteException{
		
	}
	
	private Logger LOGGER = Logger.getLogger(ServerTransfer.class);
	private ServerDAO serverDAO = new ServerDAO();

	public List<String> searchFile(String fileName) {
		// query db;
		// get all peers that contains that file;
		// return ip+port String to peer; <192.168.1.1:1099,192.168.1.2:1099...>
		List<String> clientList;
		LOGGER.info("Search availabe Peers for file [" + fileName + "]");
		clientList = serverDAO.searchPeerwithFile(fileName);
		if (clientList == null)
			LOGGER.warn("No such file found in database! file: [" + fileName + "]");
		else
			LOGGER.info("Return Peer(s) list to client");
		return clientList;
	}

	public List<String> listAllFile() throws RemoteException {
		List<String> files = serverDAO.listAllFiles();
		return files;
	}

	
	
	

}
