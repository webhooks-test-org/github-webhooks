package com.alvarolobato;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.charset.Charset;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;

public class WebHookApplicationUnitTests {

    @Test
    public void validSignatureMessageAcceptedTest() throws IOException {
        WebHookApplication app=new WebHookApplication();
        String signature = loadFromFile("repo2-deletion-event2-sha.txt");
        ResponseEntity<String> response = app.handle(signature, loadFromFile("repo2-deletion-event.json"));
        assertThat("Call should have succeded", response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void invalidSignatureMessageForbidenTest() throws IOException {
        WebHookApplication app = new WebHookApplication();
        String signature = "randomstring";
        ResponseEntity<String> response = app.handle(signature, loadFromFile("repo2-deletion-event.json"));
        assertThat("Call should have succeded", response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void nullSignatureMessageForbidenTest() throws IOException {
        WebHookApplication app = new WebHookApplication();
        String signature = "randomstring";
        ResponseEntity<String> response = app.handle(signature, loadFromFile("repo2-deletion-event.json"));
        assertThat("Call should have succeded", response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    public static String loadFromFile(String name) throws IOException {
        return StreamUtils.copyToString(WebHookApplicationUnitTests.class.getResourceAsStream(name), Charset.forName("UTF-8"));
    }
}
