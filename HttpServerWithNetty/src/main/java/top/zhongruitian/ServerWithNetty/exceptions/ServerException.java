package top.zhongruitian.ServerWithNetty.exceptions;

public class ServerException extends RuntimeException {
    public ServerException(String message) {
        super(message);
    }

    public ServerException(Throwable throwable) {
        super(throwable);
    }

    public ServerException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
