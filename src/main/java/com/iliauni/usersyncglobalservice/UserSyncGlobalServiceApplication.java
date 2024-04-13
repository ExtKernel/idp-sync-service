package com.iliauni.usersyncglobalservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class UserSyncGlobalServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserSyncGlobalServiceApplication.class, args);
    }

}
