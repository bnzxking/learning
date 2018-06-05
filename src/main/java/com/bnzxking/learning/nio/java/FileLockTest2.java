package com.bnzxking.learning.nio.java;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.concurrent.CountDownLatch;

public class FileLockTest2 {

    public static void main(String[] args) throws IOException, InterruptedException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("aaa.txt", "rw");

        FileChannel channel = randomAccessFile.getChannel();

        channel.lock(0,10,false);
        channel.lock(9,20,false);
    }

}
