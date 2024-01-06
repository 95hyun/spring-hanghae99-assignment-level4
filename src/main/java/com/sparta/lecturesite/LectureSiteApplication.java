package com.sparta.lecturesite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LectureSiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(LectureSiteApplication.class, args);
    }

}
