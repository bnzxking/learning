package com.bnzxking.learning.nio.java;

import java.nio.CharBuffer;

public class BufferBatchOper {
    public static void main(String[] args){
        char[] out = {'1','2','3','4','5','6','7','8','9','a'};

        CharBuffer charBuffer = CharBuffer.allocate(11);
        charBuffer.get(out);

        System.out.println(charBuffer.remaining());
        System.out.println(charBuffer.flip());
        System.out.println(charBuffer.remaining());
     }
}
