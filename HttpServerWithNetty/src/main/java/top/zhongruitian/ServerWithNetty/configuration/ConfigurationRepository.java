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

    public static synchronized void setIndex_File_Name(String [] files){
        Index_File_Name = files;
    }
    public static synchronized String[]getIndex_File_Name(){
        return Index_File_Name;
    }

}
