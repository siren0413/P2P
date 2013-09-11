package com.rmi.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPeerTransfer extends Remote{
	
	public boolean checkFileAvailable(String fileName) throws RemoteException;

	public byte[] obtain(String fileName, int start, int length) throws RemoteException;
	
	public int getFileLength(String fileName) throws RemoteException;
	
	
}
