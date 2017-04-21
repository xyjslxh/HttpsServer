package com.vtron.it.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Utils {
	

	// /**
	// * 四舍五入取整数
	// *
	// * @param data
	// * @return
	// */
	// public static int round(Double data) {
	// return new BigDecimal(data).setScale(0,
	// BigDecimal.ROUND_HALF_UP).intValue();
	// }

	/**
	 * Base64加密
	 * 
	 * @param str
	 * @return
	 */
	public static String encodeByBase64(String str) {
		byte[] b = null;
		String result = null;
		try {
			b = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (ObjectUtils.isNotEmpty(b)) {
			result = new BASE64Encoder().encode(b);
		}
		return result;
	}

	/**
	 * Base64解密
	 * 
	 * @param str
	 * @return
	 */
	public static String decodeByBase64(String str) {
		byte[] b = null;
		String result = null;
		if (ObjectUtils.isNotEmpty(str)) {
			try {
				b = new BASE64Decoder().decodeBuffer(str);
				result = new String(b);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
