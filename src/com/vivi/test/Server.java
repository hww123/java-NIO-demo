package com.vivi.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server {
	
	private static Selector selector;
	private static ByteBuffer send = ByteBuffer.allocate(1024);
	private static ByteBuffer read = ByteBuffer.allocate(1024);
	public static void main(String[] args) {
		try {
			ServerSocketChannel ssc = ServerSocketChannel.open();
			ssc.socket().bind(new InetSocketAddress(9999));
			ssc.configureBlocking(false);
			
			selector = Selector.open();
			ssc.register(selector, SelectionKey.OP_ACCEPT);
			
			while (true) {
				selector.select();
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> iterator = keys.iterator();
				while (iterator.hasNext()) {
					SelectionKey key = iterator.next();
					if (!key.isValid()) continue;
					if (key.isAcceptable()) {
						System.out.println("server accept");
						accept(key);
					}
					if (key.isReadable()) {
						read(key);
						System.out.println("server read");
					}
					if (key.isWritable()) {
						write(key);
						System.out.println("server wrtie");
					}
					iterator.remove();
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private static void write(SelectionKey key) throws IOException {
		SocketChannel clientChannel = (SocketChannel) key.channel();
		send.clear();
		String writemessage = "服务器写东西给客户端: some msg";
		send.put(writemessage.getBytes());
		send.flip();
		clientChannel.write(send);
		clientChannel.register(selector, SelectionKey.OP_READ);
		
	}

	private static void read(SelectionKey key) throws IOException {
		SocketChannel clientChannel = (SocketChannel) key.channel();
		read.clear();
		clientChannel.read(read);
		System.out.println("服务器读客户端:" + new String(read.array()));
		clientChannel.register(selector, SelectionKey.OP_WRITE);
	}

	private static void accept(SelectionKey key) throws IOException {
		ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
		SocketChannel sc = ssc.accept();
		sc.configureBlocking(false);
		sc.register(selector, SelectionKey.OP_READ);
		System.out.println("一个客户端连接成功: " + sc.getRemoteAddress());
		
	}

}
