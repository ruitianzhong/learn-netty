package Channel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientWithoutNetty {
    public static int port = 8080;
    public static String host = "localhost";
    public static OutputStream outputStream = null;
    public static InputStream inputStream = null;


    public static void main(String[] args) throws IOException, InterruptedException {
        createSocket();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Please enter the words");
            String s = scanner.nextLine();
            outputStream.write(s.getBytes(StandardCharsets.UTF_8));
        }
    }

    public static Socket createSocket() {
        Socket socket = new Socket();
        try {
            socket.setReuseAddress(true);
            socket.setSoLinger(true, 0);
            socket.setTcpNoDelay(true);
            socket.setKeepAlive(true);
            socket.connect(new InetSocketAddress(host, port));
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
        } catch (IOException ex) {

        }
        return socket;
    }
}
