package com.bnzxking.learning.nio.java;

import java.nio.CharBuffer;

public class BufferWrap {

    public static void main(String[] args){
        char [] myArray = new char [100];
        CharBuffer charbuffer = CharBuffer.wrap (myArray);

        System.out.println(charbuffer.remaining());

        CharBuffer charbuffer2 = CharBuffer.wrap (myArray,12,42);

        System.out.println("charbuffer.arrayOffset()"+charbuffer.arrayOffset());

        System.out.println(charbuffer2.remaining());
        charbuffer2.clear();
        System.out.println(charbuffer2.remaining());

        myArray[0]= 'a';

        System.out.println("out:"+charbuffer.get());
        System.out.println("out:"+charbuffer.get());

        System.out.println(charbuffer.array()==myArray);

        System.out.println("charbuffer.arrayOffset()"+charbuffer.arrayOffset());

        


    }
}
