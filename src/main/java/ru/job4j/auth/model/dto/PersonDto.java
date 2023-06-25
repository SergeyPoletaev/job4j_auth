package ru.job4j.auth.model.dto;

import lombok.Data;
import ru.job4j.auth.model.Role;

import java.util.List;

@Data
public class PersonDto {
    private int id;
    private List<Role> rolesIds;
}
