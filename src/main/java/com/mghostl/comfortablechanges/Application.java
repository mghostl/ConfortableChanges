package com.mghostl.comfortablechanges;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableAsync
@ComponentScan
public class Application {

    public static void main(String[] args) {
       SpringApplication.run(Application.class, args);
    }

}
