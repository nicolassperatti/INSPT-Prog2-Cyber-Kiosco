package com.cyberkiosco.cyberkiosco_springboot.security;

import com.cyberkiosco.cyberkiosco_springboot.entity.Usuario;
import com.cyberkiosco.cyberkiosco_springboot.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Implementación de UserDetails para integrar Usuario con Spring Security.
 * 
 * Esta clase envuelve tu entidad Usuario (que puede ser Admin o Final)
 * para que Spring Security la entienda y pueda usarla para autenticación.
 */
public class UserDetailsImpl implements UserDetails {

    private final Usuario usuario;

    public UserDetailsImpl(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Devuelve la colección de roles/permisos.
     * 
     * Con la nueva estructura, determinamos el rol basándonos en el tipo
     * de instancia (Admin o Final) en lugar de una relación con Rol.
     * 
     * Spring Security usa estos authorities para determinar qué rutas
     * puede acceder el usuario.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Determinamos el rol basándonos en el tipo de instancia
        String authority;
        
        if (usuario instanceof Admin) {
            authority = "Administrador";
        } else {
            // Si no es Admin, asumimos que es Final (Usuario)
            authority = "Usuario";
        }
        
        // Spring Security suele usar el prefijo "ROLE_", pero funcionará
        // con el string directo si configuramos 'hasAuthority' en lugar de 'hasRole'
        return Collections.singletonList(new SimpleGrantedAuthority(authority));
    }

    @Override
    public String getPassword() {
        return usuario.getPassword(); // Devuelve la contraseña (idealmente encriptada) de la BD
    }

    @Override
    public String getUsername() {
        return usuario.getMail(); // Usamos el mail como "nombre de usuario" para el login
    }

    // Métodos de control de cuenta (puedes dejarlos en true si no manejas expiración/bloqueo)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Getter extra para poder acceder al objeto Usuario real desde el controlador si hace falta.
     * Útil cuando necesitas acceder a propiedades específicas como fondos (si es Final).
     */
    public Usuario getUsuarioReal() {
        return this.usuario;
    }
}