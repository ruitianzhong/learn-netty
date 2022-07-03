package top.zhongruitian.ServerWithNetty.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ParametersParser {
    public static List<Properties> parseParameters(String[] args) throws IOException {
        List<Properties> propertiesList = new ArrayList<>();
        String fileName = null;
        if (args.length == 0) {
            Properties properties = getDefaultPropertiesIfExist(ConfigValueConstants.DEFAULT_PROPERTIES_FILE_NAME);
            if (properties != null) {
                fileName = ConfigValueConstants.DEFAULT_PROPERTIES_FILE_NAME;
                propertiesList.add(properties);
                Watcher.watchedFileName = fileName;
            }
            return propertiesList;
        }
        boolean file = false, command = false;
        Properties fileProperties = null,
                commandProperties = new Properties();
        int length = args.length, i = 0;

        while (i < length) {
            String s = args[i];
            if ("-p".equals(s) && length - i > 1) {
                commandProperties.setProperty(ConfigurationPrefix.PORT, args[++i]);
                i++;
                command = true;
            } else if ("-t".equals(s) && length - i > 1) {
                commandProperties.setProperty(ConfigurationPrefix.TIME, args[++i]);
                i++;
                command = true;
            } else if ("-i".equals(s) && length - i > 1) {
                i++;
                StringBuilder sb = new StringBuilder();
                while (i < length) {
                    if (i + 1 == length) {
                        sb.append(args[i]);
                    } else {
                        sb.append(args[i] + ",");
                    }
                    i++;
                }
                commandProperties.setProperty(ConfigurationPrefix.INDEX, sb.toString());
                command = true;
            } else if ("-f".equals(s) && length - i > 1) {
                fileName = args[++i];
                i++;
                fileProperties = getDefaultPropertiesIfExist(fileName);
                if (fileProperties != null) {
                    file = true;
                }
            }
        }
        if (command) {
            propertiesList.add(commandProperties);

        }
        if (file) {
            Watcher.watchedFileName = fileName;
            Watcher.watchedProperties = fileProperties;
            propertiesList.add(fileProperties);
        }
        return propertiesList;
    }

    private static Properties getDefaultPropertiesIfExist(String fileName) throws IOException {
        File file = new File(fileName);
        if (file.exists() && file.isFile() && file.canRead()) {
            InputStream inputStream = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(inputStream);
            Watcher.lastModified = file.lastModified();
            return properties;
        } else {
            return null;
        }
    }

}
