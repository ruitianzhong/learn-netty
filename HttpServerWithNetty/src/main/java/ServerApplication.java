import top.zhongruitian.ServerWithNetty.Utils.Server;
import top.zhongruitian.ServerWithNetty.handlers.HttpChannelHandlerInitializer;

import java.io.IOException;
import java.net.URISyntaxException;

import static java.lang.System.exit;

public class ServerApplication {
    public static int DEFAULT_PORT = 9999;

    public static void main(String[] args) throws IOException, URISyntaxException {
        if (args.length != 2) {
            Server server = new Server(new HttpChannelHandlerInitializer(),DEFAULT_PORT);
            server.run();
        }else if(args[0].equals("-p")||args.equals("--port")){
            int port = Integer.valueOf(args[1]);
            if(port<=0){
                System.out.println("Port number must be larger than ZERO!");
                exit(-1);
            }
            Server server = new Server(new HttpChannelHandlerInitializer(),port);
            server.run();
        }else {
            System.out.println("Usage:--port 8080 to specify the port number");
        }
    }
}
