package top.zhongruitian.ServerWithNetty.exceptions;

public class DynamicConfigException extends ServerException {
    public DynamicConfigException(String message) {
        super(message);
    }

    public DynamicConfigException(Throwable throwable) {
        super(throwable);
    }

    public DynamicConfigException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
