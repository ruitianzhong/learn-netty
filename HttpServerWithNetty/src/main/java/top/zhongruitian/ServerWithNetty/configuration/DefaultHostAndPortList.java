package top.zhongruitian.ServerWithNetty.configuration;

import java.util.ArrayList;
import java.util.Random;

public class DefaultHostAndPortList extends ArrayList<HostAndPort> implements HostAndPortList {

    @Override
    public HostAndPort getServerHostAndPort() {
        int randomPos = new Random().nextInt(size());
        return get(randomPos);
    }
}
