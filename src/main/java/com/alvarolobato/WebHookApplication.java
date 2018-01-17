package com.alvarolobato;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

/**
 * Server application that listens for http requests
 */
@Controller
@EnableAutoConfiguration
public class WebHookApplication {

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(WebHookApplication.class, args);
    }
}