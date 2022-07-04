/*
 * Copyright 2022 ruitianzhong
 */
package top.zhongruitian.ServerWithNetty.configuration;

/**
 * @author ruitianzhong
 * @email zhongruitian2003@qq.com
 * @date 2022/7/4 12:12
 * @description
 */
public class ConfigurationRepository {
    private static volatile String[] Index_File_Name;
    private static volatile long period;

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

}
