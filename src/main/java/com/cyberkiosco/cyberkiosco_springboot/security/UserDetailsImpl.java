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
 * Esta clase envuelve la entidad Usuario (que puede ser Admin o Final)
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
     * determino el rol basándonos en el tipo
     * de instancia (Admin o Final).
     * 
     * Spring Security usa estos authorities para determinar qué rutas
     * puede acceder el usuario.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Determino el rol basándome en el tipo de instancia
        String authority;
        
        if (usuario instanceof Admin) {
            authority = "Administrador";
        } else {
            // Si no es Admin, asumo que es Final (Usuario)
            authority = "Usuario";
        }
        return Collections.singletonList(new SimpleGrantedAuthority(authority));
    }

    @Override
    public String getPassword() {
        return usuario.getPassword(); // Devuelve la contraseña de la BD
    }

    @Override
    public String getUsername() {
        return usuario.getMail(); // Usa el mail como "nombre de usuario" para el login
    }

    // Métodos de control de cuenta
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