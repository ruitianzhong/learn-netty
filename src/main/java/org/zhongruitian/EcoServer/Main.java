package org.zhongruitian.EcoServer;


import top.zhongruitian.Redis;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
       Redis redis =new Redis();
       redis.flushAll();
       redis.set("hello","world");
        System.out.println(redis.get("hello"));
        redis.quit();
    }
}