package com.reader.manga.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.reader.manga.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JwtTokenService {

    @Value("${reader.jwt.secret}")
    private String secret;

    /**
     * Responsável por gerar o token.
     * @param user que está tentando realizar o login.
     * @return token.
     */
    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("reader-manga")
                    .withClaim("id", user.getId())
                    .withClaim("role", user.getRoles())
                    .withSubject(user.getEmail())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new JWTCreationException("Erro ao criar o token", e);
        }
    }

    /**
     * Verifica se o token está válido, tanto em questão de expiração, quanto se foi gerado pela aplicação.
     * @param token a ser válidado.
     */
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer("reader-manga")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("Erro ao verificar o token", e);
        }
    }

    /**
     * Defini a expiração do token para 2 horas após sua criação.
     * @return Instant da expiração do token.
     */
    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
