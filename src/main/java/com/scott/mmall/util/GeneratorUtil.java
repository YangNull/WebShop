package com.scott.mmall.util;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.apache.commons.lang3.StringUtils;
import sun.applet.Main;

import java.util.UUID;

/**
 * Created by Ubuntu on 2017/5/24.
 */
public class GeneratorUtil {
    public static String generatorUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }

    public static String generatorToken() {
        return UUID.randomUUID().toString();
    }

    public static void main(String[] args) {
        String uuid = UUID.randomUUID().toString();
    }
}
