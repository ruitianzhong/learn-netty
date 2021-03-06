package top.zhongruitian.ServerWithNetty.Utils;

import top.zhongruitian.ServerWithNetty.exceptions.BadRequestException;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class URIHelper {
    public static List<String> parseURIToList(URI uri) {

        List<String> list = new ArrayList<>();
        String path = uri.getPath();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(0) != '/') {
                throw new BadRequestException("The first character of uri is not /");
            }

            char temp = path.charAt(i);
            if (temp == '/') {
                if (!sb.isEmpty() && sb.length() > 0) {
                    list.add(sb.toString());
                    sb.delete(0, sb.length());
                } else if (temp == '\\') {
                    throw new BadRequestException("Thr uri contains illegal character");
                }
                continue;
            }
            sb.append(temp);
        }
        if (!sb.isEmpty()) {
            list.add(sb.toString());
        }
        return list;
    }

    public static String buildTheRedirectURI(List<String> list, String added) {
        if (list.size() == 0) {
            return "/" + added;
        }
        StringBuffer sb = new StringBuffer();
        for (String s : list) {
            sb.append("/");
            sb.append(s);
        }
        return sb + "/" + added;
    }

    public static String getFilteredPathName(List<String> list) {

        if (list.size() == 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (String s : list) {
            sb.append(s);
            sb.append(File.separator);
        }
        return sb.toString();
    }

}
