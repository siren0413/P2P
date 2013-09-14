package com.rmi.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IHeartBeat extends Remote {
	/*
	 * A remote method to test whether a peer is alive by send out a 
	 * signal.   
	 */

	
	public boolean signal(byte[] MD5_array, String peer_service_port) throws RemoteException;
	public void report(List<String> fileList) throws RemoteException;
	
}
