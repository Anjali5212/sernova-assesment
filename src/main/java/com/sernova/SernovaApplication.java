package com.sernova;

import com.sernova.domain.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SernovaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SernovaApplication.class, args);
    }

    @Bean
    CommandLineRunner init(PersonRepository repo) {
        return args -> {
            long count = repo.count();
            System.out.println("Persons in DB: " + count);
        };
    }
}
