package com.bnzxking.learning.nio.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class CloseTest {


    //书中说明，当一个线程被中断之后，线程中调用的channel都会被关闭
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {

        FileOutputStream fileOutputStream = new FileOutputStream(new File("aaa.txt"));
        FileChannel channel = fileOutputStream.getChannel();

        Thread thread = new Thread(() -> {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String ss = "aaaaaa";
            byte[] bytes = ss.getBytes();
            ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
            byteBuffer.put(bytes);
            byteBuffer.flip();
            try {
                channel.write(byteBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        thread.interrupt();
        thread.join();

        System.out.println(channel.isOpen());
    }
}
