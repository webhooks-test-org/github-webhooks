package com.alvarolobato;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.util.StreamUtils;

public class TestUtils {
    public static String loadFromFile(String name) throws IOException {
        return StreamUtils.copyToString(WebHookApplicationUnitTests.class.getResourceAsStream(name), Charset.forName("UTF-8"));
    }
}
