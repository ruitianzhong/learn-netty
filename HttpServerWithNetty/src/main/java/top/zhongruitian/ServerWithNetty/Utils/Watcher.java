package top.zhongruitian.ServerWithNetty.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class Watcher {
    public static long lastModified;

    public static Properties watchedProperties;
    public static String watchedFileName = null;
    private ServerConfiguration configuration;
    private Properties properties;

    public Watcher(ServerConfiguration configuration) {
        this.configuration = configuration;
    }

    public void start() {
        Timer timer = new Timer();
        System.out.println("Start the timer");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                if (watchedFileName != null) {
                    {
                        try {
                            setProperties();
                            if (properties != null && configuration.replace(watchedProperties, properties)) {
                                configuration.build();
                                watchedProperties = properties;
                            }
                            String[] test = URIResult.Default_Index_Name;

                        } catch (FileNotFoundException e) {
                            System.out.println("ex1");
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            System.out.println("ex2");
                            throw new RuntimeException(e);
                        }

                    }
                }
            }
        }, 0, configuration.getTime());
    }


    private void setProperties() throws IOException {
        File file = new File(watchedFileName);

        if (file.exists() && file.isFile() && file.lastModified() > lastModified) {
            FileInputStream fileInputStream = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(fileInputStream);
            this.properties = properties;
            lastModified = file.lastModified();
        } else {
            properties = null;
        }

    }

}
