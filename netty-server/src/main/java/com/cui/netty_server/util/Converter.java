package com.cui.netty_server.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * 字节转换工具类
 * 
 * @author cuipengfei
 * 
 */
public class Converter {

	/**
	 * 把无符号32位long转换为字节数组
	 * 
	 * @param val
	 * @return
	 */
	public static byte[] unSigned32LongToBigBytes(long val) {
		byte[] b = new byte[4];
		b[3] = (byte) (val >> 0);
		b[2] = (byte) (val >> 8);
		b[1] = (byte) (val >> 16);
		b[0] = (byte) (val >> 24);
		return b;
	}

	/**
	 * 把字节数组转换为无符号32位long
	 * 
	 * @param b
	 * @param pos
	 * @return
	 */
	public static long bigBytes2Unsigned32Long(byte[] b, int pos) {
		int firstByte = 0;
		int secondByte = 0;
		int thirdByte = 0;
		int fourthByte = 0;
		int index = pos;
		firstByte = (0x000000FF & ((int) b[index + 3]));
		secondByte = (0x000000FF & ((int) b[index + 2]));
		thirdByte = (0x000000FF & ((int) b[index + 1]));
		fourthByte = (0x000000FF & ((int) b[index + 0]));
		index = index + 4;
		return ((long) (fourthByte << 24 | thirdByte << 16 | secondByte << 8 | firstByte)) & 0xFFFFFFFFL;
	}

	/**
	 * 把无符号16位int转换为字节数组
	 * 
	 * @param val
	 * @return
	 */
	public static byte[] unSigned16IntToBigBytes(int val) {
		byte[] b = new byte[2];
		b[1] = (byte) (val >> 0);
		b[0] = (byte) (val >> 8);
		return b;
	}

	/**
	 * 把字节数组转换为无符号16位int
	 * 
	 * @param b
	 * @param pos
	 * @return
	 */
	public static int bigBytes2Unsigned16Int(byte[] b, int pos) {
		int firstByte = 0;
		int secondByte = 0;
		int index = pos;
		firstByte = (0x00FF & ((int) b[index + 1]));
		secondByte = (0x00FF & ((int) b[index + 0]));
		index = index + 2;
		return (secondByte << 8 | firstByte) & 0xFFFF;
	}

	/**
	 * LHY
	 * 
	 * @param str
	 *            必须为2的倍数，且为纯字符串
	 * @return
	 */
	public static byte[] str2BCD(String str) {
		if (str == null)
			throw new IllegalArgumentException("输入转换参数为null!");
		int strLen = str.length();
		if (strLen % 2 != 0) {
			throw new IllegalArgumentException("参数不合法,长度必须为2的倍数!");
		}
		int len = strLen / 2;
		byte[] bcd = new byte[len];
		byte[] temp = str.getBytes();
		for (int i = temp.length; i > 0; i -= 2) {
			byte low = (byte) (temp[i - 1] - 48);
			byte high = (byte) (temp[i - 2] - 48);
			bcd[(i / 2 - 1)] = (byte) (((high << 4) & 0xff) + low);
		}
		return bcd;

	}

	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bcd2Str(byte[] b, int from, int len) {
		byte[] bytes = new byte[len];
		System.arraycopy(b, from, bytes, 0, len);

		return bcd2Str(bytes);
	}

	public static String bcd2Str(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(byte2HexStr(bytes[i]));
		}
		return sb.toString();
	}

	public static String byte2HexStr(byte b) {
		String hs = "";
		String stmp = "";
		stmp = (Integer.toHexString(b & 0XFF));
		if (stmp.length() == 1)
			hs = hs + "0" + stmp;
		else
			hs = hs + stmp;
		// if (n<b.length-1) hs=hs+":";
		return hs.toUpperCase();
	}

	/**
	 * 
	 * int2Double:(经纬度从int转换成double). 
	 *
	 * @author sid
	 * @param i
	 * @return
	 */
	public static double int2Double(Long i){
		double result = i;
		if (null!=i) {
			return result/600000d;
		}
		return 0d;
	}

	/**
	 * 
	 * double2Int:(经纬度从double转换成int). 
	 *
	 * @author sid
	 * @param i
	 * @return
	 */
	public static long double2Int(Double i){
		if (null!=i) {
			return (long)(i.doubleValue()*600000);
		}
		return 0;
	}

	/**
	 * String转GBK Bytes
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] getBytes(String s) {
		return s.getBytes(Charset.forName("GBK"));
	}

	/**
	 * byte[] 转String
	 * 
	 * @param b
	 * @return
	 */
	public static String toGBKString(byte[] b) {
		try {
			String s = new String(b, "GBK");
			return s.trim();
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	/**
	 * byte[] 转String
	 * 
	 * @param b
	 * @param startIndex
	 * @param length
	 * @return
	 */
	public static String toGBKString(byte[] b, int startIndex, int length) {
//		byte[] b1 = new byte[length];
//		System.arraycopy(b, startIndex, b1, 0, length);
		return toGBKString(Arrays.copyOfRange(b, startIndex, length));
	}
}
