import top.zhongruitian.ServerWithNetty.Utils.CommandParametersParser;
import top.zhongruitian.ServerWithNetty.Utils.Server;
import top.zhongruitian.ServerWithNetty.configuration.LocalFileWatcher;
import top.zhongruitian.ServerWithNetty.configuration.ServerConfiguration;
import top.zhongruitian.ServerWithNetty.handlers.HttpServerChannelHandlerInitializer;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class ServerApplication {
    public static void startServer(String[] args) throws IOException {
        List<Properties> propertiesList = CommandParametersParser.parseParameters(args);
        ServerConfiguration configuration = new ServerConfiguration(propertiesList);
        configuration.load();
        Server server = new Server(new HttpServerChannelHandlerInitializer(), configuration);
        if (CommandParametersParser.havaFileToWatch()) {
            LocalFileWatcher watcher = new LocalFileWatcher(configuration,
                    CommandParametersParser.fileName,
                    CommandParametersParser.fileProperties,
                    CommandParametersParser.lastModified);
            watcher.start();//start the watcher when there is configuration file/properties file.
        }
        server.run();
    }
}
