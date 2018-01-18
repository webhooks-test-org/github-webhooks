# Overview
A demostrantion of how GitHub Webhooks and API can be used to track repository deletions in GitHub organizations and creating issues in repositories.

The application is based on Spring Boot. It will start a server in the defined port, by default 8484 and listen to `/webhook-client` for webhook calls.

When a webhook it is received it will validate the signature of the message by using the shared secret and handle it only if it is correct.

Only repository deletion messages will be correctly handled, the rest will be deleted.

When a repository is deleted, it will create an issue in a defined repository, asking for a defined user to review if the deletion is correct.

# How to Build

The application build is based in Java and Maven, these are the requirements to build it:

- Java SDK 8 - https://java.com/
- Mave 3.3 or 3.5 - https://maven.apache.org/

To build the application, after checking out in your local environment, execute the following command:

`mvn package`

This will run the atomated tests and build the aplication binary

# Configuring the application
The application is configured by means of the `application.properties` file, the following parameters are available

- `server.port` - Port where the server will be listening for webhooks
- `secret` - Shared secret with the Git Hub webhooks configuration for message signature. See bellow for how to set up webhooks. This can be any string, although it is recommended to use a big string with high entropy.(e.g., by taking the output of `ruby -rsecurerandom -e 'puts SecureRandom.hex(20)'` at the terminal).
- `api.base.url` - The base url for the GitHub api, for github.com is already predefined, if using Git Hub enterprise you will have to ask your administrator.
- `api.user` - The username to be used to execute the API calls
- `api.token` - A valid user token with permissions to create issues on the `issue.repository`. See https://help.github.com/articles/creating-a-personal-access-token-for-the-command-line/
- `issue.mention` - The user to be mentioned inside the issue
- `issue.repository` - The repository where the issues will be created. By default `admin` repository-

# Running the application
After the application is configured, it can be run by executing the following command in the root of the repository:

`java -jar target/github-webhooks-1.0-SNAPSHOT.jar`

# Setting up web hooks

To configure web hooks follow the instructions in the link bellow using the following information:
https://developer.github.com/webhooks/creating/
- *Payload URL*: Notice that for the webhooks to work the computer where the application is running will need to have a publicly accesible ip (PUBLIC_ADDRESS) with the listening port opened (PORT) (by default 8484). Set the Payload URL to this url, replacing the addess and port with your own: http://PUBLIC_ADDRESS:PORT/webhook-client
- *Secret*: The secret must be the same used in the properties file.
- *Content Type*: Set it up to `application/json`
- *Events*: For our use case we only need _Repository Events_. Select _Let me select individual events._ and check only the _Repository_ option.

After saving all should be ready to go.

# Testing
To test the webhooks a repository will need to be created and deleted. After the repository is deleted, check that there was created an issue in the configured repository.

# Troubleshooting
The application will log when a web hook is received and processed. These situations could be found:
- No webhook received - Go to the webhook configurations and check next to the name if there is an error, a tool tip will be visible indicating the problem

- The issue wasn't created - Check the application logs, the most tipical case are wrong credentials and/or unexistent repository

