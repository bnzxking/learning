package com.bnzxking.learning.nio.java;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.concurrent.CountDownLatch;

public class FileLockTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("aaa.txt", "rw");

        FileChannel channel = randomAccessFile.getChannel();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        Thread thread = new Thread(() -> {
            RandomAccessFile randomAccessFile2 = null;
            try {
                randomAccessFile2 = new RandomAccessFile("aaa.txt", "rw");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            FileChannel channel2 = randomAccessFile2.getChannel();
            try {
                countDownLatch.await();
                channel2.lock(9,20,false);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        channel.lock(0,10,false);
        countDownLatch.countDown();
        thread.join();
    }

}
