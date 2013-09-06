package com.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {

	private Properties prop;

	public PropertyUtil(String file) throws FileNotFoundException, IOException {
		prop = new Properties();
		InputStream in = PropertyUtil.class.getResourceAsStream(file);
		prop.load(in);
	}

	public String getProperty(String key) {
		return prop.getProperty(key);

	}
}
