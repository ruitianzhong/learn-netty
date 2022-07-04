/*
 * Copyright 2022 ruitianzhong
 */
package top.zhongruitian.ServerWithNetty.configuration;

import top.zhongruitian.ServerWithNetty.Utils.URIResult;
import top.zhongruitian.ServerWithNetty.exceptions.DynamicConfigException;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * @author ruitianzhong
 * @email zhongruitian2003@qq.com
 * @date 2022/7/3 14:12
 * @description
 */
public class ServerConfiguration {
    private String[] indexFiles;
    private int port;

    private Properties DEFAULT_PROPERTIES = DefaultPropertiesFactory.getDefaultProperties();

    private Properties ULTIMATE_PROPERTIES;


    private boolean built = false;

    private long time;
    private List<Properties> propertiesList;


    public ServerConfiguration(List<Properties> list) {
        if (list == null) {
            throw new IllegalArgumentException("list is null");
        }
        this.propertiesList = list;
        ULTIMATE_PROPERTIES = new Properties();
        list.add(DEFAULT_PROPERTIES);
    }

    public ServerConfiguration build() throws IOException {
        ULTIMATE_PROPERTIES.clear();//bug here
        for (Properties properties : propertiesList) {

            if (ULTIMATE_PROPERTIES.getProperty(ConfigurationPrefix.PORT) == null && properties.getProperty(ConfigurationPrefix.PORT) != null) {
                ULTIMATE_PROPERTIES.setProperty(ConfigurationPrefix.PORT, properties.getProperty(ConfigurationPrefix.PORT));
            }

            if (ULTIMATE_PROPERTIES.getProperty(ConfigurationPrefix.TIME) == null && properties.getProperty(ConfigurationPrefix.TIME) != null) {
                ULTIMATE_PROPERTIES.setProperty(ConfigurationPrefix.TIME, properties.getProperty(ConfigurationPrefix.TIME));
            }
            if (ULTIMATE_PROPERTIES.getProperty(ConfigurationPrefix.INDEX) == null && properties.getProperty(ConfigurationPrefix.INDEX) != null) {
                ULTIMATE_PROPERTIES.setProperty(ConfigurationPrefix.INDEX, properties.getProperty(ConfigurationPrefix.INDEX));
            }
        }
        int port = Integer.valueOf(ULTIMATE_PROPERTIES.getProperty(ConfigurationPrefix.PORT));

        int time = Integer.valueOf(ULTIMATE_PROPERTIES.getProperty(ConfigurationPrefix.TIME));
        String[] indexFiles = getIndexFiles();
        check(port, indexFiles, time);
        this.port = port;
        this.indexFiles = indexFiles;
        this.time = time;
        URIResult.setDefault_Index_Name(indexFiles);
        return this;
    }

    private String[] getIndexFiles() {
        String raw = ULTIMATE_PROPERTIES.getProperty(ConfigurationPrefix.INDEX);
        String[] ret;
        if (raw.contains(",")) {
            ret = raw.split(",");
        } else {
            ret = new String[]{raw};
        }
        return ret;
    }

    private void check(int port, String[] indexFiles, long time) {
        if (port <= 0 || indexFiles == null || indexFiles.length == 0 || time <= 0) {
            throw new DynamicConfigException("port <= 0 || indexFiles == null || indexFiles.length == 0 || time <= 0");
        }
    }

    public int getPort() {
        return port;
    }

    public long getTime() {
        return time;
    }

    public boolean replace(Properties oldProperties, Properties newProperties) {
        for (int i = 0; i < propertiesList.size(); i++) {
            if (oldProperties == propertiesList.get(i)) {
                propertiesList.set(i, newProperties);

                return true;
            }
        }

        return false;

    }

}
