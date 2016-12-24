package com.vivi.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class FileChannelDemo2 {

	public static void main(String[] args) {
		RandomAccessFile fromFile;
		try {
			fromFile = new RandomAccessFile("/Users/huangwenwei/vivi_project/file_test_demo/result.txt", "rw");
			FileChannel fromChannel = fromFile.getChannel();
			RandomAccessFile toFile = new RandomAccessFile("/Users/huangwenwei/vivi_project/file_test_demo/result2.txt", "rw");
			FileChannel toChannel = toFile.getChannel();
			long position = 0;
			long count = fromChannel.size();
			fromChannel.transferTo(position, count, toChannel);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
