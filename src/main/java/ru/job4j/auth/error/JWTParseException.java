package ru.job4j.auth.error;

public class JWTParseException extends RuntimeException {

    public JWTParseException(String message) {
        super(message);
    }
}
