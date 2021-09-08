package com.xl.learn.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApplication {
    private final static Logger LOGGER = LoggerFactory.getLogger(ServerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
