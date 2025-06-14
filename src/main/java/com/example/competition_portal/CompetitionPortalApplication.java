package com.example.competition_portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.competition_portal.entity")
public class CompetitionPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompetitionPortalApplication.class, args);
    }

}