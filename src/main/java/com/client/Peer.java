package com.client;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import org.apache.log4j.Logger;

import com.rmi.api.IRegister;
import com.rmi.api.IServerTransfer;

public class Peer {

	private final Logger LOGGER = Logger.getLogger(Peer.class);
	private ClientWindow window;
	private String serverIP;
	private String serverPort;
	private String peer_service_port;
	
	

	// constructor
	public Peer(ClientWindow window) {
		this.window = window;
	}

	public boolean shareFile(File file) {
		try {
			IRegister register = (IRegister) Naming.lookup("rmi://" + serverIP + ":" + serverPort + "/register");
			boolean result1 = register.registerFile(file.getName());
			// add the file to self database
			
			if(result1)
				return true;
		} catch (Exception e) {
			LOGGER.error("Unable to register file [" + file.getName() + "]");
			return false;
		}
		
		return false;
	}
	
	public boolean downloadFile(String fileName) {
		try {
			IServerTransfer serverTransfer = (IServerTransfer) Naming.lookup("rmi://" + serverIP + ":" + serverPort + "/serverTransfer");
			serverTransfer.loopupFile(fileName);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return false;
	}

	// getter and setter
	public String getServer_ip() {
		return serverIP;
	}

	public void setServer_ip(String server_ip) {
		this.serverIP = server_ip;
	}

	public String getServer_port() {
		return serverPort;
	}

	public void setServer_port(String server_port) {
		this.serverPort = server_port;
	}

	public String getPeer_service_port() {
		return peer_service_port;
	}

	public void setPeer_service_port(String peer_service_port) {
		this.peer_service_port = peer_service_port;
	}

}
