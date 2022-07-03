/*
 * Copyright 2022 ruitianzhong
 */
package top.zhongruitian.ServerWithNetty.Utils;

import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author ruitianzhong
 * @email zhongruitian2003@qq.com
 * @date 2022/7/3 14:12
 * @description
 */
public class ServerConfiguration {
    private long lastModified = 0L;
    private int port = 10086;
    private String[] indexFileName;
    private String propertiesFileName;

    private boolean usePropertiesFile = false;

    private Properties properties;

    private boolean built = false;

    private long time = 60 * 1000;

    public ServerConfiguration() {

    }

    public ServerConfiguration port(int port) {
        if (port <= 0) {
            return this;
        }
        this.port = port;
        return this;
    }

    public ServerConfiguration propertiesFileName(String propertiesFileName) throws IOException {
        File file = new File(propertiesFileName);
        if (file.exists() && file.isFile()) {
            lastModified = file.lastModified();
            usePropertiesFile = true;
            this.propertiesFileName = propertiesFileName;
            properties = new Properties();
            InputStream inputStream = new FileInputStream(file);
            properties.load(inputStream);
            setIndexFileName(properties);
            setPort(properties);
            long time = Integer.valueOf(properties.getProperty("server.time", "60"));
            if (time > 0) {
                this.time = time * 1000;
            }
        } else {
            throw new FileNotFoundException("Can not find the properties file");
        }
        return this;
    }

    public ServerConfiguration indexFileName(List<String> list) {
        if (list.size() == 0) {
            indexFileName = null;
            return this;
        }
        indexFileName = new String[list.size()];
        int i = 0;
        for (String temp : list) {
            indexFileName[i++] = temp;
        }
        return this;
    }

    private void setPort(Properties properties) {
        port = Integer.valueOf(properties.getProperty("top.zhongruitian.server.port", "10086"));
    }

    private void setIndexFileName(Properties properties) {
        String index = properties.getProperty("server.index", "index.html,home.html");
        String[] indexes = index.split(",");
        if (indexes == null) {
            indexFileName = new String[]{index};
        } else {
            indexFileName = indexes;
        }
    }


    public ServerConfiguration build() {
        if (this.usePropertiesFile && !built) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    File file = new File(propertiesFileName);
                    if (file.exists() && file.isFile() && file.lastModified() > lastModified) {
                        try {
                            InputStream inputStream = new FileInputStream(file);
                            Properties properties = new Properties();
                            properties.load(inputStream);
                            setIndexFileName(properties);
                            URIResult.setDefault_Index_Name(indexFileName);
                            System.out.println("Configuration changed successfully!");
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }, 0, time);
        }
        built = true;
        return this;
    }

    public ServerConfiguration time(long second) {
        this.time = second > 0 ? second * 1000 : 20000;
        return this;
    }

}
