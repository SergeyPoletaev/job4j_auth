package ru.job4j.auth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import ru.job4j.auth.model.Person;

import java.util.List;

public interface PersonRepository extends CrudRepository<Person, Integer> {

    @NonNull
    List<Person> findAll();
}
