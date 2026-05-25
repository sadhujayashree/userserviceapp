package dev.jaya.userservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public String health() {
        return "User Service is deploying and running automatically via CI/CD also checked via sonarqube";
    }
}
