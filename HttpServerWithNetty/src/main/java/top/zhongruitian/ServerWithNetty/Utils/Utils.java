/*
 * Copyright 2022 ruitianzhong
 */
package top.zhongruitian.ServerWithNetty.Utils;

import top.zhongruitian.ServerWithNetty.exceptions.BadRequestException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Utils {
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

}
