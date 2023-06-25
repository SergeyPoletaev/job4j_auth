package ru.job4j.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.repository.PersonRepository;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
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
    public boolean patch(Person person) throws InvocationTargetException, IllegalAccessException {
        Person personDb = personRepository.findById(person.getId()).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Не найден объект для обновления с id [%s]", person.getId())));
        Map<String, Method> methodsMap = getClassMethods(person);
        for (Field field : person.getClass().getDeclaredFields()) {
            String name = field.getName();
            String rootNameMethod = name.replace(name.substring(0, 1), name.substring(0, 1).toUpperCase());
            Method getter = methodsMap.get("get" + rootNameMethod);
            Object value = getter.invoke(person);
            if (value != null && value.getClass() != ArrayList.class || value != null && !((List<?>) value).isEmpty()) {
                Method setter = methodsMap.get("set" + rootNameMethod);
                setter.invoke(personDb, value);
            }
        }
        return update(personDb);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Person> findById(int id) {
        return personRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Person> findAll() {
        return personRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Person> findByLogin(String login) {
        return personRepository.findByLogin(login);
    }

    private Map<String, Method> getClassMethods(Person person) {
        Map<String, Method> methodsMap = new HashMap<>();
        for (Method method : person.getClass().getDeclaredMethods()) {
            String name = method.getName();
            if (name.startsWith("get") || name.startsWith("set")) {
                methodsMap.put(name, method);
            }
        }
        return methodsMap;
    }
}
