package com.cui.netty_server.msg;

import com.cui.netty_server.util.Converter;
import com.cui.netty_server.util.DataTypeUtil;
import com.cui.netty_server.util.LogUtil;


/**
 * 
 * ClassName: AnswerMsg 
 * Reason: 通用应答
 * date: 2014年10月15日 下午3:11:19 
 *
 * @author sid
 */
public class UP_CONNECT_RSP extends AbsMsg {
	private byte result;
	private long verifycode;


	@Override
	protected int getMsgID() {
		//TODO 应答消息的id
		return MessageID.UP_CONNECT_RSP;
	}

	@Override
	protected byte[] bodytoBytes() {
		byte[] data = new byte[5];
		try {
			int offset = 0;
			data[0] = result;
			offset+=1;
			System.arraycopy(DataTypeUtil.toByteArray32Long(verifycode), 0, data, offset, 4);
			offset+=4;
		} catch (Exception e) {
			LogUtil.getLogger().error("登录应答消息toBytes转换异常",e);
			e.printStackTrace();
		}
		return data;
	}

	@Override
	protected boolean bodyfromBytes(byte[] b) {
		boolean resultState = false;
		int offset = 0;
		try {
			result = b[0];
			offset+=1;
			verifycode = Converter.bigBytes2Unsigned32Long(b, offset);
			offset+=4;
			resultState = true;
		} catch (Exception e) {
			LogUtil.getLogger().error("登录应答消息fromBytes转换异常",e);
			e.printStackTrace();
		}
		return resultState;
	}
	@Override
	public String toString() {
		return "UP_CONNECT_RSP [result=" + result + ", verifycode="
				+ verifycode + ", head=" + head + "]";
	}

	public byte getResult() {
		return result;
	}

	public void setResult(byte result) {
		this.result = result;
	}

	public long getVerifycode() {
		return verifycode;
	}

	public void setVerifycode(long verifycode) {
		this.verifycode = verifycode;
	}
	
}
