package com.vivi.nio.demo;

import com.vivi.nio.server.ServerSocket;
import com.vivi.nio.server.support.DefaultServerSocket;

public class ServerDemo {
	
	public static void main(String[] args) {
		new DefaultServerSocket(9999);
	}

}
