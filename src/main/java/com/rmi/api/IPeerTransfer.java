package com.rmi.api;

import java.rmi.Remote;

public interface IPeerTransfer extends Remote{

	public byte[] obtain(String fileName);
	
	
	
}
