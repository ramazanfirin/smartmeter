package com.masterteknoloji.net.web.rest.util;

import java.util.Base64;

public class LoraMessageUtil {

	public static String base64ToHex(String base64Value) {

		byte[] decodedBytes = Base64.getDecoder().decode(base64Value);
		StringBuilder hexString = new StringBuilder();
		for (byte b : decodedBytes) {
			hexString.append(String.format("%02x", b));
		}

		return hexString.toString();
	}

	public static String removeFirst8Chars(int count,String input) {
		// Eğer giriş değeri 8 karakterden kısa ise boş string döner
		if (input == null || input.length() <= count) {
			return ""; // Kısa ise tüm string silinir
		}
		// İlk 8 karakteri çıkar ve geri kalan kısmı döndür
		return input.substring(count);
	}

	public static String getFirst14Chars(String input) {
		if (input == null || input.length() == 0) {
			return ""; // Eğer string boş veya null ise boş string döndür
		}
		// Eğer string'in uzunluğu 14'ten küçükse, tüm string'i döndür
		return input.length() > 14 ? input.substring(0, 14) : input;
	}

	public static byte[] appendToByteArray(byte[] original, byte[] toAppend) {
		if (original == null) {
			return toAppend;
		}
		if (toAppend == null) {
			return original;
		}

		byte[] result = new byte[original.length + toAppend.length];

		System.arraycopy(original, 0, result, 0, original.length);

		System.arraycopy(toAppend, 0, result, original.length, toAppend.length);

		return result;
	}

	public static byte[] hexStringToByteArray(String hex) {
	    int len = hex.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
	                              + Character.digit(hex.charAt(i + 1), 16));
	    }
	    return data;
	}
	
	public static String[] parseHex(String hex) {
		String[] hexArray = hex.replace(" ", "").split("(?<=\\G.{2})");
		return hexArray;
	}
}
