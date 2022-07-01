package Http;

import Utils.Server;

public class HttpServer {

    public static void main(String[] args) {
        Server server = new Server(new HttpPipelineInitializer(false));
        server.run();
    }
}
