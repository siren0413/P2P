package com.cache;

public class PeerInfo {
	/**
	 * The bean class for peer information
	 * 
	 */

	private String id;
	private String fileName;
	private String filePath;
	private int fileSize;
	
	
	/**
	 * getter for peer id
	 * @return String
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * setter for peer id
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * getter for file name
	 * @return String
	 */
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * setter for file name
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * getter for file path
	 * @return String 
	 */
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
