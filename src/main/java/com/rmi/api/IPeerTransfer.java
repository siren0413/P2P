package com.rmi.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPeerTransfer extends Remote{

	public byte[] obtain(String fileName) throws RemoteException;
	
	public long getFileLength(String fileName) throws RemoteException;
	
	
}
