/*
 * 
 */
package com.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// TODO: Auto-generated Javadoc
/**
 * The Class PropertyUtil.
 */
public class PropertyUtil {

	/** The prop. */
	private Properties prop;

	/**
	 * Instantiates a new property util.
	 * 
	 * @param file
	 *            the file
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public PropertyUtil(String file) throws FileNotFoundException, IOException {
		prop = new Properties();
		InputStream in = PropertyUtil.class.getResourceAsStream(file);
		prop.load(in);
	}

	/**
	 * Gets the property.
	 * 
	 * @param key
	 *            the key
	 * @return the property
	 */
	public String getProperty(String key) {
		return prop.getProperty(key);

	}
}
