package com.kaviddiss.bootquartz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.kaviddiss.bootquartz.config.SchedulerConfiguration;

@SpringBootApplication
@Import({SchedulerConfiguration.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
