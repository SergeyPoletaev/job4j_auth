package ru.job4j.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.service.PersonService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class RegController {
    private final PersonService personService;

    @PostMapping("/sign-up")
    public ResponseEntity<Person> save(@RequestBody @Valid Person person) {
        return new ResponseEntity<>(personService.save(person), HttpStatus.CREATED);
    }
}
