/*
 * 
 */
package com.client;

// TODO: Auto-generated Javadoc
/**
 * The Class PeerInfo.
 */
public class PeerInfo {
	
	/** The bean class for peer information. */

	private String id;
	
	/** The file name. */
	private String fileName;
	
	/** The file path. */
	private String filePath;
	
	/** The file size. */
	private int fileSize;
	
	
	/**
	 * getter for peer id.
	 * 
	 * @return String
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * setter for peer id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * getter for file name.
	 * 
	 * @return String
	 */
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * setter for file name.
	 * 
	 * @param fileName
	 *            the new file name
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * getter for file path.
	 * 
	 * @return String
	 */
	public String getFilePath() {
		return filePath;
	}
	
	/**
	 * Sets the file path.
	 * 
	 * @param filePath
	 *            the new file path
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	/**
	 * Gets the file size.
	 * 
	 * @return the file size
	 */
	public int getFileSize() {
		return fileSize;
	}
	
	/**
	 * Sets the file size.
	 * 
	 * @param fileSize
	 *            the file size
	 * @return the int
	 */
	public int setFileSize(int fileSize) {
		return this.fileSize = fileSize;
	}

}
