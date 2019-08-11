package com.phone;

import java.util.HashMap;
import java.util.Map;

public class Key {

	public static final String ID =    "";
	public static final String key =  "";
	
	public static Map<String, String> countryCodeMap = new HashMap<String, String>();
	static {
		countryCodeMap.put("India", "91");
		countryCodeMap.put("USA", "1");
	}
}
