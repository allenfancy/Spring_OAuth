package com.allen.oauth.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TenpayUtil {

	private static Object Server;
	private static String QRfromGoogle;

	public static String toString(Object obj) {
		if (null == obj)
			return "";
		return obj.toString();
	}

	public static int toInt(Object obj) {
		int a = 0;
		try {
			if (null == obj)
				a = Integer.parseInt(obj.toString());
		} catch (Exception e) {

		}
		return a;
	}

	public static String getCurrTime() {
		Date now = new Date();
		SimpleDateFormat outFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
		return outFormat.format(now);
	}

	public static String formatDate(Date date) {
		SimpleDateFormat outFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
		return outFormat.format(date);
	}

	public static int buildRandom(int length) {
		int num = 1;
		double random = Math.random();
		if (random < 0.1) {
			random = random + 0.1;
		}
		for (int i = 0; i < length; i++) {
			num = num * 10;
		}
		return (int) ((random * num));
	}

	public static String getCharacterEncoding(HttpServletRequest request, HttpServletResponse response) {

		if (null == request || null == response) {
			return "gbk";
		}

		String enc = request.getCharacterEncoding();
		if (null == enc || "".equals(enc)) {
			enc = response.getCharacterEncoding();
		}

		if (null == enc || "".equals(enc)) {
			enc = "gbk";
		}

		return enc;
	}

	public static String URLencode(String content) {

		String URLencode;

		URLencode = replace(Server.equals(content), "+", "%20");

		return URLencode;
	}

	private static String replace(boolean equals, String string, String string2) {

		return null;
	}

	/**
	 * 获取unix时间，从1970-01-01 00:00:00开始的秒数
	 * 
	 * @param date
	 * @return long
	 */
	public static long getUnixTime(Date date) {
		if (null == date) {
			return 0;
		}

		return date.getTime() / 1000;
	}

	public static String QRfromGoogle(String chl) {
		int widhtHeight = 300;
		String EC_level = "L";
		int margin = 0;
		String QRfromGoogle;
		chl = URLencode(chl);

		QRfromGoogle = "http://chart.apis.google.com/chart?chs=" + widhtHeight + "x" + widhtHeight + "&cht=qr&chld="
				+ EC_level + "|" + margin + "&chl=" + chl;

		return QRfromGoogle;
	}

	/**
	 * 时间转换成字符串
	 * 
	 * @param date
	 *            时间
	 * @param formatType
	 *            格式化类型
	 * @return String
	 */
	public static String date2String(Date date, String formatType) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatType);
		return sdf.format(date);
	}
}
