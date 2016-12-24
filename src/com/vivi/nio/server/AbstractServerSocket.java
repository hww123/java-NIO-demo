package com.vivi.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public abstract class AbstractServerSocket implements ServerSocket {
	
	private ServerSocketChannel ssc;
	
	private Selector selector;
	
	private ByteBuffer send = ByteBuffer.allocate(1024);
	
	private ByteBuffer read = ByteBuffer.allocate(1024);
	
	private int port = 80;
	
	private String msg;
	
	
	
	public AbstractServerSocket(int port) {
		this.port = port;
		open();
	}
	
	@Override
	public void open() {
		try {
			ssc = ServerSocketChannel.open();
			ssc.socket().bind(new InetSocketAddress(port));
			ssc.configureBlocking(false);
			selector = Selector.open();
			ssc.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("server is ready...");
			while (true) {
				selector.select();
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> iterator = keys.iterator();
				while (iterator.hasNext()) {
					SelectionKey key = iterator.next();
					if (!key.isValid()) continue;
					if (key.isAcceptable()) {
						System.out.println("进入 acceptable");
						ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
						SocketChannel sc = ssc.accept();
						sc.configureBlocking(false);
						sc.register(selector, SelectionKey.OP_READ);
					}
					if (key.isWritable()) {
						System.out.println("server write");
						SocketChannel sc = (SocketChannel) key.channel();
						send.clear();
						write(send);
						send.flip();
						sc.write(send);
						sc.register(selector, SelectionKey.OP_READ);
					}
					if (key.isReadable()) {
						System.out.println("server read");
						SocketChannel sc = (SocketChannel) key.channel();
						read.clear();
						sc.read(read);
						read(read);
						msg = new String(read.array());
						sc.register(selector, SelectionKey.OP_WRITE);
					}
					iterator.remove();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public abstract void read(ByteBuffer read);

	public abstract void write(ByteBuffer send);
	
	public String getMsg() {
		return msg;
	}

}
