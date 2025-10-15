package com.bash.boundbackend;

import com.bash.boundbackend.entity.role.Role;
import com.bash.boundbackend.repository.RoleRepository;
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
@EnableJpaAuditing
@OpenAPIDefinition(
        info = @Info(
                title = "Book Social CLUB API",
                version = "1.0",
                description = "REST API for managing book sharing and social interactions.",
                contact = @Contact(
                        name = "Bashimane Mamabolo",
                        email = "bash@gmail.com",
                        url = "https://github.com/Bashimane-Mamabolo"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        )
)
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
