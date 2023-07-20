package ru.job4j.auth.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @Size(min = 3)
    private String login;
    @NotNull
    @Size(min = 3)
    private String password;
    @ManyToMany
    @JoinTable(
            name = "person_role",
            joinColumns = {@JoinColumn(name = "person_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private List<Role> role = new ArrayList<>();
}
