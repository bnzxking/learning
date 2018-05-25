package com.bnzxking.learning.nio.java;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class FilePosition {

    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("aaa.txt", "r");
// Set the file position
        randomAccessFile.seek(1000);
// Create a channel from the file
        FileChannel fileChannel = randomAccessFile.getChannel();
// This will print "1000"
        System.out.println("file pos: " + fileChannel.position());
// Change the position using the RandomAccessFile object
        randomAccessFile.seek(500);
// This will print "500"
        System.out.println("file pos: " + fileChannel.position());
// Change the position using the FileChannel object
        fileChannel.position(200);
// This will print "200"
        System.out.println("file pos: " + randomAccessFile.getFilePointer());
    }
}
