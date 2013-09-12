package com.rmi.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IHeartBeat extends Remote {

	
	public boolean signal(byte[] MD5_array, String peer_service_port) throws RemoteException;
	public void report(List<String> fileList) throws RemoteException;
	
}
