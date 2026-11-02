package com.auth3.authteste3.service;

import com.auth3.authteste3.dto.AuthResponse;
import com.auth3.authteste3.dto.UsuarioResponse;
import com.auth3.authteste3.dto.LoginRequest;
import com.auth3.authteste3.dto.RegisterRequest;
import com.auth3.authteste3.model.UsuariosModels;
import com.auth3.authteste3.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService; // NOVO: para gerar o token

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UsuarioResponse register(RegisterRequest request){
        String email = normalizeEmail(request.getEmail());

        if(repository.existsByEmail(email)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado");
        }

        UsuariosModels user = new UsuariosModels();
        user.setEmail(email);
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        UsuariosModels saved = repository.save(user);
        return new UsuarioResponse(saved.getId(), saved.getEmail(),saved.getName());
    }

    public AuthResponse  login(LoginRequest request){
        String email = normalizeEmail(request.getEmail());

        UsuariosModels user = repository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais invalidas"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais invalidas");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token,
                new UsuarioResponse(user.getId(), user.getEmail(), user.getName()));
    }
    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }
}
