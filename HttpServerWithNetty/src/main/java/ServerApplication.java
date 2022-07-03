import top.zhongruitian.ServerWithNetty.Utils.*;
import top.zhongruitian.ServerWithNetty.handlers.HttpChannelHandlerInitializer;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static java.lang.System.exit;

public class ServerApplication {
    public static int DEFAULT_PORT = 10086;


    public static void main(String[] args) throws IOException, URISyntaxException {
        List<Properties> propertiesList = ParametersParser.parseParameters(args);
        ServerConfiguration configuration = new ServerConfiguration(propertiesList);
        configuration.build();
        Server server = new Server(new HttpChannelHandlerInitializer(),configuration);
        Watcher watcher = new Watcher(configuration);
        watcher.start();
        server.run();
    }
}
