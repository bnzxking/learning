package com.bnzxking.learning.nio.java;

import java.nio.CharBuffer;

public class BufferCopy {

    public static void main(String[] args){

        CharBuffer buffer = CharBuffer.allocate (8);
        buffer.position (3).limit (6).mark( ).position (5);
        CharBuffer dupeBuffer = buffer.duplicate( );
        buffer.clear( );

        System.out.println(buffer.remaining());
        System.out.println(dupeBuffer.remaining());

        CharBuffer buffer2 = CharBuffer.allocate (8);
        buffer2.position (3).limit (5);
        CharBuffer sliceBuffer2 = buffer2.slice();
        buffer2.clear( );

        System.out.println(buffer2.remaining());
        System.out.println(sliceBuffer2.remaining());

        System.out.println(buffer2.arrayOffset());
        System.out.println(sliceBuffer2.arrayOffset());

        buffer2.array()[3]='x';
        buffer2.array()[0]='b';
        System.out.println(sliceBuffer2.get());

    }
}
