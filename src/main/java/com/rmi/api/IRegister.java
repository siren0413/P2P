package com.rmi.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRegister extends Remote {

	public boolean registerPeer(String regPort) throws RemoteException;
	public boolean unRegisterPeer() throws RemoteException;
	public boolean registerFile(String fileName) throws RemoteException;
	 
}
