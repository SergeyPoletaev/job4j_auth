package ru.job4j.auth.model.dto;

import lombok.Data;

@Data
public class PersonCredentialsDto {
    private String login;
    private String password;
}
