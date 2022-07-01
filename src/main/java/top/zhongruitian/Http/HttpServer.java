package top.zhongruitian.Http;

import top.zhongruitian.utils.Server;

public class HttpServer {

    public static void main(String[] args) {
        Server server = new Server(new HttpPipelineInitializer(false));
        server.run();
    }
}
