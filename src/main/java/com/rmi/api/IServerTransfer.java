package com.rmi.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IServerTransfer  extends Remote{

	public List<String> loopupFile(String fineName) throws RemoteException;
}
