package com.rmi.api.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.rmi.api.IHeartBeat;

public class HeartBeat extends UnicastRemoteObject implements IHeartBeat {

	public HeartBeat() throws RemoteException{
		
	}

	public void report(List<String> fileList) {
		// update database, both registry info and status
	}

}
