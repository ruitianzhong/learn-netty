import org.junit.Test;
import top.zhongruitian.ServerWithNetty.Utils.Utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class UriTest {
    public static String case1 = "//";
    public static String case2 = "/index.html ";
    public static String case3 = "/static/hello/world/test/abc/";
    public static String case4 = "/static/hello/world/test/abc";
    public static String case5 = "/abc//abc";

    public static String[] expected3 = {"static/hello", "world", "test", "abc"};
    public static String[] expected5 = {"abc", "abc"};

    @Test
    public void parseUriToListTest() throws URISyntaxException {
        List<String> list3 = Utils.parseURIToList(new URI(case3)),
                list4 = Utils.parseURIToList(new URI(case4)),
                list5 = Utils.parseURIToList(new URI(case5));

        String s;
        int i = 0;
        try {
            List<String> list1 = Utils.parseURIToList(new URI(case1));
            fail();
        } catch (URISyntaxException ex) {

        }

        try {
            List<String> list2 = Utils.parseURIToList(new URI(case2));
            for (String temp : list2) {
                System.out.println(temp);
            }
            fail();
        } catch (URISyntaxException ex) {

        }

        for (String temp : list3) {

            assertTrue(expected3[i].equals(temp));
            i++;

        }
        i = 0;
        for (String temp : list4) {
            assertTrue(expected3[i].equals(temp));
            i++;

        }
        i = 0;
        for (String temp : list5) {
            assertTrue(expected5[i].equals(temp));
            i++;
        }

    }
}
