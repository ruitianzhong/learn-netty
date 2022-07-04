/*
 * Copyright 2022 ruitianzhong
 */
package top.zhongruitian.ServerWithNetty.Utils;

import top.zhongruitian.ServerWithNetty.configuration.ConfigurationRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author ruitianzhong
 * @email zhongruitian2003@qq.com
 * @date 2022/7/3 13:28
 * @description
 */

public class URIResult {
    private final List<String> resources;
    private boolean isChecked = false;
    private String filteredPath;
    private boolean redirect = false;

    private boolean NotFound = false;
    private String existing_default_index_file = null;
    private String contentType = null;


    private File file = null;
    private boolean succeed = false;

    public URIResult(String url) throws URISyntaxException {
        URI standardURI = new URI(url);
        resources = URIHelper.parseURIToList(standardURI);
        filteredPath = URIHelper.getFilteredPathName(resources);
    }

    private void check() {
        if (!isChecked) {
            if (filteredPath != null) {
                file = new File(filteredPath);
                if (file.exists()) {
                    if (file.isFile()) {
                        contentType = getContentType(filteredPath);
                        if (ContentType.UNKNOWN.equals(contentType)) {
                            NotFound = true;
                        } else {
                            succeed = true;
                        }
                    } else {
                        if (setDefaultIndexFileNameIfExist(filteredPath)) {
                            redirect = true;
                        } else {
                            NotFound = true;
                        }
                    }
                } else {
                    NotFound = true;
                }
            } else {
                NotFound = !setDefaultIndexFileNameIfExist("");
                redirect = !NotFound;
            }
            isChecked = true;
        }
    }


    private String getContentType(String filteredPath) {
        return ContentType.getContentType(resources.get(resources.size() - 1));
    }

    public String getContentType() {
        check();
        return this.contentType;
    }

    public InputStream getInputStream() throws FileNotFoundException {
        check();
        if (!succeed) {
            return null;
        } else {
            return new FileInputStream(file);
        }
    }

    public boolean isSucceed() {
        check();
        return succeed;
    }

    public boolean shouldRedirect() {
        check();
        return redirect;
    }

    public boolean isNotFound() {
        check();
        return NotFound;
    }

    public String getRedirectURI() {
        check();
        if (redirect) {
            return URIHelper.buildTheRedirectURI(resources, existing_default_index_file);
        }
        return null;
    }

    private boolean setDefaultIndexFileNameIfExist(String path) {

        for (String s : ConfigurationRepository.getIndex_File_Name()) {
            String RedirectFilePath = path + s + File.separator;
            File file = new File(RedirectFilePath);
            if (file.exists() && file.isFile()) {
                existing_default_index_file = s;
                return true;
            }
        }
        return false;
    }
}
