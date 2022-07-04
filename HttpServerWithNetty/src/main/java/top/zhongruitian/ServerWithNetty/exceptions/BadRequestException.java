package top.zhongruitian.ServerWithNetty.exceptions;

public class BadRequestException extends ServerException {
    private String BadRequestURI = null;

    public BadRequestException(String message) {
        super(message);
        this.BadRequestURI = message;
    }

    public BadRequestException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
