package com.kakaopay.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ThrowMoneyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThrowMoneyApiApplication.class, args);
    }
}
