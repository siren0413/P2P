package com.rmi.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IHeartBeat extends Remote {

	public void report(List<String> fileList) throws RemoteException;
	
}