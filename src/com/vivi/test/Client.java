package com.vivi.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Client {
	
	private static ByteBuffer write = ByteBuffer.allocate(1024);
	private static ByteBuffer read = ByteBuffer.allocate(1024);
	
	public static void main(String[] args) {
		try {
			SocketChannel sc = SocketChannel.open();
			sc.configureBlocking(false);
			sc.connect(new InetSocketAddress("192.168.1.2", 9999));
			
			Selector selector = Selector.open();
			sc.register(selector, SelectionKey.OP_CONNECT);
			
			while (true) {
				selector.select();
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> iterator = keys.iterator();
				while (iterator.hasNext()) {
					SelectionKey key = iterator.next();
					if (key.isConnectable()) {
						sc.finishConnect();
						sc.register(selector, SelectionKey.OP_WRITE);
						System.out.println("server connected...");
						break;
					}
					if (key.isReadable()) {
						SocketChannel client = (SocketChannel) key.channel();
						read.clear();
						int num = client.read(read);
						System.out.println("客户端读服务器的东西： " + new String(read.array(), 0, num));
						client.register(selector, SelectionKey.OP_WRITE);
					}
					if (key.isWritable()) {
						String msg = "客户端写东西到服务器： i want to say something";
						write.clear();
						write.put(msg.getBytes());
						write.flip();
						SocketChannel client = (SocketChannel) key.channel();
						client.write(write);
						client.register(selector, SelectionKey.OP_READ);
						System.out.println(msg);
					}
				}
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
