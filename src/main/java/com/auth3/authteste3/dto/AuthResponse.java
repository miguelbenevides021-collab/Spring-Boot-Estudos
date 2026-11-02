package com.auth3.authteste3.dto;

// Resposta do LOGIN: devolve o token + os dados do usuário
public class AuthResponse {

    // O "crachá" que o cliente vai mandar nas próximas requisições
    private final String token;

    // Dados do usuário (id, email, name), sem senha
    private final UsuarioResponse user;

    public AuthResponse(String token, UsuarioResponse user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() { return token; }
    public UsuarioResponse getUser() { return user; }
}