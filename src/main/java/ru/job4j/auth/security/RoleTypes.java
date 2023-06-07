package ru.job4j.auth.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleTypes {
    public static final String HIERARCHY = """
            ROLE_ADMIN > ROLE_EDITOR
            ROLE_EDITOR > ROLE_READER
            """;

    public static final String ADMIN = "hasRole('ROLE_ADMIN')";
    public static final String READER = "hasRole('ROLE_READER')";
    public static final String EDITOR = "hasRole('ROLE_EDITOR')";

}
