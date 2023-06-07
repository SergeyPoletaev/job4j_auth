package ru.job4j.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.job4j.auth.security.AuthorizationProperties;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties(value = AuthorizationProperties.class)
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
        log.info("Rest API service job4j_auth is started ...");
    }
}
