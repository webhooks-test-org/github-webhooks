package com.alvarolobato;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Component;

/**
 * Processes Webhook events, creating an issue when a repository is deleted. The rest of the events are ignored
 *
 */
@Component
public class MessageHandlerImpl implements MessageHandler {

    protected final Logger logger = LoggerFactory.getLogger(MessageHandlerImpl.class.getName());

    String ACTION_KEY = "action";
    String DELETION_ACTION_NAME = "deleted";
    String REPOSITORY_KEY = "repository";
    String REPOSITORY_NAME_KEY = "name";

    /* (non-Javadoc)
     * @see com.alvarolobato.MessageHandler#handleMessage(java.lang.String)
     */
    @Override
    public boolean handleMessage(String jsonMessage) {
        Map<String, Object> map = JsonParserFactory.getJsonParser().parseMap(jsonMessage);

        // FIXME: Do proper error handling and a better json parsing
        Object receivedAction = map.get(ACTION_KEY);
        Object repository = ((Map<String, Object>) map.get(REPOSITORY_KEY)).get(REPOSITORY_NAME_KEY);

        if (DELETION_ACTION_NAME.equals(receivedAction)) {
            logger.info("Received deletion action for repository {}. Processing request");
            // TODO create the issues
            return true;
        } else {
            logger.debug("Received action {} for repository {} -> Ignored", receivedAction, repository);
            return false;
        }
    }
}
