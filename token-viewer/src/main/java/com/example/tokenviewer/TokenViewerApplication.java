package com.example.tokenviewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class TokenViewerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TokenViewerApplication.class, args);
    }

    @RestController
    static class TokenController {

        @GetMapping("/token")
        String showToken(@RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient client) {
            return "export TOKEN='" + client.getAccessToken().getTokenValue() + "'";
        }
    }
}
