package com.estudo_raul.java.infrastructure.repository;


import com.estudo_raul.java.infrastructure.entity.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmail(String email);

   Optional <Usuario> findByEmail(String email);

   @Transactional
    void deleteByEmail(String email);
}
