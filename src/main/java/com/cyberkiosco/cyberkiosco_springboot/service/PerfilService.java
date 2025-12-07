
package com.cyberkiosco.cyberkiosco_springboot.service;

import com.cyberkiosco.cyberkiosco_springboot.entity.Perfil;
import com.cyberkiosco.cyberkiosco_springboot.entity.Usuario;
import com.cyberkiosco.cyberkiosco_springboot.repository.PerfilRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PerfilService {
    
    @Autowired
    private PerfilRepository perfilRepository;
    
    @Autowired
    private UsuarioService usuarioService;
    
    public List<Perfil> obtenerTodos() {
        return perfilRepository.findAll();
    }
    
    public Perfil encontrarPorId(long id) {
        return perfilRepository.findById(id).orElse(null);
    }
    
    public boolean existePorId(long id) {
        return perfilRepository.existsById(id);
    }
    
    public void guardar(Perfil perfil) {
        perfilRepository.save(perfil); //guarda y actualiza si ya existe
    }
    
    public void eliminarPorId(long id) {
        perfilRepository.deleteById(id);
    }
    
    public long contar() {
        return perfilRepository.count();
    }
    
    public Perfil encontrarPorId_usuario(long id_usuario) {
        Usuario usr = usuarioService.encontrarPorId(id_usuario);
        
        if(usr == null) {
            throw new IllegalArgumentException("El usuario cuyo perfil hay que buscar no existe.");
        }
        
        return encontrarPorUsuario(usr);
    }
    
    public Perfil encontrarPorUsuario(Usuario usr) {
        Optional<Perfil> perfil = perfilRepository.findByUsuario(usr);
        
        if(perfil.isEmpty()) {
            throw new IllegalArgumentException("El perfil a buscar no existe.");
        }

        return (Perfil) perfil.get();
    }
    
    
}

