package com.auth3.authteste3.repository;

import com.auth3.authteste3.model.UsuariosModels;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuariosModels, Long> {
    Optional<UsuariosModels> findByEmail(String email);
    boolean existsByEmail(String email);
}
