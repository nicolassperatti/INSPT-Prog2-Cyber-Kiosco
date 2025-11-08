
package com.cyberkiosco.cyberkiosco_springboot.service;

import com.cyberkiosco.cyberkiosco_springboot.entity.Rol;
import com.cyberkiosco.cyberkiosco_springboot.entity.Usuario;
import com.cyberkiosco.cyberkiosco_springboot.entity.auxiliar.Validacion;
import com.cyberkiosco.cyberkiosco_springboot.repository.UsuarioRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }
    
    public Usuario encontrarPorId(long id) {
        return usuarioRepository.findById(id).orElse(null);
    }
    
    public boolean existePorId(long id) {
        return usuarioRepository.existsById(id);
    }
    
    public void guardar(Usuario usuario) {
        usuarioRepository.save(usuario); //guarda y actualiza si ya existe
    }
    
    public void eliminarPorId(long id) {
        usuarioRepository.deleteById(id);
    }
    
    public long contar() {
        return usuarioRepository.count();
    }
    
    public List<Usuario> obtenerUsuariosPorRol_id(Long rol_id) {
        return usuarioRepository.findByRol_Id(rol_id);
    }
    
    public List<Usuario> obtenerUsuariosPorRol(Rol rol) {
        return this.obtenerUsuariosPorRol_id(rol.getId());
    }
    
    public Usuario obtenerPorMailYPassword(String email, String password) {
        
        Validacion.validarString(email, "email");
        Validacion.validarString(password, "password");
        
        return usuarioRepository.findByMailIgnoreCaseAndPassword(email, password);
    }
    
}

