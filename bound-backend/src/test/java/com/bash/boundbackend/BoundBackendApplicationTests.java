package com.bash.boundbackend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class BoundBackendApplicationTests {

    @Test
    void contextLoads() {
    }

    @Configuration
    static class TestMailSenderConfiguration {

        @Bean
        @Primary // Ensures this bean is chosen over any auto-configured one (if present)
        public JavaMailSender javaMailSender() {
            // Return a simple, non-functional mock sender for testing context loading
            return new JavaMailSenderImpl();
        }
    }

}
