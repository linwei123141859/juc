package com.siyue.one;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FtpPoolConfig {

    private static String hostname;
    private static Integer port;
    private static String user;
    private static String pass;

    static {
        InputStream resourceAsStream = FtpPoolConfig.class.getClassLoader().getResourceAsStream("ftp.properties");
        Properties properties = new Properties();
        try {
            properties.load(resourceAsStream);
            hostname = properties.getProperty("hostname");
            port = Integer.parseInt(properties.getProperty("port"));
            user = properties.getProperty("user");
            pass = properties.getProperty("pass");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getHostname() {
        return hostname;
    }

    public static Integer getPort() {
        return port;
    }

    public static String getUser() {
        return user;
    }

    public static String getPass() {
        return pass;
    }
}
