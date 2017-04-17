package com.vtron.lc.common;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ObjectUtils {
	public static boolean isEmpty(Object o) {
		if (o == null) {
			return true;
		}
		if (o instanceof String) {
			return "".equals(o);
		} else if (o instanceof Collection) {
			return ((Collection<?>) o).isEmpty();
		}
		return false;
	}

	public static boolean isNotEmpty(Object o) {
		return !isEmpty(o);
	}

	public static String genGetMethodName(String fildeName) {
		byte[] items = fildeName.getBytes();
		items[0] = (byte) ((char) items[0] - 'a' + 'A');
		return "get" + new String(items);
	}

	public static int doubleToInt(double d) {
		return Integer.parseInt(new java.text.DecimalFormat("0").format(d));
	}

	public static int getPages(int count, int size) {
		if (count % size > 0) {
			return count / size + 1;
		}
		return count / size;
	}

	/**
	 * 判断字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
		String bigStr;
		try {
			bigStr = new BigDecimal(str).toString();
		} catch (Exception e) {
			return false;
		}
		Matcher isNum = pattern.matcher(bigStr);
		return isNum.matches();
	}
}
