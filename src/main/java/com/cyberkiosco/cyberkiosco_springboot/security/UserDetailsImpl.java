package com.cyberkiosco.cyberkiosco_springboot.security;

import com.cyberkiosco.cyberkiosco_springboot.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

// Esta clase envuelve tu entidad Usuario para que Spring Security la entienda
public class UserDetailsImpl implements UserDetails {

    private final Usuario usuario;

    public UserDetailsImpl(Usuario usuario) {
        this.usuario = usuario;
    }

    // Devuelve la colección de roles/permisos.
    // Convertimos tu entidad 'Rol' a un 'GrantedAuthority' que Spring entiende.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Asumimos que tu Rol tiene un nombre como "Administrador" o "Usuario"
        // Spring Security suele usar el prefijo "ROLE_", pero funcionará con el string directo si configuramos 'hasAuthority'
        return Collections.singletonList(new SimpleGrantedAuthority(usuario.getRol().getNombre()));
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
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    // Getter extra para poder acceder al objeto Usuario real desde el controlador si hace falta
    public Usuario getUsuarioReal() {
        return this.usuario;
    }
}