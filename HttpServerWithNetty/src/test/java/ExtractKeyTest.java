import org.junit.Test;
import top.zhongruitian.ServerWithNetty.Utils.ContentType;

import static org.junit.Assert.assertEquals;

/**
 * @author ruitianzhong
 * @email zhongruitian2003@qq.com
 * @date 2022/7/2 18:36
 * @description
 */
public class ExtractKeyTest {

    @Test
    public void checkExtensionNameTest() {
        String css = "semantic.min.css",
                html = "test.html",
                js = "vue.js",
                pdf = "pdf.pdf",
                jpg = "hello.jpg",
                png = "hello.png",
                unknown = "unknown",
                unknownExtension = "unknown.unknown";
        assertEquals(ContentType.getContentType(css), ContentType.CSS);
        assertEquals(ContentType.getContentType(html), ContentType.HTML);
        assertEquals(ContentType.getContentType(js), ContentType.JS);
        assertEquals(ContentType.getContentType(pdf), ContentType.PDF);
        assertEquals(ContentType.getContentType(jpg), ContentType.JPG);
        assertEquals(ContentType.getContentType(png), ContentType.PNG);
        assertEquals(ContentType.getContentType(unknown), ContentType.UNKNOWN);
        assertEquals(ContentType.getContentType(unknownExtension), ContentType.UNKNOWN);
    }

    @Test
    public void PropertiesParsingTest() {


    }
}
