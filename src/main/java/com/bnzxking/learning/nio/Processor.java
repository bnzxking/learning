package com.bnzxking.learning.nio;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Processor {
    private static final ExecutorService service = Executors.newFixedThreadPool(16);
    public void process(final SelectionKey selectionKey) {
        service.submit(new Callable() {
            @Override
            public Object call() throws Exception {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                int count = socketChannel.read(buffer);
                buffer.flip();
                byte[] readByteArray =new byte[buffer.remaining()];
                buffer.get(readByteArray);
                System.out.println("threadId"+Thread.currentThread().getId()+"  客户端信息"+new String(readByteArray,Charset.defaultCharset()));
                if (count < 0) {
                    socketChannel.close();
                    selectionKey.cancel();
                    return null;
                } else if(count == 0) {
                    return null;
                }
                return null;
            }
        });
    }
}
