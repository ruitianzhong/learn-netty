package top.zhongruitian.OutBoundHandler;

import top.zhongruitian.utils.Server;

public class InboundAndOutboundServer {
    public static void main(String[] args) {
        Server server = new Server(new MyOutboundHandler(), new MyInboundHandler());
        server.run();
    }
}
