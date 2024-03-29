package ru.job4j.auth.security.model;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class JWTRequest {
    @NotNull
    @Size(min = 3)
    private String login;
    @NotNull
    @Size(min = 3)
    private String password;
}
