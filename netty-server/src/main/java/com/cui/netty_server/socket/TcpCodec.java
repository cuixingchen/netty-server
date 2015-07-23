package com.hdsx.taxi.upa.upaserver.socket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.nio.ByteBuffer;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.upa.upamsg.AbsMsg;
import com.hdsx.taxi.upa.upamsg.MsgFactory;
import com.hdsx.taxi.upa.upamsg.util.Converter;
import com.hdsx.taxi.upa.upamsg.util.HexStringUtil;

/**
 * 编解码
 * 
 * @author cuipengfei
 *
 */
public class TcpCodec extends ByteToMessageCodec<AbsMsg> {

	private static final byte HeadFlag = 0x5b;
	private static final byte EndFlag = 0x5d;
	private static final int Head_Length = 4 + 4 + 2 + 4 + 3 + 1 + 4;
	private static final Logger logger = LoggerFactory
			.getLogger(TcpCodec.class);

	ByteBuffer bf = ByteBuffer.allocate(10*1024*1024);
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer,
			List<Object> out) throws Exception {
		try {
			while (buffer.readableBytes() > 0) {
				byte b = buffer.readByte();
				bf.put(b);
				if(b==EndFlag){
					byte[] bytes = new byte[bf.position()-2];
					byte[] msgbytes = new byte[bf.position()];
					if(bf.get(0)==HeadFlag){
						bf.position(1);
						bf.get(bytes);
						bf.position(0);
						bf.get(msgbytes);
						logger.debug("接受到原始数据：" + HexStringUtil.Bytes2HexString(msgbytes));
						out.add(bytes);
					}
					bf.clear();
				}
			}
		} catch (Exception e) {

			logger.error("解码异常:"+e.toString());

		}

	}
	
	
	@Override
	protected void encode(ChannelHandlerContext ctx, AbsMsg msg, ByteBuf out)
			throws Exception {
		byte[] bt = msg.toBytes();
		out.writeBytes(bt);
		logger.debug("发送原始数据："+HexStringUtil.Bytes2HexString(bt));
		
	}

}
