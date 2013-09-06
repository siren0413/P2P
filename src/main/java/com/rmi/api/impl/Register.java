package com.rmi.api.impl;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;

import org.apache.log4j.Logger;

import com.frame.Window;
import com.rmi.api.IRegister;

@SuppressWarnings("serial")
public class Register extends UnicastRemoteObject implements IRegister {

	private Logger LOGGER = Logger.getLogger(Register.class);
	private Window window = Window.getInstance();
	
	protected Register() throws RemoteException {
		super();
	}

	public boolean register(String regPort) {
		String clienthost;
		try {
			// get client IP address
			clienthost = RemoteServer.getClientHost();
			InetAddress ia = java.net.InetAddress.getByName(clienthost);
			String clentIp = ia.getHostAddress();
			LOGGER.info("Received client registry request. client IP[" + clentIp + "]");
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean unRegister() {
		// TODO Auto-generated method stub
		return false;
	}

}
