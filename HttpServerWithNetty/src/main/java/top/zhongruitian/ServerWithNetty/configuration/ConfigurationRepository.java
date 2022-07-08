/*
 * Copyright 2022 ruitianzhong
 */
package top.zhongruitian.ServerWithNetty.configuration;

import java.util.*;
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
    private static ReentrantReadWriteLock RWLockForURLList = new ReentrantReadWriteLock();

    private static volatile Map<String, HostAndPortList> urlMap = new HashMap<>();
    private static List<String> keyList = new ArrayList<>();

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
        RWLockForURLMapping.writeLock().lock();
        RWLockForURLList.writeLock().lock();
        urlMap.put(url, hostAndPortList);
        keyList.add(url);
        RWLockForURLMapping.writeLock().unlock();
        RWLockForURLList.writeLock().unlock();
    }

    public static HostAndPortList get(String url) {
        RWLockForURLMapping.readLock().lock();
        HostAndPortList hostAndPort = urlMap.get(url);
        RWLockForURLMapping.readLock().unlock();
        return hostAndPort;
    }

    public static List<String> getKeyList() {
        RWLockForURLList.readLock().lock();
        List<String> copy = new ArrayList<>();
        copy.addAll(keyList);
        RWLockForURLList.readLock().unlock();
        return copy;
    }

    public static void setUrlMap(Map<String, HostAndPortList> map) {
        if (map == null) {
            return;
        }
        RWLockForURLList.writeLock().lock();
        RWLockForURLMapping.writeLock().lock();
        urlMap = map;
        Set<String> keySet = urlMap.keySet();
        for (String s : keySet) {
            keyList.add(s);
        }
        RWLockForURLMapping.writeLock().unlock();
        RWLockForURLList.writeLock().unlock();
    }
}
