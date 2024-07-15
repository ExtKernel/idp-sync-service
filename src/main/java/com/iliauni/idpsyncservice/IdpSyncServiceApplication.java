package com.iliauni.idpsyncservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class IdpSyncServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdpSyncServiceApplication.class, args);
    }

}
