package com.vivi.nio.server.support;

import java.nio.ByteBuffer;

import com.vivi.nio.server.AbstractServerSocket;

public class DefaultServerSocket extends AbstractServerSocket {

	public DefaultServerSocket(int port) {
		super(port);
	}

	@Override
	public void read(ByteBuffer read) {
		System.out.println(new String(read.array()));
	}

	@Override
	public void write(ByteBuffer send) {
		send.put(getMsg().getBytes());
	}

}
