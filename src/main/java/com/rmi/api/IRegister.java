package com.rmi.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRegister extends Remote {

	public boolean register(String regPort, String fileName) throws RemoteException;
	public boolean unRegister() throws RemoteException;
	
}
