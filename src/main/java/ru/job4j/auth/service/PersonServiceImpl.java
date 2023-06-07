package ru.job4j.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final PasswordEncoder encoder;

    @Override
    public Person save(Person person) {
        return personRepository.save(person.setPassword(encoder.encode(person.getPassword())));
    }

    @Override
    public boolean update(Person person) {
        return save(person).getId() == person.getId();
    }

    @Override
    public boolean delete(int id) {
        personRepository.delete(new Person().setId(id));
        return findById(id).isEmpty();
    }

    @Override
    public Optional<Person> findById(int id) {
        return personRepository.findById(id);
    }

    @Override
    public List<Person> findAll() {
        return personRepository.findAll();
    }

    @Override
    public Optional<Person> findByLogin(String login) {
        return personRepository.findByLogin(login);
    }
}
