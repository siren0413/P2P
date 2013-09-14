package com.client;

import java.rmi.Naming;
import java.util.List;

import org.junit.Test;

import com.rmi.api.IServerTransfer;

public class LoadTest {

	private String serverIP = "192.168.1.125";
	private String serverPort = "1099";
	
	
	public LoadTest() {
		
	}
	
	@Test
	public void responseTimeTest() {
		long start = System.currentTimeMillis();
		Thread thread;
		
		for(int i = 0; i < 1000; i++) {
			thread = new Thread(new Runnable() {
				public void run() {
					getFileList();
				}
			});
			thread.start();
		}
		
		long end = System.currentTimeMillis();
		long responseTime = end-start;
		System.out.println(responseTime);
	}
	
	
	private void getFileList() {
		try {
			IServerTransfer serverTransfer = (IServerTransfer) Naming.lookup("rmi://" + serverIP + ":" + serverPort
					+ "/serverTransfer");
			List<String> files = serverTransfer.listAllFile();
			for (String file : files) {
				System.out.println(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
