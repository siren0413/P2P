package com.rmi.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Remote method to search available peer of  a given file.
 * 
 */
public interface IServerTransfer  extends Remote{

	/**
	 * search a file in the index server
	 * @param fileName
	 * @return	List<String>
	 * @throws RemoteException
	 */
	public List<String> searchFile(String fileName) throws RemoteException;
	
	
	/**
	 * list all files in the index server
	 * @return List<String>
	 * @throws RemoteException
	 */
	public List<String> listAllFile() throws RemoteException;
	
}
