package com.rmi.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IServerTransfer  extends Remote{
	/*
	 * Remote method to search available peer of  a given file.
	 */

	public List<String> searchFile(String fileName) throws RemoteException;
	
	public List<String> listAllFile() throws RemoteException;
	
}
