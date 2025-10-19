package com.bash.boundbackend;

import com.bash.boundbackend.modules.auth.entity.Role;
import com.bash.boundbackend.modules.auth.repository.RoleRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")

@EnableAsync
public class BoundBackendApplication {

    public static void main(String[] args) {

        SpringApplication.run(BoundBackendApplication.class, args);


    }

    @Bean
    public CommandLineRunner runner(RoleRepository roleRepository) {
        return  args -> {
            if (roleRepository.findByName("USER").isEmpty()) {
                roleRepository.save(Role.builder().name("USER").build());
            }
        };

    }

}
