/*
 * Copyright 2022 ruitianzhong.All rights reserved.
 */

import org.junit.Test;
import top.zhongruitian.ServerWithNetty.Utils.CommandParametersParser;
import top.zhongruitian.ServerWithNetty.configuration.ConfigurationRepository;
import top.zhongruitian.ServerWithNetty.configuration.ServerConfiguration;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author ruitianzhong
 * @email zhongruitian2003@qq.com
 * @date 2022/7/5 10:22
 * @description
 */
public class CommandParsingTest {
    public static String[] defaultIndexFile = new String[]{"index.html", "home.html"};
    public static int defaultPort = 10086;
    public static long defaultPeriod = 2000;

    @Test
    public void specifyPropertiesFileTest() throws IOException {

        String[] args = {"-f", "NoExist.conf"};
        try {
            CommandParametersParser.parseParameters(args);
            fail();
        } catch (Exception ex) {

        }
    }

    @Test
    public void CommandSetPortOnly() throws IOException {
        String[] args = {"-p", "1233"};
        int port = 1233;
        buildConfiguration(args);
        checkConfiguration(defaultIndexFile, port, defaultPeriod);
    }

    @Test
    public void CommandSetPeriodOnly() throws IOException {
        String[] args = new String[]{"-t", "12345"};
        buildConfiguration(args);
        checkConfiguration(defaultIndexFile, defaultPort, 12345);
    }

    @Test
    public void CommandSetIndexFileOnly() throws IOException {
        String[] args = new String[]{"-i", "index.html", "test.html", "hello.pdf", "last.html"};
        String[] expect = new String[]{"index.html", "test.html", "hello.pdf", "last.html"};
        buildConfiguration(args);
        checkConfiguration(expect, defaultPort, defaultPeriod);
    }

    @Test
    public void ParameterDoNotExistTest() {
        String[] args = new String[]{"-a", "10086", "-f", "9999"};
        expectFail(args);
    }

    @Test
    public void ParameterError1() {
        String[] args = new String[]{"-p", "-f", "application.properties"};
        expectFail(args);
    }

    @Test
    public void ParameterError2() {
        String[] args = new String[]{"-t", "123", "-p", "-i", "index.html", "test.html"};
        expectFail(args);
    }

    @Test
    public void allConfigTest() throws IOException {
        String[] args = new String[]{"-t", "123", "-p", "80", "-i", "index.html", "hello.html", "test.html"};
        String[] expectIndex = new String[]{"index.html", "hello.html", "test.html"};
        buildConfiguration(args);
        checkConfiguration(expectIndex, 80, 123);

    }


    public void expectFail(String[] args) {
        try {
            buildConfiguration(args);
            fail();
        } catch (Exception ex) {

        }
    }

    public void checkConfiguration(String[] expectIndex, int port, long period) {
        assertEquals(period, ConfigurationRepository.getPeriod());
        assertEquals(port, ConfigurationRepository.getPort());
        String[] actual = ConfigurationRepository.getIndex_File_Name();
        for (int i = 0; i < expectIndex.length; i++) {
            assertEquals(expectIndex[i], actual[i]);
        }
    }

    public void buildConfiguration(String[] args) throws IOException {
        ServerConfiguration configuration = new ServerConfiguration(CommandParametersParser.parseParameters(args));
        configuration.load();
    }
}
