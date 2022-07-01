import top.zhongruitian.ServerWithNetty.Utils.Server;
import top.zhongruitian.ServerWithNetty.handlers.HttpChannelHandlerInitializer;

import java.io.IOException;
import java.net.URISyntaxException;

public class ServerApplication {
    public static void main(String[] args) throws IOException, URISyntaxException {
        Server server = new Server(new HttpChannelHandlerInitializer());
        server.run();
    }
}
