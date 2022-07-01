package top.zhongruitian.BytesToIntegerDecoder;

import top.zhongruitian.Channel.ClientWithoutNetty;

import java.io.IOException;

public class ClientSendInteger extends ClientWithoutNetty {
    public static void main(String[] args) throws IOException {
        createSocket();
        byte[] bytes = new byte[4];

        for (int i = 0; i < 10; i++) {
            IntegerToByte(i, bytes);
            outputStream.write(bytes);
        }
        while (true) {

        }
    }

    public static byte[] IntegerToByte(int integer, byte[] bytes) {
        if (bytes.length != 4) {
            throw new IllegalArgumentException("the length of bytes array is not 4.");
        }
        bytes[0] = (byte) (integer >> 24);
        bytes[1] = (byte) (integer >> 16);
        bytes[2] = (byte) (integer >> 8);
        bytes[3] = (byte) (integer >> 0);
        return bytes;
    }
}
