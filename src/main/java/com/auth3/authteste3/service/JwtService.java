package com.auth3.authteste3.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

// Responsável por duas coisas: CRIAR o token e LER o token
@Service
public class JwtService {

    // Chave usada para assinar e verificar os tokens
    private final SecretKey key;

    // Quanto tempo o token vale (em milissegundos)
    private final long expirationMs;

    // @Value pega os valores do application.properties e injeta aqui
    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.expiration-ms}") long expirationMs) {
        // Converte o texto Base64 da variável de ambiente em uma chave de verdade
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expirationMs = expirationMs;
    }

    // CRIA o token a partir do e-mail do usuário
    public String generateToken(String email) {
        Date now = new Date();
        return Jwts.builder()
                .subject(email)                                          // quem é o dono do token
                .issuedAt(now)                                           // quando foi criado
                .expiration(new Date(now.getTime() + expirationMs))      // quando expira
                .signWith(key)                                           // assina com a chave secreta
                .compact();                                              // gera o texto final "xxxxx.yyyyy.zzzzz"
    }

    // LÊ o token e devolve o e-mail que está dentro dele.
    // Se o token for falso, alterado ou expirado, lança JwtException.
    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(key)              // confere se a assinatura bate com a nossa chave
                .build()
                .parseSignedClaims(token)     // abre o token (aqui ele valida assinatura e validade)
                .getPayload()
                .getSubject();                // devolve o e-mail guardado em "subject"
    }
}