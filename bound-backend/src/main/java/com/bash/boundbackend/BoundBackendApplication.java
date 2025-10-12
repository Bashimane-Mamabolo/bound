package com.bash.boundbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BoundBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoundBackendApplication.class, args);
    }

}
