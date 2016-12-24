package com.vivi.nio.client.support;

import java.nio.ByteBuffer;

import com.vivi.nio.client.AbstractClientSocket;

public class DefaultClientSocket extends AbstractClientSocket {

	public DefaultClientSocket(String addr, int port) {
		super(addr, port);
	}

	@Override
	public void write(ByteBuffer byteBuffer) {
		String msg = "test client msg";
		byteBuffer.put(msg.getBytes());
		
	}

	@Override
	public void read(byte[] bytes) {
		System.out.println(new String(bytes));
	}

}
