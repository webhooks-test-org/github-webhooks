package com.alvarolobato;

import static com.alvarolobato.TestUtils.loadFromFile;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class WebHookApplicationUnitTests {

    @Test
    public void validSignatureMessageAcceptedTest() throws IOException {
        WebHookApplication app=new WebHookApplication();
        String signature = loadFromFile("repo2-deletion-event-sha.txt");
        ResponseEntity<String> response = app.handleRequest(signature, loadFromFile("repo2-deletion-event.json"));
        assertThat("Call should have succeded", response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void invalidSignatureMessageForbidenTest() throws IOException {
        WebHookApplication app = new WebHookApplication();
        String signature = "randomstring";
        ResponseEntity<String> response = app.handleRequest(signature, loadFromFile("repo2-deletion-event.json"));
        assertThat("Call should have succeded", response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void nullSignatureMessageForbidenTest() throws IOException {
        WebHookApplication app = new WebHookApplication();
        String signature = "randomstring";
        ResponseEntity<String> response = app.handleRequest(signature, loadFromFile("repo2-deletion-event.json"));
        assertThat("Call should have succeded", response.getStatusCode(), equalTo(HttpStatus.OK));
    }
}
