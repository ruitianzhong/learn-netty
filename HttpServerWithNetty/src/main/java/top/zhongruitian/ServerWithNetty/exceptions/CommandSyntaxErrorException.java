package top.zhongruitian.ServerWithNetty.exceptions;

public class CommandSyntaxErrorException extends  ServerException{
    public CommandSyntaxErrorException(String message) {
        super(message);
    }
}
