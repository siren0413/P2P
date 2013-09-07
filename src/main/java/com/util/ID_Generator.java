package com.util;

import java.util.UUID;

public class ID_Generator {

	public static String generateID() {
		return UUID.randomUUID().toString();
	}
}
