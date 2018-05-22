package com.bnzxking.learning.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class MultiReactor {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(1234));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        int coreNum = Runtime.getRuntime().availableProcessors();
        MultiProcessor[] processors = new MultiProcessor[coreNum];
        for (int i = 0; i < processors.length; i++) {
            processors[i] = new MultiProcessor();
        }
        int index = 0;
        while (selector.select() > 0) {
            Set<SelectionKey> keys = selector.selectedKeys();
            for (SelectionKey key : keys) {
                keys.remove(key);
                if (key.isAcceptable()) {
                    ServerSocketChannel acceptServerSocketChannel = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = acceptServerSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    MultiProcessor processor = processors[(int) ((index++) % coreNum)];
                    processor.addChannel(socketChannel);
                    processor.wakeup();
                }
            }
        }
    }
}
