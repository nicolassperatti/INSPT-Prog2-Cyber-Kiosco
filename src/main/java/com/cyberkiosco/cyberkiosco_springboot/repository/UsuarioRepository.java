package com.cyberkiosco.cyberkiosco_springboot.repository;

import com.cyberkiosco.cyberkiosco_springboot.entity.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Usuario (clase base).
 * 
 * Con JOINED strategy, las consultas a Usuario automáticamente
 * hacen JOIN con las tablas admin y final según el tipo.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    /**
     * Busca un usuario por su email (case-insensitive).
     * Funciona tanto para Admin como Final.
     */
    Optional<Usuario> findByMail(String mail);
    
    /**
     * Busca un usuario por email y password.
     * Útil para autenticación manual (aunque Spring Security lo maneja mejor).
     */
    Usuario findByMailIgnoreCaseAndPassword(String email, String password);
    
    
}