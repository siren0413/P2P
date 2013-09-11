package com.rmi.api.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import com.rmi.api.IServerTransfer;

public class ServerTransfer extends UnicastRemoteObject implements IServerTransfer {

	public ServerTransfer() throws RemoteException{
		
	}

	public List<String> loopupFile(String fileName) {
		// query db;
		// get all peers that contains that file;
		// return ip+port String to peer;
		
		
		return null;
	}

	
	

}
