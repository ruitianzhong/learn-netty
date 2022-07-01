import top.zhongruitian.ServerWithNetty.handlers.HttpChannelHandlerInitializer;
import top.zhongruitian.ServerWithNetty.Utils.Server;

import java.io.IOException;
import java.net.URISyntaxException;

public class ServerApplication {
    public static void main(String[] args) throws IOException, URISyntaxException {
        Server server = new Server(new HttpChannelHandlerInitializer());
        server.run();
    }
}
