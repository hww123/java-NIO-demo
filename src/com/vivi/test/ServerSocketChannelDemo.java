package com.vivi.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ServerSocketChannelDemo {

	public static void main(String[] args) {
		ServerSocketChannel serverSocketChannel;
		try {
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.socket().bind(new InetSocketAddress(9999));
			serverSocketChannel.configureBlocking(false);
			ByteBuffer buf = ByteBuffer.allocate(48);
			int n = 0;
			System.out.println("服务器已开启...");
			while (true) {
				SocketChannel socketChannel = serverSocketChannel.accept();
				if (socketChannel != null) {
					System.out.println("成功获取客户端链接...");
					n = socketChannel.read(buf);
					while (n != -1) {
						buf.flip();
						while(buf.hasRemaining()){
							System.out.print((char) buf.get());
						}
						buf.clear();
						n = socketChannel.read(buf);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
