package com.bnzxking.learning.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SimpleClient {

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel;
        socketChannel = SocketChannel.open();

        socketChannel.configureBlocking(false);
        if (socketChannel.connect(new InetSocketAddress("127.0.0.1", 1234))) {
            byte[] bytes = new String("hello world").getBytes();
            //根据数组容量创建ByteBuffer
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            //将字节数组复制到缓冲区
            writeBuffer.put(bytes);
            //flip操作
            writeBuffer.flip();
            //发送缓冲区的字节数组
            socketChannel.write(writeBuffer);
        }

    }
}
