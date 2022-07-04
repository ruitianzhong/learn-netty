import top.zhongruitian.ServerWithNetty.Utils.ParametersParser;
import top.zhongruitian.ServerWithNetty.Utils.Server;
import top.zhongruitian.ServerWithNetty.configuration.ServerConfiguration;
import top.zhongruitian.ServerWithNetty.configuration.Watcher;
import top.zhongruitian.ServerWithNetty.handlers.HttpChannelHandlerInitializer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Properties;

public class ServerApplication {
    public static int DEFAULT_PORT = 10086;


    public static void main(String[] args) throws IOException, URISyntaxException {
        List<Properties> propertiesList = ParametersParser.parseParameters(args);
        ServerConfiguration configuration = new ServerConfiguration(propertiesList);
        configuration.build();
        Server server = new Server(new HttpChannelHandlerInitializer(), configuration);
        Watcher watcher = new Watcher(configuration);
        watcher.start();
        server.run();
    }
}
