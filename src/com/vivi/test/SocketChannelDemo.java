package com.vivi.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketChannelDemo {

	public static void main(String[] args) {
		SocketChannel socketChannel;
		try {
			socketChannel = SocketChannel.open();
			socketChannel.connect(new InetSocketAddress("192.168.1.2", 9999));
			socketChannel.configureBlocking(false);
			String newData = "New String to write to file..." + System.currentTimeMillis();
			ByteBuffer buf = ByteBuffer.allocate(48);
			buf.clear();
			buf.put(newData.getBytes());
			buf.flip();
			while (buf.hasRemaining()) {
				socketChannel.write(buf);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
