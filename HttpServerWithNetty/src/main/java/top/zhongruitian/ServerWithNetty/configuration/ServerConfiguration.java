package top.zhongruitian.ServerWithNetty.configuration;

import top.zhongruitian.ServerWithNetty.exceptions.DynamicConfigException;

import java.util.*;

public class ServerConfiguration {
    private String[] indexFiles;
    private int port;

    private Properties DEFAULT_PROPERTIES = DefaultPropertiesFactory.getDefaultProperties();

    private Properties ULTIMATE_PROPERTIES;
    private boolean loaded = false;
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

    public void load() {
        //bug here previously
        if (!loaded) {
            mergeThePropertiesAndUpdateTheConfigurationRepo();
            loaded = true;
        }
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
        checkLoaded();
        return port;
    }

    public boolean replace(Properties oldProperties, Properties newProperties) {
        checkLoaded();
        for (int i = 0; i < propertiesList.size(); i++) {
            if (oldProperties == propertiesList.get(i)) {
                propertiesList.remove(oldProperties);
                propertiesList.add(0, newProperties);
                return true;
            }
        }
        return false;
    }

    public boolean isLoaded() {
        return loaded;
    }

    private void checkLoaded() {
        if (!loaded) {
            throw new IllegalArgumentException("The ServerConfiguration has not been loaded");
        }
    }

    public void reload() {
        checkLoaded();
        ULTIMATE_PROPERTIES.clear();
        mergeThePropertiesAndUpdateTheConfigurationRepo();
    }


    private void mergeThePropertiesAndUpdateTheConfigurationRepo() {
        loadRepostConfig();
        updateConfiguration();
        updateConfigurationRepo();
    }

    private void updateConfiguration() {
        for (Properties properties : propertiesList) {

            if (ULTIMATE_PROPERTIES.getProperty(ConfigurationPrefix.PORT) == null && properties.getProperty(ConfigurationPrefix.PORT) != null) {
                ULTIMATE_PROPERTIES.setProperty(ConfigurationPrefix.PORT, properties.getProperty(ConfigurationPrefix.PORT));
            }

            if (ULTIMATE_PROPERTIES.getProperty(ConfigurationPrefix.PERIOD) == null && properties.getProperty(ConfigurationPrefix.PERIOD) != null) {
                ULTIMATE_PROPERTIES.setProperty(ConfigurationPrefix.PERIOD, properties.getProperty(ConfigurationPrefix.PERIOD));
            }
            if (ULTIMATE_PROPERTIES.getProperty(ConfigurationPrefix.INDEX) == null && properties.getProperty(ConfigurationPrefix.INDEX) != null) {
                ULTIMATE_PROPERTIES.setProperty(ConfigurationPrefix.INDEX, properties.getProperty(ConfigurationPrefix.INDEX));
            }
        }
    }

    private void updateConfigurationRepo() {
        int port = Integer.valueOf(ULTIMATE_PROPERTIES.getProperty(ConfigurationPrefix.PORT));
        int time = Integer.valueOf(ULTIMATE_PROPERTIES.getProperty(ConfigurationPrefix.PERIOD));
        String[] indexFiles = getIndexFiles();
        check(port, indexFiles, time);
        this.port = port;
        this.indexFiles = indexFiles;
        this.time = time;
        ConfigurationRepository.setIndex_File_Name(indexFiles);
        ConfigurationRepository.setPeriod(time);
        ConfigurationRepository.setPort(port);
    }

    private void loadRepostConfig() {
        for (Properties properties : propertiesList) {
            String result = properties.getProperty(ConfigurationPrefix.REPOST);
            if (result != null) {
                if (!"true".equals(result)) {
                    Map<String, HostAndPortList> map = new HashMap<>();
                    ConfigurationRepository.setUrlMap(map);
                    return;
                }
                updateConfigRepo(properties);
                break;
            }
        }
    }

    private void updateConfigRepo(Properties properties) {
        String prefix = ConfigurationPrefix.GROUPS;
        List<HostAndPortList> list = new ArrayList<>();
        int i = 1;
        while (true) {
            String s = properties.getProperty(prefix + "." + i);
            if (s == null) {
                break;
            }
            list.add(buildHostAndPortList(s));
            i++;
        }
        int j = 1;
        Map<String, HostAndPortList> map = new HashMap<>();
        while (j < i) {
            List<String> urlList = new ArrayList<>();
            String mapping = properties.getProperty(ConfigurationPrefix.MAPPING + "." + j);

            if (mapping == null) {
                urlList.add("");
            } else {
                fillUrlList(urlList, mapping);
            }
            HostAndPortList hostAndPortList = list.get(j - 1);
            List<String> remove = new ArrayList<>();
            for (String temp : urlList) {
                if (temp.length() != 0) {
                    map.put(temp, hostAndPortList);
                }
            }
            j++;
        }
        ConfigurationRepository.setUrlMap(map);
    }

    private HostAndPortList buildHostAndPortList(String name) {
        HostAndPortList list = new DefaultHostAndPortList();
        String[] url = name.split(";");
        for (String temp : url) {
            String[] hostAndPortString = temp.split(":");
            if (hostAndPortString.length != 2) {
                throw new DynamicConfigException("host and port syntax is wrong");
            }
            String host = hostAndPortString[0];
            int port = Integer.valueOf(hostAndPortString[1]);
            HostAndPort hostAndPort = new HostAndPort(host, port);
            list.add(hostAndPort);
        }
        return list;
    }

    private void fillUrlList(List<String> list, String properties) {
        String[] s = properties.split(";");
        for (String temp : s) {
            list.add(temp);
        }
    }
}
