package com.vivi.nio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import com.vivi.nio.client.ClientSocket;

/**
 * @author huangwenwei
 *
 */
public abstract class AbstractClientSocket implements ClientSocket {
	
	private SocketChannel socketChannel;
	
	private Selector selector;
	
	private ByteBuffer read = ByteBuffer.allocate(1024);
	
	private ByteBuffer write = ByteBuffer.allocate(1024);
	
	private String addr = "127.0.0.1";
	
	private int port = 80;
	
	
	public AbstractClientSocket(String addr, int port) {
		this.addr = addr;
		this.port = port;
		open();
	}
	

	@Override
	public void open() {
		try {
			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
			socketChannel.connect(new InetSocketAddress(addr, port));
			selector = Selector.open();
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
			while (true) {
				selector.select();
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> iterator = keys.iterator();
				while (iterator.hasNext()) {
					SelectionKey key = iterator.next();
					if (!key.isValid()) continue;
					if (key.isConnectable()) {
						System.out.println("client connected...");
						SocketChannel sc = (SocketChannel) key.channel();
						sc.finishConnect();
						sc.register(selector, SelectionKey.OP_WRITE);
						break;
					}
					if (key.isReadable()) {
						System.out.println("client read....");
						SocketChannel sc = (SocketChannel) key.channel();
						read.clear();
						sc.read(read);
						read(read.array());
						sc.register(selector, SelectionKey.OP_WRITE);
					}
					if (key.isWritable()) {
						System.out.println("client write...");
						SocketChannel sc = (SocketChannel) key.channel();
						write.clear();
						write(write);
						write.flip();
						sc.write(write);
						sc.register(selector, SelectionKey.OP_READ);
					}
				}
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public abstract void write(ByteBuffer byteBuffer);

	public abstract void read(byte[] array);



}
