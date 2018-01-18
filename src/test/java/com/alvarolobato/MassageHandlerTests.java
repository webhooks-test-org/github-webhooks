package com.alvarolobato;

import static com.alvarolobato.TestUtils.loadFromFile;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

public class MassageHandlerTests {
    @Test
    public void hanldeDeletionMessageTest() throws IOException {
        MessageHandler handler = new MessageHandlerImpl();
        boolean processed = handler.handleMessage("repository", loadFromFile("repo2-deletion-event.json"));
        assertTrue("Messages should have been processed", processed);
    }

    @Test
    public void ignoreCreationEventTest() throws IOException {
        MessageHandler handler = new MessageHandlerImpl();
        boolean processed = handler.handleMessage("repository", loadFromFile("repo4-creation-event.json"));
        assertFalse("Message shouldn't have been processed", processed);
    }
}
