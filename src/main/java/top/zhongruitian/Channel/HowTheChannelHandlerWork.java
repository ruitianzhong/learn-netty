package top.zhongruitian.Channel;

import top.zhongruitian.utils.Server;

public class HowTheChannelHandlerWork {
    public static void main(String[] args) {
        Server server = new Server(8080, new FirstHandler(), new SecondHandler(), new ThirdHandler());
        server.run();
    }
}
