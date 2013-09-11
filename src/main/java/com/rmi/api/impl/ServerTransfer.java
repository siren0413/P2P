package com.rmi.api.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import com.rmi.api.IServerTransfer;

@SuppressWarnings("serial")
public class ServerTransfer extends UnicastRemoteObject implements IServerTransfer {

	public ServerTransfer() throws RemoteException{
		
	}

	public List<String> searchFile(String fileName) {
		// query db;
		// get all peers that contains that file;
		// return ip+port String to peer; <192.168.1.1:1099,192.168.1.2:1099...>
		
		
		return null;
	}

	
	

}
