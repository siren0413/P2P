package com.rmi.api.impl;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.LookAndFeel;

import org.apache.log4j.Logger;

import com.client.ClientWindow;
import com.dao.RegisterDAO;
import com.rmi.api.IRegister;

@SuppressWarnings("serial")
public class Register extends UnicastRemoteObject implements IRegister {

	private ClientWindow window = ClientWindow.getInstance();
	
	public Register() throws RemoteException {
		super();
	}

	private Logger LOGGER = Logger.getLogger(Register.class);
	private RegisterDAO registerDAO = new RegisterDAO();
	
	
	public boolean registerPeer(String regPort) {
		String clienthost;
		try {
			// get client IP address
			clienthost = RemoteServer.getClientHost();
			InetAddress ia = java.net.InetAddress.getByName(clienthost);
			String clentIp = ia.getHostAddress();
			LOGGER.info("Received peer registry request. client IP[" + clentIp + "]");
			
			boolean result = registerDAO.addPeer(clentIp, regPort);
			if(!result)
				LOGGER.warn("Client registry failed!");
			else {
				LOGGER.info("Registered peer ip["+clentIp+"] service port["+regPort+"] successfully!");
				return true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.warn("Client registry failed!");
		}
		return false;
	}

	public boolean unRegister() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean registerFile(String fileName) throws RemoteException {
		String clienthost;
		try {
			// get client IP address
			clienthost = RemoteServer.getClientHost();
			InetAddress ia = java.net.InetAddress.getByName(clienthost);
			String clentIp = ia.getHostAddress();
			LOGGER.info("Received client add file request. client IP[" + clentIp + "]");
			
			boolean result = registerDAO.addFile(clentIp, fileName);
			if(!result)
				LOGGER.warn("Client add file failed !");
			else {
				LOGGER.info("Added file["+fileName+"] successfully!");
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			LOGGER.warn("Client add file failed !");
		}
		return false;
	}

}
