package BytesToIntegerDecoder;

import Utils.Server;

public class BytesToIntegerServer {
    public static void main(String[] args) {
        Server server = new Server(8080,new FrameExtractionHandler(),new FrameToIntegerHandler());
        server.run();
    }
}
