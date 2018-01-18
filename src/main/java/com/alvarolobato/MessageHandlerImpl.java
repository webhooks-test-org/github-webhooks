package com.alvarolobato;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Component;

/**
 * Processes Webhook events, creating an issue when a repository is deleted. The rest of the events are ignored
 *
 */
@Component
public class MessageHandlerImpl implements MessageHandler {

    protected final Logger logger = LoggerFactory.getLogger(MessageHandlerImpl.class.getName());

    String REPOSITORY_EVENT_TYPE = "repository";

    String ACTION_KEY = "action";
    String DELETION_ACTION_NAME = "deleted";
    String REPOSITORY_KEY = "repository";
    String REPOSITORY_NAME_KEY = "name";
    String OWNER_KEY = "owner";
    String OWNER_LOGIN_KEY = "login";

    @Value("${issue.mention}")
    String mention;

    @Value("${issue.repository}")
    String issueRepository;

    @Autowired
    IssueCreator issueCreator;

    /* (non-Javadoc)
     * @see com.alvarolobato.MessageHandler#handleMessage(java.lang.String)
     */
    @Override
    public boolean handleMessage(String eventType, String jsonMessage) {
        Map<String, Object> map = JsonParserFactory.getJsonParser().parseMap(jsonMessage);

        // Check it is repository event
        if (!REPOSITORY_EVENT_TYPE.equals(eventType)) {
            logger.debug("Received event of type {} -> Ignored", eventType);
            return false;
        }

        // FIXME: Do proper error handling and a better json parsing
        Object receivedAction = map.get(ACTION_KEY);
        Map<String,Object> repository = (Map<String, Object>) map.get(REPOSITORY_KEY);
        Object repositoryName=repository.get(REPOSITORY_NAME_KEY);
        Object owner = repository.get(OWNER_KEY);
        Object ownerLogin = ((Map<String, Object>)owner).get(OWNER_LOGIN_KEY);

        if (DELETION_ACTION_NAME.equals(receivedAction)) {
            logger.info("Received deletion action for repository {}. Creating issue", repositoryName);
            try {
                issueCreator.createIssue(ownerLogin.toString(), issueRepository, "Repository " + repositoryName + " was deleted",
                    "Repository " + repositoryName + " was deleted,\n Please @" + mention + " validate the deletion");
            } catch (Exception e) {
                logger.error("There was an error creating the issue", e);
            }
            // TODO create the issues
            return true;
        } else {
            logger.debug("Received action {} for repository {} -> Ignored", receivedAction, repository);
            return false;
        }
    }

}
