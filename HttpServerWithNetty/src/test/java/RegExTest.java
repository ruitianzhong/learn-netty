import org.junit.Test;
import top.zhongruitian.ServerWithNetty.Utils.ContentType;

import static org.junit.Assert.assertEquals;

/**
 * @author ruitianzhong
 * @email zhongruitian2003@qq.com
 * @date 2022/7/2 18:36
 * @description
 */
public class RegExTest {

    @Test
    public void RegularExpressionTest() {
        String css = "test.css",
                html = "test.html",
                js = "vue.js",
                x_icon = "favicon.ico",
                pdf = "pdf.pdf",
                jpg = "hello.jpg",
                png = "hello.png";
        assertEquals(ContentType.getContentType(css), ContentType.CSS);
        assertEquals(ContentType.getContentType(html), ContentType.HTML);
        assertEquals(ContentType.getContentType(js), ContentType.JS);
        assertEquals(ContentType.getContentType(x_icon), ContentType.X_ICON);
        assertEquals(ContentType.getContentType(pdf), ContentType.PDF);
        assertEquals(ContentType.getContentType(jpg), ContentType.JPG);
        assertEquals(ContentType.getContentType(png), ContentType.PNG);
    }
}
