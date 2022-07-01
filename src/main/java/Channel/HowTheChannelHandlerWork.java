package Channel;

import Utils.Server;

public class HowTheChannelHandlerWork {
    public static void main(String[] args) {
        Server server = new Server(8080, new FirstHandler(), new SecondHandler(),new ThirdHandler());
        server.run();
    }
}
