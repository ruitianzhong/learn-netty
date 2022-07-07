/*
 * Copyright 2022 ruitianzhong
 */
package top.zhongruitian.ServerWithNetty.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author ruitianzhong
 * @email zhongruitian2003@qq.com
 * @date 2022/7/4 12:12
 * @description
 */
public class ConfigurationRepository {
    private static volatile String[] Index_File_Name;
    private static volatile long period;
    private static volatile int port;

    private static ReentrantReadWriteLock RWLockForURLMapping = new ReentrantReadWriteLock();

    private static volatile Map<String, HostAndPortList> urlMap = new HashMap<>();

    static {
        HostAndPort hostAndPort = new HostAndPort("localhost", 8080);
        String s = "/example";
        HostAndPortList list = new DefaultHostAndPortList();
        list.add(hostAndPort);
        urlMap.put(s, list);
    }

    public static synchronized void setIndex_File_Name(String[] files) {
        if (files.length != 0) {
            Index_File_Name = files;
        }

    }

    public static synchronized String[] getIndex_File_Name() {
        return Index_File_Name;
    }

    public static synchronized long getPeriod() {
        return period;
    }

    public static synchronized void setPeriod(long time) {
        if (time > 0) {
            period = time;
        }
    }

    public static synchronized void setPort(int Port) {
        port = Port;
    }

    public static synchronized int getPort() {
        return port;
    }

    public static void put(String url, HostAndPortList hostAndPortList) {
        RWLockForURLMapping.readLock().lock();
        urlMap.put(url, hostAndPortList);
        RWLockForURLMapping.readLock().unlock();
    }

    public static HostAndPortList get(String url) {
        RWLockForURLMapping.writeLock().lock();
        HostAndPortList hostAndPort = urlMap.get(url);
        RWLockForURLMapping.writeLock().unlock();
        return hostAndPort;
    }
}
