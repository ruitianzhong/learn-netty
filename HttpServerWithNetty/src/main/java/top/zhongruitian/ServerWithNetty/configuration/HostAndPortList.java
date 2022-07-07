package top.zhongruitian.ServerWithNetty.configuration;

import java.util.List;

public interface HostAndPortList extends List<HostAndPort> {
    HostAndPort getServerHostAndPort();
}
