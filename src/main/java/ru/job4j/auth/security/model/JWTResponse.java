package ru.job4j.auth.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JWTResponse {
    private String token;
}
