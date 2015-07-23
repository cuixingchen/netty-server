package com.cui.netty_server.msg;

import java.util.Arrays;

import com.cui.netty_server.util.Converter;
import com.cui.netty_server.util.DataTypeUtil;
import com.cui.netty_server.util.LogUtil;


/**
 * 
 * ClassName: LoginMsg 
 * Reason: 登录消息 
 * date: 2014年10月15日 下午2:41:47 
 *
 * @author cuipengfei
 */
public class UP_CONNECT_REQ extends AbsMsg {

	private long userid;
	private String connecttime;
	private String mac;

	

	@Override
	public String toString() {
		return "UP_CONNECT_REQ [userid=" + userid + ", connecttime="
				+ connecttime + ", mac=" + mac + ", head=" + head + "]";
	}


	@Override
	protected int getMsgID() {
		return MessageID.UP_CONNECT_REQ;
	}


	@Override
	protected byte[] bodytoBytes() {
		byte[] data = new byte[4+7+32];
		try {
			int offset = 0;
			System.arraycopy(Converter.unSigned32LongToBigBytes(userid), 0, data, offset, 4);
			offset+=4;
			System.arraycopy(DataTypeUtil.strToBCD(connecttime), 0, data, offset, 7);
			offset+=7;
			System.arraycopy(mac.getBytes(), 0, data, offset, mac.getBytes().length);
		} catch (Exception e) {
			LogUtil.getLogger().error("登录消息toBytes转换异常",e);
			e.printStackTrace();
		}
		return data;
	}

	@Override
	protected boolean bodyfromBytes(byte[] b) {
		boolean resultState = false;
		int offset = 0;
		try {
			userid = Converter.bigBytes2Unsigned32Long(b, offset);
			offset+=4;
			connecttime = DataTypeUtil.BCDToStr(Arrays.copyOfRange(b, offset, offset+7));
			offset+=7;
//			String ss=HexStringUtil.Bytes2HexString(Arrays.copyOfRange(b, offset, offset+32));
			mac = new String(Arrays.copyOfRange(b, offset, offset+32));
			offset+=32;
			resultState = true;
		} catch (Exception e) {
			LogUtil.getLogger().error("登录消息fromBytes转换异常",e);
			e.printStackTrace();
		}
		return resultState;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getConnecttime() {
		return connecttime;
	}

	public void setConnecttime(String connecttime) {
		this.connecttime = connecttime;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

}
