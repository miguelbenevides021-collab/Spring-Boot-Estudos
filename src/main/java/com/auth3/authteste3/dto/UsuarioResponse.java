package com.auth3.authteste3.dto;

public class UsuarioResponse {
    private Long id;
    private String email;
    private String name;

    public UsuarioResponse(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
