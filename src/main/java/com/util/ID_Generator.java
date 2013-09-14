/*
 * 
 */
package com.util;

import java.util.UUID;

// TODO: Auto-generated Javadoc
/**
 * ID_Generator.
 */
public class ID_Generator {

	/**
	 * Generate unique id.
	 * 
	 * @return the string
	 */
	public static String generateID() {
		return UUID.randomUUID().toString();
	}
}
