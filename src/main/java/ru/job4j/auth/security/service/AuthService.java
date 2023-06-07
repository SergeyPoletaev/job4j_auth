package ru.job4j.auth.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.job4j.auth.error.AuthException;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.model.Role;
import ru.job4j.auth.security.AuthorizationProperties;
import ru.job4j.auth.security.model.JWTRequest;
import ru.job4j.auth.security.model.JWTResponse;
import ru.job4j.auth.security.util.AuthHelper;
import ru.job4j.auth.service.PersonService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {
    public static final long EXPIRATION_TIME = 864_000_000; /* 10 days */
    public static final String TOKEN_PREFIX = "Bearer ";

    private final PersonService personService;
    private final PasswordEncoder passwordEncoder;
    private final AuthorizationProperties properties;
    private Algorithm algorithm;

    public JWTResponse login(JWTRequest res) {
        Person person = personService.findByLogin(res.getLogin())
                .orElseThrow(() -> new AuthException("User not found"));
        boolean passChecker = passwordEncoder.matches(res.getPassword(), person.getPassword());
        if (!passChecker) {
            throw new AuthException("Incorrect password");
        }
        return new JWTResponse(generateToken(person));
    }

    private String generateToken(Person person) {
        return TOKEN_PREFIX + JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withIssuer(properties.getIssuer())
                .withClaim("roles", person.getRole().stream()
                        .map(Role::getName)
                        .toList())
                .withClaim("id", person.getId())
                .sign(algorithm);
    }

    @PostConstruct
    private void init() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        ClassPathResource resource = new ClassPathResource(properties.getPrivateKey());
        byte[] keyContentAsBytes = AuthHelper.readFromResource(resource);
        PKCS8EncodedKeySpec rsaPrivateKeySpec = new PKCS8EncodedKeySpec(keyContentAsBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey rsaPrivateKey = keyFactory.generatePrivate(rsaPrivateKeySpec);
        this.algorithm = Algorithm.RSA256((RSAPrivateKey) rsaPrivateKey);
    }

}
