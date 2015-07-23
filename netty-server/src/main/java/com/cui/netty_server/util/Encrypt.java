package com.cui.netty_server.util;

import java.util.Random;

/**
 * 消息体加密
 * 
 * @author cuipengfei
 * 
 */
public class Encrypt {
	// Const unsigned UINT32 M1 =A;
	// Const unsigned UINT32 IA1 =B;
	// Const unsigned UINT32 IC1 =C;
	// Void encrypt(UINT32 key, unsigned char* buffer, UINT32 size )
	// {
	// UINT32 idx = 0;
	// if( 0 = = key )
	// {
	// key = 1;
	// }
	// UINT32 mkey = M1;
	// if (0 = = mkey )
	// {
	// mkey = 1;
	// }
	// while( idx < size )
	// {
	// key = IA1 * ( key % mkey ) + IC1;
	// buffer[idx++] ^= (unsigned char)((key>>20)&0xFF);
	// }
	// }
	// public static long M1 = 0xA;
	// public static long IA1 = 0xB;
	// public static long IC1 = 0xC;

	/**
	 * 加密解密
	 * 
	 * @param key
	 * @param buffer
	 * @param size
	 */
	public static byte[] encryptUtil(long key, byte[] buffer, long size,
			long M1, long IA1, long IC1) {
		int idx = 0;
		if (0 == key) {
			key = 1;
		}
		long mkey = M1;
		if (0 == mkey) {
			mkey = 1;
		}
		while (idx < size) {
			key = IA1 * (key % mkey) + IC1;
			buffer[idx++] ^= ((key >> 20) & 0xFF);
		}
		return buffer;
	}

	private static Random ran;

	/**
	 * 32位伪随机数产生器
	 * 
	 * @return
	 */
	public static long getRandom32long() {
		if (ran == null) {
			ran = new Random();
		}
		return ran.nextLong() & 0xFFFFFFFFL;

	}

}
