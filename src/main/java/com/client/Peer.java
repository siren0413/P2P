package com.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.dao.PeerDAO;
import com.rmi.api.IHeartBeat;
import com.rmi.api.IPeerTransfer;
import com.rmi.api.IRegister;
import com.rmi.api.IServerTransfer;
import com.util.SystemUtil;

public class Peer {

	private final Logger LOGGER = Logger.getLogger(Peer.class);
	private ClientWindow window;
	private String serverIP;
	private String serverPort;
	private String peer_service_port;
	private PeerDAO peerDAO;
	
	// constructor
	public Peer(ClientWindow window) {
		this.window = window;
		peerDAO = new PeerDAO();
	}

	public boolean shareFile(File file) {
		try {
			IRegister register = (IRegister) Naming.lookup("rmi://" + serverIP + ":" + serverPort + "/register");
			boolean result1 = register.registerFile(file.getName());
			if(result1)
				LOGGER.info("register file["+file.getName()+"] to index server successfully!");
			else
				return false;
			// add the file to self database
			boolean result2 = peerDAO.insertFile(file.getAbsolutePath(), file.getName(), 100);
			if(result2)
				LOGGER.info("insert file["+file.getName()+"] to local database successfully!");
			else
				return false;

		} catch (Exception e) {
			LOGGER.error("Unable to register file [" + file.getName() + "]",e);
			return false;
		}

		return true;
	}

	public boolean downloadFile(String fileName, String savePath) {
		try {
			IServerTransfer serverTransfer = (IServerTransfer) Naming.lookup("rmi://" + serverIP + ":" + serverPort
					+ "/serverTransfer");
			List<String> peers = serverTransfer.searchFile(fileName);
			
			if(peers == null) {
				JOptionPane.showMessageDialog(window.getFrame(), "No source available for download!", "ERROR", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			for (String peer : peers) {
				IPeerTransfer peerTransfer = (IPeerTransfer) Naming.lookup("rmi://" + peer + "/peerTransfer");
				LOGGER.info("start downloading file from:" + "rmi://" + peer + "/peerTransfer");
				if(!peerTransfer.checkFileAvailable(fileName)) {
					continue;
				}
				
				int length = peerTransfer.getFileLength(fileName);
				int start = 0;
				int left = length;
				
				LOGGER.debug("file length ["+length+"]");
				
				File file = new File(savePath);
				OutputStream out = new FileOutputStream(file);
				byte[] buffer;
				
				window.getProgressBar().setMaximum(length);
				window.getProgressBar().setVisible(true);
				window.getProgressBar().setStringPainted(true);
				
				window.getTextArea().append(SystemUtil.getSimpleTime()+"Start downloading...\n");
				while(left>0) {
					Thread.sleep(1000);
					buffer = peerTransfer.obtain(fileName, start, 1024*Integer.valueOf(window.getTextField_DownloadLimit().getText()));
					out.write(buffer);
					left -= buffer.length;
					start += buffer.length;
					window.getProgressBar().setValue(start);
					window.getProgressBar().setIndeterminate(false);
					window.getProgressBar().repaint();
				}
				out.close();
				
			}
			LOGGER.info("download file successfully!");
			window.getTextArea().append(SystemUtil.getSimpleTime()+"Download complete!\n");
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(window.getFrame(), e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}

		window.getProgressBar().setVisible(false);
		return true;
	}
	
	
	public boolean sendSignal() {
		try {
			IHeartBeat heartBeat = (IHeartBeat) Naming.lookup("rmi://" + serverIP + ":" + serverPort + "/heartBeat");
			List<String> listFiles = peerDAO.selectAllFiles();
			LOGGER.debug("peer list:"+listFiles.toString());
			MessageDigest md = MessageDigest.getInstance("MD5");
			return heartBeat.signal(md.digest(listFiles.toString().getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean sendReport() {
		try {
		IHeartBeat heartBeat = (IHeartBeat) Naming.lookup("rmi://" + serverIP + ":" + serverPort + "/heartBeat");
		List<String> listFiles = peerDAO.selectAllFiles();
		heartBeat.report(listFiles);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public void listServerFile() {
		try {
			IServerTransfer serverTransfer = (IServerTransfer) Naming.lookup("rmi://" + serverIP + ":" + serverPort
					+ "/serverTransfer");
			List<String> files = serverTransfer.listAllFile();
			window.getTextArea().append(SystemUtil.getSimpleTime()+"****************** Available File List *******************\n");
			for(String file:files) {
				window.getTextArea().append(SystemUtil.getSimpleTime()+file+"\n");
			}
			window.getTextArea().append(SystemUtil.getSimpleTime()+"**********************************************************\n");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
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
