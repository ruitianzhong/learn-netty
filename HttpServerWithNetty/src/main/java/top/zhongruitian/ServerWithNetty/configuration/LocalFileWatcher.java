package top.zhongruitian.ServerWithNetty.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class LocalFileWatcher implements Watcher {
    private long lastModified;

    private Properties watchedProperties;
    private String watchedFileName = null;
    private ServerConfiguration configuration;
    private Properties properties;
    private long period;


    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if (watchedFileName != null) {
                {
                    try {
                        setProperties();
                        if (properties != null && configuration.replace(watchedProperties, properties)) {
                            configuration.load();
                            watchedProperties = properties;
                            log("Configuration was reloaded successfully");
                            if (updatePeriodAndCheckIfRestartNeeded()) {
                                restart();
                            }
                        }

                    } catch (FileNotFoundException e) {
                        log("FileNotFound");
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        log("throw an IOException");
                        throw new RuntimeException(e);
                    }

                }
            }
        }
    };
    private Timer timer;

    public LocalFileWatcher(ServerConfiguration configuration, String fileName, Properties fileProperties, long lastModified) {
        if (configuration == null || fileName == null || fileProperties == null) {
            throw new IllegalArgumentException("configuration is null");
        }
        this.configuration = configuration;
        this.watchedFileName = fileName;
        this.watchedProperties = fileProperties;
        this.lastModified = lastModified;
        timer = new Timer();
    }


    @Override
    public void start() {
        period = configuration.getTime();
        log("Timer was started");
        timer.schedule(task, 0, configuration.getTime());
    }

    private void restart() {
        log("restart the timer");
        timer.cancel();
        timer = new Timer();
        timer.schedule(task, 0, configuration.getTime());
    }

    private void setProperties() throws IOException {
        File file = new File(watchedFileName);
        if (file.exists() && file.isFile() && file.lastModified() > lastModified) {
            FileInputStream fileInputStream = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(fileInputStream);
            this.properties = properties;
            lastModified = file.lastModified();
            log("Configuration file had changed.Reloading...");

        } else {
            properties = null;
        }

    }

    private void log(String msg) {
        String date = new Date().toString();
        System.out.println(date + " " + msg);
    }

    private boolean updatePeriodAndCheckIfRestartNeeded() {
        String period = properties.getProperty(ConfigurationPrefix.TIME);
        if (period == null) {
            return false;
        }
        long newPeriod = Integer.valueOf(period);
        if (newPeriod > 0 && newPeriod != this.period) {
            this.period = newPeriod;
            return true;
        }
        return false;
    }
}
