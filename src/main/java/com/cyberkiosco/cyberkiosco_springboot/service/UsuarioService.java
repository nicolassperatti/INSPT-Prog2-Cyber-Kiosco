package com.cyberkiosco.cyberkiosco_springboot.service;

import com.cyberkiosco.cyberkiosco_springboot.entity.Usuario;
import com.cyberkiosco.cyberkiosco_springboot.entity.Admin;
import com.cyberkiosco.cyberkiosco_springboot.entity.Final;
import com.cyberkiosco.cyberkiosco_springboot.entity.auxiliar.Validacion;
import com.cyberkiosco.cyberkiosco_springboot.repository.UsuarioRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio para gestionar usuarios.
 * 
 * Ahora maneja tanto Admin como Final usando polimorfismo.
 */
@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    /**
     * Obtiene todos los usuarios (tanto Admin como Final).
     */
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }
    
    /**
     * Busca un usuario por ID.
     * Retorna Admin o Final según corresponda.
     */
    public Usuario encontrarPorId(long id) {
        return usuarioRepository.findById(id).orElse(null);
    }
    
    /**
     * Verifica si existe un usuario con el ID dado.
     */
    public boolean existePorId(long id) {
        return usuarioRepository.existsById(id);
    }
    
    /**
     * Guarda un usuario (puede ser Admin o Final).
     * JPA automáticamente detecta el tipo y guarda en la tabla correcta.
     */
    public void guardar(Usuario usuario) {
        usuarioRepository.save(usuario); // Guarda y actualiza si ya existe
    }
    
    /**
     * Elimina un usuario por ID.
     */
    public void eliminarPorId(long id) {
        usuarioRepository.deleteById(id);
    }
    
    /**
     * Cuenta el total de usuarios (Admin + Final).
     */
    public long contar() {
        return usuarioRepository.count();
    }
    
    /**
     * Obtiene todos los usuarios de tipo Admin.
     */
    public List<Usuario> obtenerTodosAdmins() {
        return usuarioRepository.findAllAdmins();
    }
    
    /**
     * Obtiene todos los usuarios de tipo Final.
     */
    public List<Usuario> obtenerTodosFinales() {
        return usuarioRepository.findAllFinales();
    }
    
    /**
     * Busca un usuario por email.
     * Útil para autenticación.
     */
    public Usuario obtenerPorMail(String email) {
        Validacion.validarString(email, "email");
        return usuarioRepository.findByMail(email).orElse(null);
    }
    
    /**
     * Busca un usuario por email y password.
     * NOTA: En producción, usa Spring Security en lugar de esto.
     */
    public Usuario obtenerPorMailYPassword(String email, String password) {
        Validacion.validarString(email, "email");
        Validacion.validarString(password, "password");
        return usuarioRepository.findByMailIgnoreCaseAndPassword(email, password);
    }
    
    /**
     * Verifica si un usuario es de tipo Admin.
     */
    public boolean esAdmin(Usuario usuario) {
        return usuario instanceof Admin;
    }
    
    /**
     * Verifica si un usuario es de tipo Final.
     */
    public boolean esFinal(Usuario usuario) {
        return usuario instanceof Final;
    }
    
    /**
     * Obtiene los fondos de un usuario Final.
     * Lanza excepción si el usuario es Admin (no tiene fondos).
     */
    public double obtenerFondos(Usuario usuario) {
        if (usuario instanceof Final) {
            return ((Final) usuario).getFondos();
        }
        throw new IllegalArgumentException("El usuario no es de tipo Final y no tiene fondos.");
    }
    
    /**
     * Suma fondos a un usuario Final.
     * Lanza excepción si el usuario es Admin.
     */
    public void sumarFondos(Usuario usuario, double fondos) {
        if (usuario instanceof Final) {
            ((Final) usuario).sumarFondos(fondos);
            guardar(usuario); // Guarda los cambios
        } else {
            throw new IllegalArgumentException("Solo los usuarios Final pueden tener fondos.");
        }
    }
    
    /**
     * Resta fondos de un usuario Final.
     * Lanza excepción si el usuario es Admin o no tiene fondos suficientes.
     */
    public void restarFondos(Usuario usuario, double fondos) {
        if (usuario instanceof Final) {
            ((Final) usuario).restarFondos(fondos);
            guardar(usuario); // Guarda los cambios
        } else {
            throw new IllegalArgumentException("Solo los usuarios Final pueden tener fondos.");
        }
    }
}