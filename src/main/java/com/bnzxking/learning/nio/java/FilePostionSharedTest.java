package com.bnzxking.learning.nio.java;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.concurrent.CountDownLatch;

public class FilePostionSharedTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("aaa.txt", "r");
        FileChannel channel = randomAccessFile.getChannel();


        CountDownLatch countDownLatch = new CountDownLatch(1);
        Thread r = new Thread(() -> {
            try {
                FileChannel channel2 = randomAccessFile.getChannel();
                countDownLatch.await();
                System.out.println(channel2.position());
                channel2.position(1000);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        r.start();
        channel.position(2000);
        countDownLatch.countDown();
        r.join();
        System.out.println(channel.position());
    }
}
