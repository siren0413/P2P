package com.rmi.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRegister extends Remote {
	/*
	 * Remote method to register a peer, unregister a peer and register a peer's file
	 */

	public boolean registerPeer(String regPort) throws RemoteException;
	public boolean unRegisterPeer() throws RemoteException;
	public boolean registerFile(String fileName) throws RemoteException;
	 
}
