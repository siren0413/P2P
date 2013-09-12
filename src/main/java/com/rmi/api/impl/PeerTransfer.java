package com.rmi.api.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.apache.log4j.Logger;

import com.dao.PeerDAO;
import com.rmi.api.IPeerTransfer;

@SuppressWarnings("serial")
public class PeerTransfer extends UnicastRemoteObject implements IPeerTransfer {

	private Logger LOGGER = Logger.getLogger(PeerTransfer.class); 
	private PeerDAO peerDAO = new PeerDAO();

	public PeerTransfer() throws RemoteException {
		super();
	}

	public byte[] obtain(String fileName, int start, int length) throws RemoteException{
		// get byte[] from other peers;
		try {
			String filePath = peerDAO.findFile(fileName);
			InputStream is = new FileInputStream(filePath);
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			byte[] buffer = new byte[length];
			int readSize;
			is.skip(start);
			if((readSize = is.read(buffer,0,length))!=-1) {
				byteArray.write(buffer,0,readSize);
			}
			is.close();
			return byteArray.toByteArray();
			
		} catch (FileNotFoundException e) {
			LOGGER.error("file: "+fileName+" not found",e);
			return null;
		} catch (IOException e) {
			LOGGER.error("unable to read file",e);
			return null;
		} 
				
	}

	public int getFileLength(String fileName) throws RemoteException {
		String filePath = peerDAO.findFile(fileName);
		File file = new File(filePath);
		return (int) file.length();
	}
	
	public String getFilePath(String fileName) {
		return peerDAO.findFile(fileName);
	}

	public boolean checkFileAvailable(String fileName) throws RemoteException {
		return peerDAO.checkFileAvailable(fileName);
	}


	
	
	

}
