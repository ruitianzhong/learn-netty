package OutBoundHandler;

import Utils.Server;

public class InboundAndOutboundServer {
    public static void main(String[] args) {
        Server server = new Server(new MyOutboundHandler(),new MyInboundHandler());
        server.run();
    }
}
