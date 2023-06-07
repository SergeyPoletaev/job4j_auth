package ru.job4j.auth.error;

public class AuthException extends RuntimeException {

    public AuthException(String message) {
        super(message);
    }
}
