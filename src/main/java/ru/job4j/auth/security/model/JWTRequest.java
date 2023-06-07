package ru.job4j.auth.security.model;

import lombok.Getter;

@Getter
public class JWTRequest {
    private String login;
    private String password;
}
