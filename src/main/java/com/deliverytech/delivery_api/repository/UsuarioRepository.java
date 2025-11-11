package com.deliverytech.delivery_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery_api.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Buscar usuário por email
    Optional<Usuario> findByEmail(String email);

    // Verificar se email já existe
    boolean existsByEmail(String email);

    // Buscar usuário por email e ativo
    Optional<Usuario> findByEmailAndAtivo(String email, Boolean ativo);

}
