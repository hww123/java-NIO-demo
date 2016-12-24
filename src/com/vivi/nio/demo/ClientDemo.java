package com.vivi.nio.demo;

import com.vivi.nio.client.support.DefaultClientSocket;

public class ClientDemo {
	
	
	public static void main(String[] args) {
		new DefaultClientSocket("192.168.1.2", 9999);
	}

}
