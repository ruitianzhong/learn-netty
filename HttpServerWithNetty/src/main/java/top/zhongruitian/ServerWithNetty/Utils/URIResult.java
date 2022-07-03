package top.zhongruitian.ServerWithNetty.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class URIResult {
    private final List<String> resources ;
    private boolean isChecked = false;
    private String filteredPath = null;
    private boolean redirect = false;

    private boolean NotFound = false;


    private File file = null;
    private boolean succeed = false;

    public URIResult(String url) throws URISyntaxException {
        URI standardURI = new URI(url);
        resources = Utils.parseURIToList(standardURI);
        if (resources.size() == 0) {
            redirect = true;
        } else {
            filteredPath = URIHelper.getFilteredPathName(resources);
        }
    }

    private void check() {
        if (!isChecked) {
            if (!redirect) {
                file = new File(filteredPath);
                if (file.exists()) {
                    if (file.isFile()) {
                        String contentType = getContentType(filteredPath);
                        if (ContentType.UNKNOWN.equals(contentType)) {
                            NotFound = true;
                        }
                        succeed = true;
                    } else {
                        redirect = true;
                    }
                }
            }
            isChecked = true;
        }
    }


    private String getContentType(String filteredPath) {
        return ContentType.getContentType(resources.get(resources.size() - 1));
    }

    public InputStream getInputStream() throws FileNotFoundException {
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
        return NotFound;
    }


}
