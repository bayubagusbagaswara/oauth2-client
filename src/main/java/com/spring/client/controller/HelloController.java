package com.spring.client.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.Principal;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@RestController
public class HelloController {

    private final WebClient webClient;

    public HelloController(WebClient webClient) {
        this.webClient = webClient;
    }

    // ketika client mengakses URL /api/hello maka akan ditampilkan string seperti dibawah
    @GetMapping("/api/hello")
    public String hello(Principal principal) {
        return String.format("Hello %s, Welcome to Daily Code Buffer", principal.getName());
    }

    @GetMapping("/api/users")
    public String[] users(@RegisteredOAuth2AuthorizedClient("api-client-authorization-code") OAuth2AuthorizedClient client) {
        return this.webClient
                .get()
                .uri("http://127.0.0.1:8090/api/users")
                .attributes(oauth2AuthorizedClient(client))
                .retrieve()
                .bodyToMono(String[].class)
                .block();
    }

}
