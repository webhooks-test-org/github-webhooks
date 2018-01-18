package com.alvarolobato;

import static com.alvarolobato.TestUtils.loadFromFile;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class WebHookApplicationUnitTests {

    final static String TEST_SECRET = "mJ.DU2HeC2z2FYmvpY^jKktPQt";

    @Test
    public void validSignatureMessageAcceptedTest() throws IOException {
        WebHookApplication app = prepareApp();
        String signature = loadFromFile("repo2-deletion-event-sha.txt");
        ResponseEntity<String> response = app.handleRequest("repository", signature, loadFromFile("repo2-deletion-event.json"));
        assertThat("Call should have succeded", response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void invalidSignatureMessageForbidenTest() throws IOException {
        WebHookApplication app = prepareApp();
        String signature = "randomstring";
        ResponseEntity<String> response = app.handleRequest("repository", signature, loadFromFile("repo2-deletion-event.json"));
        assertThat("Call should have failed with forbiden", response.getStatusCode(), equalTo(HttpStatus.FORBIDDEN));
    }

    @Test
    public void nullSignatureMessageForbidenTest() throws IOException {
        WebHookApplication app = prepareApp();
        ResponseEntity<String> response = app.handleRequest("repository", null, loadFromFile("repo2-deletion-event.json"));
        assertThat("Call should have failed with forbiden", response.getStatusCode(), equalTo(HttpStatus.FORBIDDEN));
    }

    private WebHookApplication prepareApp() {
        WebHookApplication app = new WebHookApplication();
        MessageHandler handler = new MessageHandler() {  // a dummy message handler
            @Override
            public boolean handleMessage(String eventType, String jsonMessage) {
                return true;
            }
        };
        app.setHandler(handler);
        app.setSecret(TEST_SECRET);
        return app;
    }
}
