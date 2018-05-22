package com.bnzxking.learning.nio.java;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteBufferTest {

    public static void main(String[] args){
        ByteBuffer byteBuffer = ByteBuffer.allocate(2);
        byteBuffer.put(new byte[]{127,127});

//        System.out.println(byteToBit(new Byte("127")));

        byteBuffer.flip();

        ByteBuffer duplicate = byteBuffer.duplicate();

        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        System.out.println((int)byteBuffer.getChar());
        System.out.println(byteBuffer.order().toString());
        System.out.println(byteBuffer.array());

        System.out.println((int)duplicate.getChar());
        System.out.println(duplicate.order().toString());
        System.out.println(duplicate.array());

    }


    public static String byteToBit(byte b) {
        return ""
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    }
}
