package com.alvarolobato;

import org.apache.commons.codec.digest.HmacUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Server application that listens for http requests and handles webhook requests from GitHub
 */
@Controller
@EnableAutoConfiguration
@ComponentScan("com.alvarolobato")
public class WebHookApplication {

    protected final Logger logger = LoggerFactory.getLogger(WebHookApplication.class.getName());

    @Value("${secret}")
    private String secret;

    @Autowired
    private MessageHandler handler;

    @RequestMapping(name = "/webhook-client", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<String> handleRequest(@RequestHeader("X-Hub-Signature") String signature, @RequestBody String body) {

        // Check if the message is correctly signed
        String messageSha = "sha1=" + HmacUtils.hmacSha1Hex(secret, body);
        if (!messageSha.equals(signature)) {
            logger.debug("Received in valid message, signature do not match received signature '{}', expected '{}'", signature, messageSha);

            // we don't care do differentiate if signature is null or just doesn't match
            return new ResponseEntity<String>("Forbiden", HttpStatus.FORBIDDEN);
        }

        handler.handleMessage(body);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(WebHookApplication.class, args);
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setHandler(MessageHandler handler) {
        this.handler = handler;
    }
}
