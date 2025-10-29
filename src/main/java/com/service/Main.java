package com.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EnableJpaRepositories(basePackages = {"service.repository"})
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}