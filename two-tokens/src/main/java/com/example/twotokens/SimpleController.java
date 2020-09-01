package com.example.twotokens;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {

    @GetMapping("/")
    String hello() {
        return "Hello!";
    }

    @PostMapping("/")
    String helloPost(@RequestParam("message") String message) {
        return "hello: " + message;
    }
}
