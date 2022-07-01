package top.zhongruitian.BytesToIntegerDecoder;

import top.zhongruitian.utils.Server;

public class BytesToIntegerServer {
    public static void main(String[] args) {
        Server server = new Server(8080, new FrameExtractionHandler(), new FrameToIntegerHandler());
        server.run();
    }
}
