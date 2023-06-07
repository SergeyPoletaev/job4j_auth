package ru.job4j.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.security.RoleTypes;
import ru.job4j.auth.service.PersonService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;

    @GetMapping("/")
    @PreAuthorize(RoleTypes.READER)
    public List<Person> findAll() {
        return personService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize(RoleTypes.READER)
    public ResponseEntity<Person> findById(@PathVariable int id) {
        Optional<Person> person = personService.findById(id);
        return new ResponseEntity<>(person.orElse(new Person()),
                person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PutMapping("/")
    @PreAuthorize(RoleTypes.EDITOR)
    public ResponseEntity<Void> update(@RequestBody Person person) {
        return personService.update(person)
                ? ResponseEntity.ok().build() : ResponseEntity.internalServerError().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(RoleTypes.ADMIN)
    public ResponseEntity<Void> delete(@PathVariable int id) {
        return personService.delete(id)
                ? ResponseEntity.ok().build() : ResponseEntity.internalServerError().build();
    }
}
