package top.zhongruitian.ServerWithNetty.configuration;

public class HostAndPort {
    private int port;
    private String host;

    public HostAndPort(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

}
