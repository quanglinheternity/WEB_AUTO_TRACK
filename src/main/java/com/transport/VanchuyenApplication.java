package com.transport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VanchuyenApplication {

    public static void main(String[] args) {
        SpringApplication.run(VanchuyenApplication.class, args);
    }
}
