package com.util;

import java.util.UUID;

public class ID_Generator {
	/*
	 * Generate unique id 
	 */

	public static String generateID() {
		return UUID.randomUUID().toString();
	}
}
