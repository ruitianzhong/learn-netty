package top.zhongruitian.ServerWithNetty.Utils;

import java.util.regex.Pattern;

public class ContentType {
  public static  final  String UNKNOWN ="UNKNOWN";
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

    public static String getContentType(String fileName) {
        if(fileName==null){
            return UNKNOWN;
        }
        int lastIndexOf=fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return UNKNOWN;
        }
        String last = fileName.substring(lastIndexOf);
        if(last.equals(".html")){
            return ContentType.HTML;
        }
        if (last.equals(".css")) {

            return ContentType.CSS;
        }
        if (last.equals(".js")) {
            return ContentType.JS;
        }
        if (last.equals(".pdf")) {
            return ContentType.PDF;
        }

        if (last.equals(".jpg")||last.equals(".jpeg")) {
            return ContentType.JPG;
        }
        if (last.equals(".png")) {
            return ContentType.PNG;
        }
        if (last.equals(".xml")) {
            return ContentType.XML;
        }

        return UNKNOWN;
    }
}
