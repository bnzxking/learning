package com.bnzxking.learning.nio;

import java.io.IOException;
import java.util.Scanner;

public class Test {
    //测试主方法
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Client.start();
                        while (!Client.isFinishConnect()) {
                            Thread.sleep(3000L);
                        }
                        for (int i = 0; i < 10; i++) {
                            Client.sendMsg("thread id:" + Thread.currentThread().getId() + " str:" + i+"\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            thread.setName(i + "");
            thread.start();

            Thread.sleep(1000000L);
        }
    }
}
