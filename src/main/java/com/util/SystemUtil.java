package com.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemUtil {

	private static final Date date = new Date();
	
	public SystemUtil() {
	}
	
	public static String getSimpleTime() {
		date.setTime(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss ");
		return sdf.format(date);
	}

}
