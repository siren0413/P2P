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

import com.rmi.api.IPeerTransfer;

@SuppressWarnings("serial")
public class PeerTransfer extends UnicastRemoteObject implements IPeerTransfer {

	private Logger LOGGER = Logger.getLogger(PeerTransfer.class); 

	protected PeerTransfer() throws RemoteException {
		super();
	}

	public byte[] obtain(String fileName) throws RemoteException{
		// get byte[] from other peers;
		try {
			InputStream is = new FileInputStream(fileName);
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			int length = (int)getFileLength(fileName);
			byte[] buffer = new byte[length];
			int readSize;
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

	public long getFileLength(String fileName) throws RemoteException {
		File file = new File(fileName);
		return file.length();
	}
	
	public String getFilePath(String fileName) {
		// get file path from database
		return null;
	}
	

}
