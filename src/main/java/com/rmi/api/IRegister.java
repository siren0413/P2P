package com.rmi.api;

import java.rmi.Remote;

public interface IRegister extends Remote {

	public boolean register(String regPort, String fileName);
	public boolean unRegister();
	
}
