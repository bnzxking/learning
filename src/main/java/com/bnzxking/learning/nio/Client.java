package com.bnzxking.learning.nio;

import java.io.IOException;

public class Client {
    private static String DEFAULT_HOST = "127.0.0.1";
    private static int DEFAULT_PORT = 1234;
    private static ClientHandle clientHandle;
    public static void start() throws IOException {
        start(DEFAULT_HOST,DEFAULT_PORT);
    }
    public static synchronized void start(String ip,int port) throws IOException {
        if(clientHandle!=null)
            clientHandle.stop();
        clientHandle = new ClientHandle(ip,port);
        clientHandle.doConnect();
//        new Thread(clientHandle,"Server").start();
    }
    //向服务器发送消息
    public static boolean sendMsg(String msg) throws Exception{
        if(msg.equals("q")) return false;
        clientHandle.sendMsg(msg);
        return true;
    }

    public static boolean isFinishConnect() throws Exception{
        return clientHandle.isFinishConnect();
    }

    public static void main(String[] args) throws IOException {
        start();
    }
}
