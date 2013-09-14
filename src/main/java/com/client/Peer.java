package com.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.cache.PeerInfo;
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
			LOGGER.debug("invoke remote object [" + "rmi://" + serverIP + ":" + serverPort + "/register]");
			IRegister register = (IRegister) Naming.lookup("rmi://" + serverIP + ":" + serverPort + "/register");
			boolean result1 = register.registerFile(file.getName());
			if (result1)
				LOGGER.info("register file[" + file.getName() + "] to index server successfully!");
			else
				return false;
			// add the file to self database
			boolean result2 = peerDAO.insertFile(file.getAbsolutePath(), file.getName(), 100);
			if (result2)
				LOGGER.info("insert file[" + file.getName() + "] to local database successfully!");
			else
				return false;

		} catch (SQLException e) {
			LOGGER.error("Unable to register file [" + file.getName() + "] due to DAO error", e);
			return false;
		} catch (RemoteException e) {
			LOGGER.error("Unable to register file [" + file.getName() + "] due to Remote error", e);
		} catch (MalformedURLException e) {
			LOGGER.error("Unable to register file [" + file.getName() + "] due to URL not correct", e);
		} catch (NotBoundException e) {
			LOGGER.error("Unable to register file [" + file.getName() + "] due to Not bound", e);
		}

		return true;
	}

	public boolean downloadFile(String fileName, String savePath) {
		try {
			LOGGER.debug("invoke remote object [" + "rmi://" + serverIP + ":" + serverPort + "/serverTransfer]");
			IServerTransfer serverTransfer = (IServerTransfer) Naming.lookup("rmi://" + serverIP + ":" + serverPort
					+ "/serverTransfer");
			List<String> peers = serverTransfer.searchFile(fileName);

			if (peers == null) {
				JOptionPane.showMessageDialog(window.getFrame(), "No source available for download!", "ERROR",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
			boolean result = false;

			for (String peer : peers) {
				LOGGER.debug("invoke remote object [" + "rmi://" + peer + "/peerTransfer]");
				IPeerTransfer peerTransfer;
				try {
					peerTransfer = (IPeerTransfer) Naming.lookup("rmi://" + peer + "/peerTransfer");
				} catch (Exception e2) {
					e2.printStackTrace();
					continue;
				}
				LOGGER.info("start downloading file from:" + "rmi://" + peer + "/peerTransfer");
				try {
					if (!peerTransfer.checkFileAvailable(fileName)) {
						continue;
					}
				} catch (RemoteException e2) {
					e2.printStackTrace();
					continue;
				}

				int length = 0;
				try {
					length = peerTransfer.getFileLength(fileName);
				} catch (RemoteException e2) {
					e2.printStackTrace();
					continue;
				}
				int start = 0;
				int left = length;

				LOGGER.info("file size:" + length + " bytes");

				File file = new File(savePath);
				OutputStream out;
				try {
					out = new FileOutputStream(file);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
					continue;
				}
				byte[] buffer;

				window.getProgressBar().setMaximum(length);
				window.getProgressBar().setVisible(true);
				window.getProgressBar().setStringPainted(true);

				LOGGER.info("download speed:" + Integer.valueOf(window.getTextField_DownloadLimit().getText()) * 1024 + " KB/S");

				window.getTextArea().append(SystemUtil.getSimpleTime() + "Start downloading...\n");
				while (left > 0) {
					try {
						Thread.sleep(1000);

						buffer = peerTransfer.obtain(fileName, start,
								1024 * Integer.valueOf(window.getTextField_DownloadLimit().getText()));

						out.write(buffer);
						left -= buffer.length;
						start += buffer.length;
						window.getProgressBar().setValue(start);
						window.getProgressBar().setIndeterminate(false);
						window.getProgressBar().repaint();
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				result = true;
			}
			if(result) {
				LOGGER.info("download file successfully!");
				window.getTextArea().append(SystemUtil.getSimpleTime() + "Download complete!\n");
			}else {
				LOGGER.info("fail to download.");
				window.getTextArea().append(SystemUtil.getSimpleTime() + "Download abort!\n");
			}
		} catch (Exception e) {
			LOGGER.error("fail to download file [" + fileName + "] ", e);
			JOptionPane.showMessageDialog(window.getFrame(), e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}

		window.getProgressBar().setVisible(false);
		return true;
	}

	public boolean sendSignal() {
		try {
			LOGGER.info("sending heartbeat signal to index server");
			LOGGER.debug("invoke remote object [" + "rmi://" + serverIP + ":" + serverPort + "/heartBeat]");
			IHeartBeat heartBeat = (IHeartBeat) Naming.lookup("rmi://" + serverIP + ":" + serverPort + "/heartBeat");
			List<String> listFiles = peerDAO.selectAllFiles();
			Collections.sort(listFiles);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (!heartBeat.signal(md.digest(listFiles.toString().getBytes()), peer_service_port)) {
				LOGGER.info("peer data is not consistent with server data, request sync.");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		LOGGER.info("peer data is consistent with server data.");
		return true;
	}

	public boolean sendReport() {
		try {
			LOGGER.info("sending heartbeat report to sync with index server");
			LOGGER.debug("invoke remote object [" + "rmi://" + serverIP + ":" + serverPort + "/heartBeat]");
			IHeartBeat heartBeat = (IHeartBeat) Naming.lookup("rmi://" + serverIP + ":" + serverPort + "/heartBeat");
			List<String> listFiles = peerDAO.selectAllFiles();
			heartBeat.report(listFiles);
			LOGGER.info("sent report successfully.");
		} catch (Exception e) {
			LOGGER.error("fail to send report", e);
		}

		return false;
	}

	public void listServerFile() {
		try {
			LOGGER.debug("invoke remote object [" + "rmi://" + serverIP + ":" + serverPort + "/serverTransfer]");
			IServerTransfer serverTransfer = (IServerTransfer) Naming.lookup("rmi://" + serverIP + ":" + serverPort
					+ "/serverTransfer");
			List<String> files = serverTransfer.listAllFile();
			LOGGER.debug("got file list from index server.");
			window.getTextArea().append(
					SystemUtil.getSimpleTime() + "****************** Available File List *******************\n");
			for (String file : files) {
				window.getTextArea().append(SystemUtil.getSimpleTime() + file + "\n");
			}
			window.getTextArea().append(
					SystemUtil.getSimpleTime() + "**********************************************************\n");

		} catch (Exception e) {
			LOGGER.error("fail to receive file list from server", e);
		}

	}

	public void updateLocalDatabase() {
		LOGGER.info("update local database");
		try {
			List<PeerInfo> list = peerDAO.queryAllfromPeerInfo();
			for (PeerInfo info : list) {
				File file = new File(info.getFilePath());
				if (!file.exists()) {
					LOGGER.info("file not found [" + file.getAbsolutePath() + "]");
					peerDAO.deleteFile(info.getFileName());
					LOGGER.info("delete file [" + file.getName() + "] from database.");
				}
			}

		} catch (SQLException e) {
			LOGGER.error("DAO error", e);
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
