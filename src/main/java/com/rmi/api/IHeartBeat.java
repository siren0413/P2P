package com.rmi.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IHeartBeat extends Remote {

	
	public boolean signal(byte[] MD5_array) throws RemoteException;
	public void report(List<String> fileList) throws RemoteException;
	
}
