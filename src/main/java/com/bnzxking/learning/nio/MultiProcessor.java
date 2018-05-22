package com.bnzxking.learning.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiProcessor {
    private static final ExecutorService service =
            Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors());
    private Selector selector;

    public MultiProcessor() throws IOException {
        this.selector = SelectorProvider.provider().openSelector();
        start();
    }

    public void addChannel(SocketChannel socketChannel) throws ClosedChannelException {
        socketChannel.register(this.selector, SelectionKey.OP_READ);
    }

    public void wakeup() {
        this.selector.wakeup();
    }

    public void start() {
        service.submit(
                new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        while (true) {
                            if (selector.select(500) <= 0) {
                                continue;
                            }
                            Set<SelectionKey> keys = selector.selectedKeys();
                            Iterator<SelectionKey> iterator = keys.iterator();
                            while (iterator.hasNext()) {
                                SelectionKey key = iterator.next();
                                iterator.remove();
                                if (key.isReadable()) {
                                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                                    SocketChannel socketChannel = (SocketChannel) key.channel();
                                    int count = socketChannel.read(buffer);
                                    buffer.flip();
                                    byte[] readByteArray =new byte[buffer.remaining()];
                                    buffer.get(readByteArray);
                                    System.out.println("threadId"+Thread.currentThread().getId()+"  客户端信息"+new String(readByteArray,Charset.defaultCharset()));
                                    if (count < 0) {
                                        socketChannel.close();
                                        key.cancel();
                                        continue;
                                    } else if (count == 0) {
                                        continue;
                                    } else {
                                    }
                                }
                            }
                        }
                    }
                }
        );
    }
}
