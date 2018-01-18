package com.alvarolobato;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Component
public class IssueCreator {

    private final Logger logger = LoggerFactory.getLogger(IssueCreator.class.getName());

    @Value("${api.base.url}")
    private String baseApiUrl;

    @Value("${api.user}")
    private String user;

    @Value("${api.token}")
    private String token;

    private HttpHeaders headers;

    private RestTemplate restTemplate;

    public IssueCreator(){
        restTemplate = new RestTemplate();
    }

    /**
     * Creates an issue
     *
     * @param owner of the repository to create it
     * @param repository to create the issue in
     * @param title of the issue
     * @param body of the issue
     * @return the issue number
     * @throws Exception in case of error
     */
    public String createIssue(String owner, String repository, String title, String body) throws Exception {

        if (headers == null) {
            headers = createAuthHeader(user, token);
        }
        // Prepare the issue to be created
        Issue issue = new Issue(title, body);
        // Headers need to be injected
        HttpEntity<Issue> request = new HttpEntity<Issue>(issue, headers);

        // Execute the call
        URI uri = buildIssuesUri(owner, repository);
        logger.debug("Creating issue against " + uri + " data: " + issue + " user:" + user);

        ResponseEntity<Issue> issueResponse = restTemplate.exchange(uri, HttpMethod.POST, request, Issue.class);

        // Check if it was created
        if (issueResponse.getStatusCode() != HttpStatus.CREATED) {
            throw new Exception("Error creating issue, response code " + issueResponse.getStatusCode());
        }

        issue = issueResponse.getBody();
        logger.info("Issue " + issue.getNumber() + " created in repository " + repository);
        return issue.getNumber();
    }

    public HttpHeaders createAuthHeader(String user, String token) {
        String plainCreds = user + ":" + token;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        return headers;
    }

    public URI buildIssuesUri(String owner, String repository) throws URISyntaxException {
        return new URI(baseApiUrl + "repos/" + owner + "/" + repository + "/issues");
    }

    /**
     * Models an issue for the rest client
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Issue {
        private String number;
        private String title;
        private String body;

        public Issue() {

        }

        public Issue(String title, String body) {
            this.title = title;
            this.body = body;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String toString() {
            return "[" + number + "] - " + title + " / " + body;
        }
    }
}
