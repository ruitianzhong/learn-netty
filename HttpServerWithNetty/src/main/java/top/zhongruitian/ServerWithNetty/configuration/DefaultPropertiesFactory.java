/*
 * Copyright 2022 ruitianzhong
 */
package top.zhongruitian.ServerWithNetty.configuration;

import java.util.Properties;

/**
 * @author ruitianzhong
 * @email zhongruitian2003@qq.com
 * @date 2022/7/3 18:26
 * @description
 */
public class DefaultPropertiesFactory {
    public static Properties getDefaultProperties() {
        Properties properties = new Properties();
        properties.setProperty(ConfigurationPrefix.PORT, ConfigValueConstants.PORT);
        properties.setProperty(ConfigurationPrefix.PERIOD, ConfigValueConstants.PERIOD);
        properties.setProperty(ConfigurationPrefix.INDEX, ConfigValueConstants.INDEX);
        return properties;
    }
}
