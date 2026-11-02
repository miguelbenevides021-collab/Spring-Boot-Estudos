package com.auth3.authteste3.controller;

import com.auth3.authteste3.dto.AuthResponse;
import com.auth3.authteste3.dto.UsuarioResponse;
import com.auth3.authteste3.dto.LoginRequest;
import com.auth3.authteste3.dto.RegisterRequest;
import com.auth3.authteste3.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UsuarioService service;

    public AuthController(UsuarioService usuarioService) {
        this.service = usuarioService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse register(@Valid @RequestBody RegisterRequest request) {
        return service.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return service.login(request);
    }
}
