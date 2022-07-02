package top.zhongruitian.ServerWithNetty.Utils;

import java.util.regex.Pattern;

public class ContentType {

    public static final String HTML = "text/html;charset=utf-8";
    public static final String CSS = "text/css";
    public static final String JS = "application/javascript";
    public static final String PNG = "image/png";
    public static final String JPG = "image/jpeg";
    public static final String JSON = "application/json";
    public static final String XML = "text/xml";
    public static final String PDF = "application/pdf";
    public static final String X_ICON = "image/x-icon";
    public static final String Default_Content_Type = HTML;

    public static String getContentType(String last) {
        if (last == null) {
            return Default_Content_Type;
        }
        if (Pattern.matches("\\w+.pdf", last)) {
            return ContentType.PDF;
        }
        if (Pattern.matches("\\w+.css", last)) {

            return ContentType.CSS;
        }
        if (Pattern.matches("\\w+.js", last)) {
            return ContentType.JS;
        }
        if (Pattern.matches("\\w+.ico", last)) {
            return ContentType.X_ICON;
        }
        if (Pattern.matches("\\w+.jpg", last)) {
            return ContentType.JPG;
        }
        if (Pattern.matches("\\w+.png", last)) {
            return ContentType.PNG;
        }
        if (Pattern.matches("\\w+.xml", last)) {
            return ContentType.XML;
        }
        return Default_Content_Type;
    }
}
