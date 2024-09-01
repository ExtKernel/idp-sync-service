package com.iliauni.idpsyncservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@EnableDiscoveryClient
@SpringBootApplication
public class IdpSyncServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdpSyncServiceApplication.class, args);
    }

    @Bean(destroyMethod = "shutdown")
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }
}
