package com.storyhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Entry point. Run this class (or `mvn spring-boot:run`) to start StoryHub. */
@SpringBootApplication
public class StoryHubApplication {
    public static void main(String[] args) {
        SpringApplication.run(StoryHubApplication.class, args);
    }
}
