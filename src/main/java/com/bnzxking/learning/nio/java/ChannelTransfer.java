package com.bnzxking.learning.nio.java;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.channels.Channels;
import java.io.FileInputStream;

public class ChannelTransfer {

    public static void main (String [] argv)throws Exception{
        argv = new String[]{"D://aaaaa.txt"};
        catFiles (Channels.newChannel (System.out), argv);
    }
    // Concatenate the content of each of the named files to
// the given channel. A very dumb version of 'cat'.
    private static void catFiles (WritableByteChannel target,String [] files)throws Exception{
        for (int i = 0; i < files.length; i++) {
            FileInputStream fis = new FileInputStream (files [i]);
            FileChannel channel = fis.getChannel( );
            channel.transferTo (0, channel.size( ), target);
            channel.close( );
            fis.close( );
        }
    }
}
