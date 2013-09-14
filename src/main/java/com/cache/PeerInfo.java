package com.cache;

public class PeerInfo {

	private String id;
	private String fileName;
	private String filePath;
	private int fileSize;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public int getFileSize() {
		return fileSize;
	}
	public int setFileSize(int fileSize) {
		return this.fileSize = fileSize;
	}

}
