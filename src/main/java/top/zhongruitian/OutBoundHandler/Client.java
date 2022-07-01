package top.zhongruitian.OutBoundHandler;

import top.zhongruitian.Channel.ClientWithoutNetty;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Client extends ClientWithoutNetty {
    public static void main(String[] args) throws IOException {
        createSocket();
        outputStream.write("Hello.html world!".getBytes(StandardCharsets.UTF_8));
        while (true) {

        }

    }
}
