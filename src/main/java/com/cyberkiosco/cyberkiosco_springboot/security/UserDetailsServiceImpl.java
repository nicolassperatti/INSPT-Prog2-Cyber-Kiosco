package com.cyberkiosco.cyberkiosco_springboot.security;

import com.cyberkiosco.cyberkiosco_springboot.entity.Usuario;
import com.cyberkiosco.cyberkiosco_springboot.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementación de UserDetailsService para Spring Security.
 * 
 * Spring Security llama a este servicio cuando un usuario intenta autenticarse.
 * Busca el usuario en la BD y lo envuelve en UserDetailsImpl.
 * 
 * Con la nueva estructura, funciona igual: busca por mail y retorna
 * el usuario (que puede ser Admin o Final) envuelto en UserDetailsImpl.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Spring Security llama a este método cuando un usuario intenta autenticarse.
     * 
     * @param username El email del usuario (configurado en WebSecurityConfig)
     * @return UserDetails con la información del usuario
     * @throws UsernameNotFoundException Si el usuario no existe
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Spring Security busca solo por mail primero, y él mismo chequea la password después.
        
        Usuario usuario = usuarioRepository.findByMail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + username));

        // Devuelvo la implementación que cree
        // UserDetailsImpl determinará si es Admin o Final y asignará el authority correspondiente
        return new UserDetailsImpl(usuario);
    }
}