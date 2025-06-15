package com.example.competition_portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.competition_portal.entity")
@ComponentScan(basePackages = {
        "com.example.competition_portal",
        "com.example.competition_portal.controller",
        "com.example.competition_portal.config"
})
public class CompetitionPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompetitionPortalApplication.class, args);
    }

}