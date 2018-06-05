package com.bnzxking.learning.nio.java;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class FileLockTest3 {

    public static void main(String[] args) throws IOException, InterruptedException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("aaa.txt", "rw");

        FileChannel channel = randomAccessFile.getChannel();

        System.out.println("lock begin");

        channel.lock(1,11,false);

        System.out.println("lock end");

        Thread.sleep(100000L);
    }

}
