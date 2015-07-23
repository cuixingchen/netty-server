package com.cui.netty_server.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Pattern;

/**
 * 序列化工具类
 * @author cuipengfei
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked","unused" })
public final class DataTypeUtil {

//	public static int bytesToInt(byte[] b) {
//
//		int mask = 0xff;
//		int temp = 0;
//		int n = 0;
//		for (int i = 0; i < b.length; i++) {
//			n <<= 8;
//			temp = b[i] & mask;
//			n |= temp;
//		}
//		return n;
//	}
	
	
	
//	public static long bytesToLong(byte[] b) {
//
//		int mask = 0xff;
//		int temp = 0;
//		long n = 0;
//		for (int i = 0; i < b.length; i++) {
//			n <<= 8;
//			temp = b[i] & mask;
//			n |= temp;
//		}
//		return n;
//	}
	
	
	public static String longToHex(long l, int length) {
		String temp = Long.toHexString(l);
		while (temp.length()<length*2) {
			temp = "0" + temp;
		}
		return temp;
	}
	
	
	public static byte[] getDevBytes(String devid) {
		int len = devid.length();
		while (len<10) {
			devid = "0"+devid;
			len++;
		}
		return strToBCD(devid);
	}
	
	
	public static String IntToHex(int i, int length) {
		String temp = Integer.toHexString(i);
		while (temp.length()<length*2) {
			temp = "0" + temp;
		}
		return temp;
	}
	
	public static int HexToInt(String hex) {
		return Integer.parseInt(hex, 16);
	}
	
//	public static String bytesToBinary(byte[] b, int byteLength) {
//		int i = bytesToInt(b);
//		String binString = Integer.toBinaryString(i);
//		while (binString.length() < byteLength*8) {
//			binString = "0" + binString;
//		}
//		return binString;
//	}
	
	public static String intToBinary(int i, int byteLength) {
		String binString = Integer.toBinaryString(i);
		while (binString.length() < byteLength*8) {
			binString = "0" + binString;
		}
		return binString;
	}
	
	public static String binaryToInt(String binary, int intlength){
		String s = Integer.parseInt(binary,2)+"";
		while ((s+"").length()<intlength){
			s = "0"+s;
		}
		return s;
	}
	
	public static byte[] getLittleData(byte[] b) {
		int len = b.length;
		byte[] temp = new byte[len];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = b[len-1-i];
		}
		return temp;
	}
	
	public static String getSrcData(String hex) {
		return hex;
	}
	
	public static byte[] getLittleEndian(byte[] b) {
		byte[] temp = new byte[b.length];
		for (int i = 0; i < b.length; i++) {
			temp[b.length-i-1] = b[i];
		}
		return temp;
	}
	
	public static String getLittleEndian(String hex) {
		int len = hex.length();
		if (len%2==1)return null;
		String str = "";
		for (int i = len/2; i > 0; i--) {
			str += hex.substring((i-1)*2, i*2);
		}
		return str;
	}
	
	/**
	 * ���캯��
	 */
	public DataTypeUtil() {
	}

	/**
     * ���ֽ�ת�����޷�ŵ������ַ���ʽ
     * @param src
     * @return
     */
    public static String byte2Hexs(byte src) {
        try {
        	String stmp = (java.lang.Integer.toHexString(src & 0XFF));
            if (stmp.length() == 1) {
            	stmp = "0" + stmp;
            }
            return stmp.toUpperCase();
        }
        catch (java.lang.NullPointerException ex) {
            return null;
        }
    }
    
	/**
	 * �ж�child�Ƿ���parent������
	 * 
	 * @param parent
	 *            Class
	 * @param child
	 *            Class
	 * @return boolean
	 */
	public static boolean isChildClass(Class parent, Class child) {

		if (child == null || parent == null)
			return false;
		Class cls = child;
		while (!cls.getName().equals(parent.getName())) {
			cls = cls.getSuperclass();
			if (cls == null)
				return false;
		}
		return true;
	}
	
	public static Object invokeMethod(String className, String methodName,

	Object[] args) {
		return invokeMethod(className, methodName, null, args);
	}

	/**
	 * ����Java�ķ���
	 * <p>
	 * add by wjz 040825
	 * 
	 * @param className
	 *            String ����ƣ�����Package��
	 * @param methodName
	 *            String �������
	 * @param args
	 *            Object[] ���ò���
	 * @return Object
	 */
	public static Object invokeMethod(String className, String methodName,
			Class[] argsType, Object[] args) {
		try {
			Class cls = Class.forName(className);
			Object obj = null;
			if (cls == null)
				return obj;
			Constructor classConstructor = cls.getConstructor(null);

			Object clsObj = classConstructor.newInstance(null);
			// �в������ͣ�ֱ�ӵ���
			if (argsType != null) {
				Method m = cls.getMethod(methodName, argsType);
				if (m == null)
					return null;
				obj = m.invoke(clsObj, args);
				if (obj == null)
					return "true";
				return obj;
			}
			Class argCls[];
			// ���ֻ��һ��ͬ��ģ�ֱ�ӵ���
			int count = 0;
			Method m = null;
			Method[] ma = cls.getMethods();
			if (ma == null || ma.length == 0)
				return null;
			for (int i = 0; i < ma.length; i++) {
				if (ma[i] == null || !ma[i].getName().equals(methodName)) {
					continue;
				}
				m = ma[i];
				count++;

			}
			if (count == 1) {
				obj = m.invoke(clsObj, args);
				if (m.getReturnType() == null && obj == null)
					return "true";
				return obj;

			}
			// ���ڶ��ͬ������Ҫ�����ж�
			for (int i = 0; i < ma.length; i++) {
				if (ma[i] == null || !ma[i].getName().equals(methodName)) {
					continue;
				}
				argCls = ma[i].getParameterTypes();
				if ((argCls == null || argCls.length == 0 || args == null || args.length == 0)) {
					obj = ma[i].invoke(clsObj, args);
					if (ma[i].getReturnType() == null && obj == null)
						return "true";
					return obj;

				} else {
					// ����Ⱥ����Ͳ�һ��,�򲻵���
					if (argCls.length != args.length)
						continue;
					int j;
					for (j = 0; j < argCls.length; j++) {
						if (args[j] == null || args[j].getClass() == null)
							break;
						if (!isChildClass(argCls[j], args[j].getClass())) {
							break;
						}
					}
					if (j != argCls.length) {
						continue;
					}

					obj = ma[i].invoke(clsObj, args);
					if (ma[i].getReturnType() == null && obj == null)
						return "true";
					return obj;

				}

			}

			return obj;
		} catch (Throwable ex) {
			LogUtil.getLogger().error(ex.getMessage(), ex);
			return null;
		}
	}

	/**
	 * ���ϻ� 2004-01-13 ��Ӵ˷���
	 * 
	 * @param className
	 *            ������
	 * @param argsType
	 *            �������
	 * @param argsValue
	 *            ����ֵ
	 * @return ����ʵ���Ժ�Ķ���
	 */
	public static Object getClassInstance(String className, Class[] argsType,
			Object[] argsValue) {
		try {
			Class dynamicClass = Class.forName(className);
			Constructor classConstructor = dynamicClass
					.getConstructor(argsType);
			Object objInstance = classConstructor.newInstance(argsValue);
			return objInstance;
		} catch (Exception ex) {
			LogUtil.getLogger().error(ex.getMessage(), ex);
			return null;
		}
	}
	
	/**
	 * ���ϻ� 2004-01-13 ��Ӵ˷���
	 * 
	 * @param className
	 *            ������
	 * @param argsType
	 *            �������
	 * @param argsValue
	 *            ����ֵ
	 * @return ����ʵ���Ժ�Ķ���
	 */
	public static Object getClassInstance(String className) {
		return getClassInstance(className, null, null);
	}

	// add by hejiang 20030725
	/**
	 * Gets the DateStr attribute of the DataTypeUtil class
	 * 
	 * @return The DateStr value
	 */
	public static synchronized String getDateStr() {
		java.util.Date now = new java.util.Date();
		String s = DateUtil.getStringDate("yyyy-MMdd-HHmmss");
		return s;
	}

	// add by hejiang

	/**
	 * ����8λ������ַ�
	 * <p>
	 * wujinzhong 0530
	 * <p>
	 * 
	 * @return ��Ӧ������ֵ,������,�򷵻�Integer.MIN_VALUE
	 */

	public static synchronized String getRandomStr() {
		java.util.Date now = new java.util.Date();
		String s = DateUtil.getStringDate("yyyyMMddHHmmssSSS");
		try {
			Thread.sleep(1);
		} catch (Exception ex) {
			LogUtil.getLogger()
					.error("FATAL ERROR! get random string fail", ex);
		}
		return s;
		// return Long.toString(now.getTime());
		// return String.valueOf((int) (Math.random() * 100000000));

	}

	/**
	 * ��Hashtable�ļ�ֵ���� wjz 0830
	 * 
	 * @param ht
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 */

	public static Object[] getSortedKey(Hashtable ht) {
		if (ht == null) {
			return null;
		}

		try {
			Vector v = new Vector(ht.keySet());
			Collections.sort(v);
			Object[] keys = v.toArray();
			return keys;
		} catch (Exception ex) {
			LogUtil.getLogger().error("sort error", ex);
			return ht.keySet().toArray();
		}

	}

	// end by add

	/**
	 * ����8λ��������Σ�
	 * 
	 * @param bt1
	 *            Description of Parameter
	 * @param bt2
	 *            Description of Parameter
	 * @return ��Ӧ������ֵ,������,�򷵻�Integer.MIN_VALUE
	 */
	// public static int getRandomInt() {
	// return (int) (Math.random() * 100000000);
	// }
	// end by add
	/**
	 * ����8λ��������Σ� ����8λ��������Σ� ����8λ��������Σ� ����8λ��������Σ� ����8λ��������Σ� ����8λ��������Σ�
	 * ����8λ��������Σ� ����8λ��������Σ� ����8λ��������Σ� �ж������ֽ������Ƿ����
	 * 
	 * @param bt1
	 *            Description of Parameter
	 * @param bt2
	 *            Description of Parameter
	 * @return ��Ӧ������ֵ,������,�򷵻�Integer.MIN_VALUE
	 * @return The BytesEquals value
	 */
	public static boolean isBytesEquals(byte bt1[], byte bt2[]) {
		if (bt1.length != bt2.length) {
			return false;
		}
		for (int i = 0; i < bt1.length; i++) {
			if (bt1[i] != bt2[i]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * obj���ַ��ʾ��ʽ�Ƿ�Ϊ���ַ�null��"")
	 * <p>
	 * add by wjz 040401
	 * 
	 * @param obj
	 *            Ŀ�����
	 * @return ���ַ���ʽΪnull��""����Ϊtrue,����Ϊfalse
	 */
	public static boolean isEmptyStr(Object obj) {
		if (obj == null) {
			return true;
		}
		try {
			String s = obj.toString();
			if (s == null || s.trim().length() == 0) {
				return true;
			}
			return false;
		} catch (Exception ex) {
			return false;
		}

	}

	/**
	 * �ֽ����鷴�����
	 * 
	 * @param source
	 *            Description of Parameter
	 * @param search
	 *            Description of Parameter
	 * @param start
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 */
	public static int byteBackwardIndexOf(byte source[], byte search[],
			int start) {
		int iRet = -1;
		int i = start;
		do {
			if (i <= 0) {
				break;
			}
			if (i - search.length < 0) {
				iRet = -1;
				break;
			}
			if (isBytesEquals(subBytes(source, (i - search.length) + 1, i),
					search)) {
				iRet = (i - search.length) + 1;
				break;
			}
			i--;
		} while (true);
		return iRet;
	}

	/**
	 * �ֽ��������
	 * 
	 * @param source
	 *            Description of Parameter
	 * @param search
	 *            Description of Parameter
	 * @param start
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 */
	public static int byteIndexOf(byte source[], byte search[], int start) {
		int iRet = -1;
		int i = start;
		do {
			if (i >= source.length) {
				break;
			}
			if (i + search.length > source.length) {
				iRet = -1;
				break;
			}
			if (isBytesEquals(subBytes(source, i, (i + search.length) - 1),
					search)) {
				iRet = i;
				break;
			}
			i++;
		} while (true);
		return iRet;
	}

	/**
	 * Ϊһ����ά�������ӻ������
	 * 
	 * @author wujinzhong 050530
	 * @param data
	 *            Object[][]
	 * @param cloumnsCount
	 *            int ���ӻ���ٵ����������ʾ�����У������ʾ������
	 * @return Object[][]
	 */
	public static Object[][] addArrayColumns(Object[][] data, int cloumnsCount) {
		if (data == null)
			return null;
		if (cloumnsCount == 0)
			return data;
		int col_len = data[0].length + cloumnsCount;
		int row_len = data.length;
		Object[][] data2 = new Object[row_len][col_len];
		int copy_col_len;// ʵ��Ҫ����������
		if (cloumnsCount >= 0) {
			copy_col_len = data[0].length;
		} else {
			copy_col_len = col_len;
		}
		for (int i = 0; i < row_len; i++) {
			System.arraycopy(data[i], 0, data2[i], 0, copy_col_len);
		}

		return data2;
	}
	
	
	public static String strPaddingFront(String src, int length, String padstr) {
		if (src==null)src="";
		while (src.length()<length) {
			src = padstr + src;
		}
		return src;
	}

	/**
	 * ���ֽ�����ת��Ϊ16�����ַ��� {10,-1}-->0aFF
	 * 
	 * @param src
	 *            Դ����
	 * @return �ַ�,null��ʾʧ��
	 * @roseuid 3C15CF9502F8
	 */
	public static String bytes2Hexs(byte[] src) {

		StringBuffer hs = new StringBuffer();
		String stmp = "";
		hs.setLength(src.length * 2);
		int t = 0;
		try {

			for (int n = 0; n < src.length; n++) {
				stmp = (java.lang.Integer.toHexString(src[n] & 0XFF));
				if (stmp.length() == 1) {
					hs.setCharAt(t, '0');
					t++;
					hs.setCharAt(t, stmp.charAt(0));
					t++;
				} else {
					hs.setCharAt(t, stmp.charAt(0));
					t++;
					hs.setCharAt(t, stmp.charAt(1));
					t++;
				}
			}
		}
		// try
		catch (java.lang.NullPointerException ex) {
			return null;
		}
		String strTemp = hs.toString().toUpperCase();
		hs = null;
		return strTemp;
	}

	/**
	 * ���ֽ�����ת��Ϊ16�����ַ��� {10,-1}-->0aFF
	 * ÿ���ֽں���һ���ո�
	 * @param src
	 *            Դ����
	 * @return �ַ�,null��ʾʧ��
	 * @roseuid 3C15CF9502F8
	 */
	public static String bytes2HexsSpace(byte[] src) {

		StringBuffer hs = new StringBuffer();
		String stmp = "";
		hs.setLength(src.length * 3);
		int t = 0;
		try {

			for (int n = 0; n < src.length; n++) {
				stmp = (java.lang.Integer.toHexString(src[n] & 0XFF));
				if (stmp.length() == 1) {
					hs.setCharAt(t, '0');
					t++;
					hs.setCharAt(t, stmp.charAt(0));
					t++;
				} else {
					hs.setCharAt(t, stmp.charAt(0));
					t++;
					hs.setCharAt(t, stmp.charAt(1));
					t++;
				}
				hs.setCharAt(t, ' ');
				t++;
			}
		}
		// try
		catch (java.lang.NullPointerException ex) {
			return null;
		}
		String strTemp = hs.toString().toUpperCase();
		hs = null;
		return strTemp;
	}
	/**
	 * �ֽ����鿽��
	 * 
	 * @param source
	 *            Դ�ֽ�����
	 * @param dest
	 *            Ŀ���ֽ�����
	 * @param len
	 *            ��������
	 * @return 0���ɹ� >0��ʾʧ��,ֵΪ��ӦERROR_CODE
	 * @roseuid 3C183B36025A
	 */
	public static int bytesCopy(byte[] source, byte[] dest, int len) {
		try {
			for (int i = 0; i < len; i++) {
				dest[i] = source[i];
			}

			return 0;
		} catch (Exception ex) {
			LogUtil.getLogger().error(
					"ByteProcess_Java" + ":bytesCopy:", ex);
			return 100;
		}
	}

	/**
	 * ��HashMap�в���ĳ��keyd��ֵ,֧�ֵݹ����
	 * <p>
	 * add by wjz 0226
	 * 
	 * @param map
	 *            Description of the Parameter
	 * @param key
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static Object findMapValueByKey(Map map, Object key) {
		if (map == null || key == null) {
			return null;
		}
		Object[] keys = map.keySet().toArray();
		if (keys == null) {
			return null;
		}
		int len = keys.length;
		Object key2 = null;
		for (int i = 0; i < len; i++) {
			key2 = keys[i];
			if (key2 == null) {
				continue;
			}
			if (key2.equals(key)) {
				return map.get(key2);
			}
			Object v1 = map.get(key2);
			if (v1 != null && v1 instanceof Map) {
				Object v2 = findMapValueByKey((Map) v1, key);
				if (v2 != null) {
					return v2;
				}
			}
		}
		return null;
	}

	/**
	 * ��16�����ַ�ת��Ϊ�ֽ������� 0aFF-->{10,-1}
	 * 
	 * @param src
	 *            �ַ�
	 * @return Ŀ������,null��ʾʧ��
	 * @roseuid 3C15D0230126
	 */
	public static byte[] hexs2Bytes(java.lang.String src) {
		try {

			int len = src.length() / 2;
			int itemp;
			String stmp;
			if (src.length() % 2 == 1) {
				return null;
			}
			byte[] bt = new byte[len];

			for (int i = 0; i < len; i++) {
				stmp = src.substring(i * 2, i * 2 + 2);
				// itemp = Integer.decode("0x" + stmp).intValue() - 256;
				itemp = Integer.decode("0x" + stmp).intValue();
				bt[i] = (byte) itemp;

			}
			return bt;
		} catch (java.lang.NumberFormatException e) {
			LogUtil.getLogger().error(
					"ByteProcess_Java" + ":hexs2Bytes:", e);
			return null;
		}
	}

	// ���ַ���MD5����
	public static String MD5(String src) {
		MessageDigest md = null;
		String des = "";
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			LogUtil.getLogger().error("MD5 error", e);
		}
		md.update(src.getBytes());
		byte[] b = md.digest();
		des = bytes2Hexs(b);
		return des;
	}
	
	
	public static String MD5UTF8(String src) {
		MessageDigest md = null;
		String des = "";
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			LogUtil.getLogger().error("MD5 error", e);
		}
		try {
			md.update(src.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			LogUtil.getLogger().error("MD5 utf-8 error", e);
			e.printStackTrace();
		}
		byte[] b = md.digest();
		des = bytes2Hexs(b);
		return des;
	}
	

	/**
	 * The main program for the DataTypeUtil class
	 * 
	 * @param args
	 *            The command line arguments
	 */
	public static void main(String[] args) {
		int s = 0x9000;
		byte[] b = DataTypeUtil.toByteArray32Long(s);
	}

	/**
	 * ����������sjlx,��SRC���д���
	 * 
	 * @param src
	 *            Description of Parameter
	 * @param errCode
	 *            Description of Parameter
	 * @param sjlx
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 */

	public static byte[] manageBytes(byte[] src, StringBuffer errCode,
			String sjlx) {
		byte[] bt2 = src;
		if (sjlx.substring(0, 1).equals("1")) {
			return null;
		}
		if (sjlx.substring(2, 3).equals("1")) {
			return null;
		}
		return bt2;
	}

	/**
	 * ȡ��һ�����е����ֵ
	 * <p>
	 * modified by wjz 1005:change intValue to doubleValue
	 * </p>
	 * 
	 * @param iArr
	 *            Description of Parameter
	 * @return
	 */
	public static Number maxNum(Number iArr[]) {
		if (iArr == null || iArr.length <= 0) {
			return null;
		}
		Number rtn = iArr[0];
		for (int i = 1; i < iArr.length; i++) {
			if (rtn.doubleValue() > iArr[i].doubleValue()) {
				continue;
			}
			if (rtn.doubleValue() < iArr[i].doubleValue()) {
				rtn = iArr[i];
				continue;
			}
		}
		return rtn;
	}

	/**
	 * ȡ��һ�����е���Сֵ
	 * <p>
	 * modified by wjz 1005:change intValue to doubleValue
	 * </p>
	 * 
	 * @param iArr
	 *            Description of Parameter
	 * @return
	 */
	public static Number minNum(Number iArr[]) {
		if (iArr == null || iArr.length <= 0) {
			return null;
		}
		Number rtn = iArr[0];
		for (int i = 1; i < iArr.length; i++) {
			if (rtn.doubleValue() < iArr[i].doubleValue()) {
				continue;
			}
			if (rtn.doubleValue() > iArr[i].doubleValue()) {
				rtn = iArr[i];
				continue;
			}
		}
		return rtn;
	}

	/**
	 * ��ӡ�ַ����� {10,FF} type=0��ӡ10��-1 type=1��ӡ0A��FF
	 * 
	 * @param src
	 *            ��Ҫ��ӡ������
	 * @param type
	 *            0-��ӡ�ַ��ʮ����ֵ��1����ӡ�ַ��ʮ�����ֵ
	 * @return ����ַ�
	 * @roseuid 3C183E6F030A
	 */
	public String printCharArray(String src, int type) {
		return null;
	}

	/**
	 * ��ӡ���������
	 * <p>
	 * wujinzhong 0604,040922
	 * </P>
	 * 
	 * @param obj
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 */
	public static String printObject(Object obj) {
		StringBuffer sb = new StringBuffer();
		if (obj == null)
			return "null";
		if (obj instanceof HashMap) {
			HashMap map = (HashMap) obj;
			Iterator keys = map.keySet().iterator();
			while (keys.hasNext()) {
				sb.append("\t");
				Object key = keys.next();
				sb.append("key=" + key);
				sb.append(printObject(map.get(key)));
				sb.append("\n");
			}
			return sb.toString();
		} else if (obj instanceof ArrayList) {

			ArrayList ary = (ArrayList) obj;
			Iterator values = ary.iterator();
			while (values.hasNext()) {
				sb.append("\t");
				sb.append(printObject(values.next()));
				sb.append("\n");
			}

			return sb.toString();
		} else if (obj instanceof byte[]) {
			byte[] bts = (byte[]) (obj);
			sb.append("size=" + bts.length + ",data={");
			for (int i = 0; i < bts.length; i++) {
				sb.append(bts[i] + ",");
			}
			sb.append("}");
			return sb.toString();
		} else {
			return obj.toString();
		}

	}

	/**
	 * ���������㷨
	 * 
	 * @param strNumber
	 *            ��������ǰ���ַ�
	 * @param cont
	 *            С��������λ��
	 * @return ���������Ժ� ���ַ�
	 */
	public static String round(String strNumber, int cont) {
		int mi = 1;
		for (int i = 0; i <= cont; i++) {
			mi = mi * 10;
		}
		if (strNumber == null) {
			strNumber = "0";
		}
		double d = new Double(strNumber).doubleValue();
		BigDecimal number = new BigDecimal(d * mi);
		BigInteger integer = new BigInteger("0");
		int end = Integer.parseInt(number.toBigInteger().toString().substring(
				number.toBigInteger().toString().length() - 1));
		if (end >= 5) {
			if (d > 0) {
				integer = number.toBigInteger().add(new BigInteger("10"));
			} else {
				integer = number.toBigInteger().subtract(new BigInteger("10"));
			}
		} else {
			integer = number.toBigInteger();
		}
		integer = integer.divide(new BigInteger("10"));
		number = new BigDecimal(integer, cont);
		return number.toString();
	}

	/**
	 * ��Hashtable���� wjz 0830
	 * 
	 * @param ht
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 */
	public static Hashtable sortHashTable(Hashtable ht) {
		if (ht == null || ht.size() == 0) {
			return ht;
		}
		try {

			// ht.put("03", "�����걨");
			// ht.put("01", "��ֵ˰һ����");
			// ht.put("04", "���ʼ���");
			// ht.put("02", "��ֵ˰С��ģ");
			Vector v = new Vector(ht.keySet());
			// zxt.pub.util.LogUtil.getLogger().debug(v);
			Collections.sort(v);
			Object[] keys = v.toArray();
			int len = ht.size();
			Hashtable ht2 = new Hashtable(len);
			for (int i = 0; i < len; i++) {

				ht2.put(keys[i], ht.get(keys[i]));
			}
			return ht2;
		} catch (Exception ex) {
			LogUtil.getLogger().error("sort error", ex);
			return ht;
		}

	}

	/**
	 * ȡ������
	 * 
	 * @param source
	 *            Description of Parameter
	 * @param from
	 *            Description of Parameter
	 * @param end
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 */
	public static byte[] subBytes(byte source[], int from, int end) {
		if (from > end || end > source.length - 1) {
			return null;
		}
		byte btRet[] = new byte[(end - from) + 1];
		for (int i = 0; i <= end - from; i++) {
			btRet[i] = source[i + from];
		}

		return btRet;
	}

	/**
	 * Description of the Method
	 * 
	 * @param foo
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 */
	public static byte[] toByteArray16Int(int foo) {
		return toByteArray(foo, new byte[2]);
	}

	/**
	 * Description of the Method
	 * 
	 * @param foo
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 */
	public static byte[] toByteArray32Long(long foo) {
		return toByteArray(foo, new byte[4]);
	}

	/**
	 * Description of the Method
	 * 
	 * @param foo
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 */
	public static byte[] toByteArray64Long(long foo) {
		return toByteArray(foo, new byte[8]);
	}

	/**
	 * Description of the Method
	 * 
	 * @param foo
	 *            Description of Parameter
	 * @param array
	 *            Description of Parameter
	 * @return Description of the Return Value
	 * @eturn Description of the Returned Value
	 */
	private static byte[] toByteArray(long foo, byte[] array) {
		int len = array.length;
		for (int iInd = 0; iInd < len; iInd++) {
			array[iInd] = (byte) ((foo >> ((len - iInd - 1) * 8)) & 0x000000FF);
		}
		return array;
	}

	/**
	 * ���ַ�ת��Ϊ����
	 * 
	 * @param src
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 */
	public static String toCHNString(String src) {
		if (src == null || src.length() <= 0) {
			return src;
		}
		try {
			return new String(src.getBytes("ISO-8859-1"), "GB2312");
		} catch (java.io.UnsupportedEncodingException ex) {
			LogUtil.getLogger().error(ex.getMessage(), ex);
			return null;
		}

	}

	/**
	 * ADD BY YQL2005-07-01 ���ַ�ת��Ϊ����ISO-8859-1�ַ�
	 * 
	 * @param src
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 */
	public static String GBKtoISOString(String src) {
		if (src == null || src.length() <= 0) {
			return src;
		}
		try {
			return new String(src.getBytes("GBK"), "ISO-8859-1");
		} catch (java.io.UnsupportedEncodingException ex) {
			LogUtil.getLogger().error(ex.getMessage(), ex);
			return null;
		}
	}

	/**
	 * ADD BY YQL2005-07-01 ���ַ�ת��Ϊ����ISO-8859-1�ַ�
	 * 
	 * @param src
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 */
	public static String GB2312toISOString(String src) {
		if (src == null || src.length() <= 0) {
			return src;
		}
		try {
			return new String(src.getBytes("GB2312"), "ISO-8859-1");
		} catch (java.io.UnsupportedEncodingException ex) {
			LogUtil.getLogger().error(ex.getMessage(), ex);
			return null;
		}
	}

	/**
	 * ���ַ�ת��Ϊ����GBK�ַ�
	 * 
	 * @param src
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 */
	public static String toGBKString(String src) {
		if (src == null || src.length() <= 0) {
			return src;
		}
		try {
			return new String(src.getBytes("ISO-8859-1"), "GBK");
		} catch (java.io.UnsupportedEncodingException ex) {
			LogUtil.getLogger().error(ex.getMessage(), ex);
			return null;
		}
	}

	/**
	 * ������ת��Ϊ������ add by wjz 1119
	 * 
	 * @param o
	 *            Դ����
	 * @param forceConvert
	 *            �ǲ���ǿ��ת��
	 * @return ��Ӧ�ĸ�����
	 */
	public static double toDouble(Object o, boolean forceConvert) {

		try {
			String s = o.toString();
			return Double.parseDouble(s);
		} catch (Exception ex) {

			if (forceConvert) {
				return 0;
			}
			throw new IllegalArgumentException("�ö���Ϊ�ջ���һ���Ϸ��ĸ�����");
		}

	}

	/**
	 * Description of the Method
	 * 
	 * @param src
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 */
	public static String toHexsFormat(byte[] src) {
		if (src == null) {
			return null;
		}
		int len = src.length;
		StringBuffer sb = new StringBuffer();

		String s = "";
		sb.append(",value=");

		for (int i = 0; i < len; i++) {
			s = java.lang.Integer.toHexString(src[i] & 0XFF);
			if (s.length() == 1) {
				sb.append("0");
			}
			sb.append(s);
			sb.append(",");
		}
		return sb.substring(0, sb.length() - 1);
	}

	/**
	 * ����ĸ��ֽڵ�����洢,���һ������
	 * <p>
	 * ����e0,08,00,00�������2272
	 * <p>
	 * wujinzhong 0610
	 * 
	 * @param bts
	 *            Description of Parameter
	 * @return The Int value
	 */
	public static int toInt(byte[] bts) {
		if (bts == null) {
			return Integer.MIN_VALUE;
		}
		int v = 0;
		int len = bts.length;
		for (int i = len - 1; i >= 0; i--) {
			v = v * 256 + ((bts[i] + 256) % 256);

		}
		return v;
	}
	
	
	/**
	 * ��ݰ˸��ֽڵ�����洢,���һ������
	 * @param bts
	 *            Description of Parameter
	 * @return The Long value
	 */
	public static long toLong(byte[] bts) {
		if (bts == null) {
			return Long.MIN_VALUE;
		}
		int v = 0;
		int len = bts.length;
		for (int i = len - 1; i >= 0; i--) {
			v = v * 256 + ((bts[i] + 256) % 256);

		}
		return v;
	}
	

	/**
	 * ������ת��Ϊ������
	 * 
	 * @param o
	 *            Դ����
	 * @return ��Ӧ��Intֵ,������,�򷵻�Integer.MIN_VALUE
	 */
	public static int toInt(Object o) {
		if (o == null) {
			throw new IllegalArgumentException("�ö���Ϊ��");
		}
		String s = o.toString();
		try {
			return Integer.parseInt(s);
		} catch (Exception ex) {

			LogUtil.getLogger().error(ex.getMessage(), ex);
			return Integer.MIN_VALUE;
		}
	}

	/**
	 * ������ת��Ϊ������
	 * 
	 * @param o
	 *            Դ����
	 * @param defaultInt
	 * @return ��Ӧ��Intֵ,������,�򷵻�defaultInt
	 */
	public static int toInt(Object o, int defaultInt) {
		try {
			if (o instanceof BigDecimal) {
				return ((BigDecimal) o).intValue();
			} else if (o instanceof String) {
				return new BigDecimal((String) o).setScale(0,
						BigDecimal.ROUND_HALF_UP).intValue();
			}
			return Integer.parseInt(o.toString());
		} catch (Exception ex) {
			return defaultInt;
		}
	}

	/**
	 * ������ת��Ϊ������
	 * 
	 * @param o
	 *            Դ����
	 * @return ��Ӧ��Longֵ,������,�򷵻�Long.MIN_VALUE
	 */
	public static long toLong(Object o) {
		if (o == null) {
			throw new IllegalArgumentException("�ö���Ϊ��");
		}
		String s = o.toString();
		try {
			return Long.parseLong(s);
		} catch (Exception ex) {

			LogUtil.getLogger().error(ex.getMessage(), ex);
			return Long.MAX_VALUE;
		}
	}

	/**
	 * ������ת��ΪLong����
	 * 
	 * @param o
	 *            Դ����
	 * @param defaultLong
	 * @return ��Ӧ��longֵ,������,�򷵻�defaultLong
	 */
	public static long toLong(Object o, long defaultLong) {
		try {
			if (o instanceof BigDecimal) {
				return ((BigDecimal) o).longValue();
			}
			return Long.parseLong(o.toString());
		} catch (Exception ex) {
			return defaultLong;
		}
	}

	
	/**
	 * �ж��ַ��Ƿ�Ϊ����
	 */
	static Pattern pattern = Pattern.compile("[0-9]*(\\.?)[0-9]*");
	public static boolean isDecimal(String str) {
		if(isEmptyStr(str)) return false;  
		return pattern.matcher(str).matches();
	}

	/**
	 * ������ת��ΪBigDecimal����
	 * 
	 * @param o
	 *            Դ����
	 * @return ��Ӧ������ֵ,������,�򷵻�Long.MIN_VALUE
	 */
	public static BigDecimal toBigDecimal(Object o) {
		if (o == null) {
			throw new IllegalArgumentException("�ö���Ϊ��");
		}
		String s = o.toString();
		try {
			return new BigDecimal(s);
		} catch (Exception ex) {

			LogUtil.getLogger().error(ex.getMessage(), ex);
			return new BigDecimal(Long.MAX_VALUE);
		}
	}

	/**
	 * ������ת��ΪBigDecimal����
	 * 
	 * @param o
	 *            Դ����
	 * @param defaultBigDecimal
	 * @return ��Ӧ��BigDecimalֵ,������,�򷵻�defaultBigDecimal
	 */
	public static BigDecimal toBigDecimal(Object o, BigDecimal defaultBigDecimal) {
		try {
			return new BigDecimal(o.toString());
		} catch (Exception ex) {
			return defaultBigDecimal;
		}
	}

	/**
	 * ��Vectorת��Ϊ�ַ��������ʽ add by yql 1129
	 * 
	 * @param vec
	 * @return
	 */
	public static String[] vecToStrArr(Vector vec) {
		if (vec == null) {
			return null;
		}
		int iLen = vec.size();
		String[] rtnArr = new String[iLen];
		for (int i = 0; i < iLen; i++) {
			rtnArr[i] = vec.elementAt(i).toString();
		}
		return rtnArr;
	}

	/**
	 * ���ض�����ַ���֣����Ϊ���򷵻�"" add by wjz 040902
	 * 
	 * @param obj
	 *            Object
	 * @return String
	 */
	public static String toNotNullString(Object obj) {
		if (obj == null)
			return "";
		return obj.toString();
	}

	public static String quoteStr(Object o) {
		return "'" + o + "'";
	}

	/**
	 * ��BCD�뻹ԭΪ�ַ�Ŀǰ��֧�ֺ���
	 * <p>
	 * add by wjz 040916
	 * 
	 * @param bt
	 *            byte[] BCD��ʽ������
	 * @return String
	 */
	public static String BCDToStr(byte[] bt) {
		String s = "";
		if (bt == null)
			return "";
		byte[] bt2 = new byte[bt.length * 2];
		byte b = 0;
		byte b1 = 0;
		byte b2 = 0;
		char c = 0;
		try {
			int len = bt2.length;
			int pos = 0;
			for (int i = 0; i < bt.length; i++) {
				b = bt[i];
				bt2[i * 2] = (byte) (bt[i] >> 4 & 0x0f); // ��λ
				bt2[i * 2 + 1] = (byte) (bt[i] & 0x0f); // ��λ

			}
			boolean flag = true; // һ���ַ�Ŀ�ʼ
			for (int i = 0; i < len; i++) {
				b = bt2[i];
				if (flag) {
					if (b <= 9) {
						s = s + (char) (b + 0x30);
					} else {
						flag = false;
					}
				} else { // ���ǿ�ʼ����ǰ��ϲ�
					if (b == 0x0f && (i == len - 1))
						continue;

					s = s + (char) ((bt2[i - 1] << 4) + b - 128);

					flag = true;

				}
			}
			return s;
		} catch (Exception ex) {
			LogUtil.getLogger().error(
					"BCDToStr ERROR:" + ex.getMessage(), ex);
			return null;
		}

	}

	/**
	 * ���ַ�ת��ΪBCD�룬Ŀǰ��֧�ֺ���
	 * <p>
	 * add by wjz 040916
	 * 
	 * @param s
	 *            String
	 * @return byte[] BCD��ʽ������
	 */
	public static byte[] strToBCD(String s) {
		byte[] bt = new byte[s.length()];
		// ��ʼ��
		for (int i = 0; i < bt.length; i++) {
			bt[i] = (byte) 0xff;
		}
		char[] chars = s.toCharArray();
		byte c = 0;
		try {
			int len = chars.length;
			int pos = 0;
			boolean flag = true; // true-high position;false-lower position
			for (int i = 0; i < len; i++) {
				c = (byte) (chars[i]);
				if (c >= '0' && c <= '9') { // ����
					if (!flag) { // ���ֽ�
						bt[pos] = (byte) (bt[pos] + (c - 0x30));
						flag = true;
						pos++;
					} else { // ���ֽ�
						bt[pos] = (byte) ((c - 0x30) << 4);
						flag = false;
					}
				} else { // �ַ�
					if (!flag) { // ���ֽ�
						bt[pos] = (byte) ((byte) (bt[pos]) + (byte) ((c + 128) >> 4));
						pos++;
						bt[pos] = (byte) (c << 4);
					} else { // ���ֽ�
						bt[pos] = (byte) (c + 128);
						pos++;
					}

				}

			}
			if (!flag) {
				bt[pos] = (byte) (bt[pos] + 0x0f);
				pos++;
			}
			byte[] ret = new byte[pos];
			System.arraycopy(bt, 0, ret, 0, pos);
			return ret;
		} catch (Exception ex) {
			LogUtil.getLogger().error(
					"strToBCD ERROR:" + ex.getMessage(), ex);
			return null;
		}

	}

	public static Integer parseInteger(Object o, Integer defaulti) {
		try {
			return new Integer(Integer.parseInt(o.toString()));
		} catch (Exception e) {
			return defaulti;
		}
	}

	public static Double parseDouble(Object o, Double defaulti) {
		try {
			return new Double(Double.parseDouble(o.toString()));
		} catch (Exception e) {
			return defaulti;
		}
	}

	public static Float parseFloat(Object o, Float defaulti) {
		try {
			return new Float(Float.parseFloat(o.toString()));
		} catch (Exception e) {
			return defaulti;
		}
	}

	public static Long parseLong(Object o, Long defaulti) {
		try {
			return new Long(Long.parseLong(o.toString()));
		} catch (Exception e) {
			return defaulti;
		}
	}

	public static BigDecimal parseBigDecimal(Object o, BigDecimal defaulti) {
		try {
			return new BigDecimal(o.toString());
		} catch (Exception e) {
			return defaulti;
		}
	}

	public static Date parseDate(Object o, Date defaulti) {
		if (o instanceof Date) {
			return (Date) o;
		}
		try {
			return DateUtil.strToDate(o.toString());
		} catch (Exception e) {
			return defaulti;
		}
	}

	/**
	 * "1001,1002"-->"'1001','1002'"
	 * 
	 * @param s
	 * @return
	 */
	public static String getInSQLStr(String s) {
		if (s == null || s.length() <= 0)
			return "";
		String[] ss = s.split(",");
		return getInSQLStr(ss);
	}

	public static String getInSQLStr(Object[] ss) {
		if (ss == null)
			return "";
		String retstr = "";
		for (int i = 0; i < ss.length; i++) {
			if (i != 0)
				retstr += ",";
			retstr += "'" + ss[i] + "'";
		}
		return retstr;
	}

	public synchronized static String getSysId() {
		try {
			Thread.sleep(36);
		} catch (Exception e) {

		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
		Date d = getSysDateTime();
		return sf.format(d);
	}

	/**
	 * ȡϵͳʱ�䣬��ʱ���֡���
	 * 
	 * @return
	 */
	public static Date getSysDateTime() {
		return new Date();
	}
	
	
	/**
	 * �����㣨��γ�ȣ�֮��ľ��� ��λ����
	 */
	public static double getDistatce(double lat1, double lat2, double lon1, double lon2) {
		double latA = rad(lat1), lngA = rad(lon1);
		double latB = rad(lat2), lngB = rad(lon2);
		double d = 2 * Math.asin(Math.sqrt(Math.pow(
				Math.sin((latA - latB) / 2), 2)
				+ Math.cos(latA)
				* Math.cos(latB)
				* Math.pow(Math.sin((lngA - lngB) / 2), 2))) * 6378137;
		return new BigDecimal(d).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	
	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}
	
	public static BigDecimal decimalDivide(BigDecimal cs, BigDecimal bcs, int scale) {
		return cs.divide(bcs ,scale, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * ���byte���飻�����Զ�����
	 * @param source
	 * @param length
	 * @param sign
	 * @return
	 */
	public static byte[] fillData(byte[] source,int length,byte sign){
		byte[] result = new byte[length];
		for (int i = 0; i < length; i++) {
			if(i<source.length){
				result[i]=source[i];
			}else{
				result[i]=sign;
			}
		}
		
		return result;
	}

	/**
	 * �жϵ�ǰ�����б��Ƿ��Ϊ��
	 * @param objects
	 * @return
	 */
	public static boolean isNotEmpty(Object... objects) {
		for (Object obj : objects) {
			if (obj == null) {
				return false;
			}
			if (obj instanceof String) {
				if ("null".equals(((String) obj).trim())||"".equals(((String) obj).trim())) {
					return false;
				}
			}
		}
		
		return true;
	}
}
