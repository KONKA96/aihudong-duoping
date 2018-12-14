package com.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class TestSha1 {
	public static String testSha1(String tenant,String user) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		Map<String, String> sortSignParams = new HashMap<String, String>();
		sortSignParams.put("param1", user);
		sortSignParams.put("param2", tenant);
		
		
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : sortSignParams.entrySet()) {
		    String k = (String) entry.getKey();
		    String v = (String) entry.getValue();
		    sb.append(k.toLowerCase() + "=" + v + "&");
		}
		String str = sb.substring(0, sb.lastIndexOf("&"));
		System.out.println("str--->"+str);
		MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
		mdTemp.update(str.getBytes("UTF-8"));
		byte[] md = mdTemp.digest();

		final String HEX = "0123456789abcdef";
		StringBuilder sb1 = new StringBuilder(md.length * 2);
		for (byte b : md) {
			sb1.append(HEX.charAt((b >> 4) & 0x0f));
			sb1.append(HEX.charAt(b & 0x0f));
		}
		System.out.println(sb1.toString());
		return sb1.toString();
	}
	
	public static String testSha2(String tenant,String user) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		SortedMap<String, String> sortSignParams = new TreeMap<String, String>();
		sortSignParams.put("tenant", tenant);
		sortSignParams.put("user", user);
		
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : sortSignParams.entrySet()) {
		    String k = (String) entry.getKey();
		    String v = (String) entry.getValue();
		    sb.append(k.toLowerCase() + "=" + v + "&");
		}
		String str = sb.substring(0, sb.lastIndexOf("&"));
		System.out.println("str--->"+str);
		MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
		mdTemp.update(str.getBytes("UTF-8"));
		byte[] md = mdTemp.digest();

		final String HEX = "0123456789abcdef";
		StringBuilder sb1 = new StringBuilder(md.length * 2);
		for (byte b : md) {
			sb.append(HEX.charAt((b >> 4) & 0x0f));
			sb.append(HEX.charAt(b & 0x0f));
		}
		System.out.println(sb1.toString());
		return sb1.toString();
	}
}
