/*
 * 
 */
package com.util;

import java.text.SimpleDateFormat;
import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class SystemUtil.
 */
public class SystemUtil {

	/** The Constant date. */
	private static final Date date = new Date();
	
	/**
	 * Instantiates a new system util.
	 */
	public SystemUtil() {
	}
	
	/**
	 * Gets the simple time.
	 * 
	 * @return the simple time
	 */
	public static String getSimpleTime() {
		date.setTime(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss ");
		return sdf.format(date);
	}

}
