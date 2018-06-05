package com.bnzxking.learning.nio.java;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.CountDownLatch;

public class TestMappedByteBuffer {

    public static void main(String[] args) throws IOException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Thread rw = new Thread(() -> {
            try {
                RandomAccessFile randomAccessFile2  = new RandomAccessFile("D:\\aaaaa.txt", "rw");
                FileChannel channel2 = randomAccessFile2.getChannel();
                MappedByteBuffer mappedByteBuffer2 = channel2.map(FileChannel.MapMode.READ_WRITE, 0L, channel2.size());
                dumpBuffer("READ_WRITE",mappedByteBuffer2);
                countDownLatch.await();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        rw.start();

        RandomAccessFile randomAccessFile = new RandomAccessFile("D:\\aaaaa.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0L, channel.size());
        mappedByteBuffer.put("hhhhhhh".getBytes());
        mappedByteBuffer.force();
        channel.force(false);
        countDownLatch.countDown();
        rw.join();
    }

    public static void dumpBuffer(String prefix, ByteBuffer buffer) throws Exception {
        System.out.print(prefix + ": '");
        int nulls = 0;
        int limit = buffer.limit();
        for (int i = 0; i < limit; i++) {
            char c = (char) buffer.get(i);
            if (c == '\u0000') {
                nulls++;
                continue;
            }
            if (nulls != 0) {
                System.out.print("|[" + nulls
                        + " nulls]|");
                nulls = 0;
            }
            System.out.print(c);
        }
        System.out.println("'");
    }

}
