package com.alvarolobato;

/**
 * Responsible for processing messages, will return true if the message was processed.
 */
public interface MessageHandler {

    /**
     * Does the actual handling of the message
     *
     * @param eventType the type of the event
     * @param jsonMessage to be processed
     * @return true if the message was processed, false if it was ignored.
     */
    boolean handleMessage(String eventType, String jsonMessage);
}
