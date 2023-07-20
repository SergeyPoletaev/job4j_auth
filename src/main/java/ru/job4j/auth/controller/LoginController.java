package ru.job4j.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.auth.security.model.JWTRequest;
import ru.job4j.auth.security.model.JWTResponse;
import ru.job4j.auth.security.service.AuthService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JWTResponse> login(@RequestBody @Valid JWTRequest res) {
        JWTResponse token = authService.login(res);
        return ResponseEntity.ok(token);
    }
}
