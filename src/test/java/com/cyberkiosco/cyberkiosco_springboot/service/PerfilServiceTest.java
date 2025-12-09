/*package com.cyberkiosco.cyberkiosco_springboot.service;

import com.cyberkiosco.cyberkiosco_springboot.entity.Perfil;
import com.cyberkiosco.cyberkiosco_springboot.entity.Usuario;
import com.cyberkiosco.cyberkiosco_springboot.service.PerfilService;
import com.cyberkiosco.cyberkiosco_springboot.service.RolService;
import com.cyberkiosco.cyberkiosco_springboot.service.UsuarioService;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")  // Activa la configuración de H2
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class PerfilServiceTest {
    
    @Autowired
    private PerfilService perfilService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private RolService rolService;
    
    
    private Usuario crearUsuarioEjemplo() {
        Usuario usr = new Usuario();
        usr.setNombre("Rodrigo");
        usr.setApellido("Perez");
        usr.setMail("rodirgoPerez@email.com");
        usr.setPassword("abc123");
        usr.setFondos(8740.45);
        usr.setRol(rolService.encontrarPorId(1L)); // administrador
        
        return usr;
    }
    
    private Perfil crearPerfilEjemplo(Usuario usr) {
        Perfil perfil = new Perfil();
        perfil.setNombre("rodri14386");
        perfil.setFoto("rodri14386.png");
        perfil.setUsuario(usr);
        
        return perfil;
    }
    
    

    @Test
    @Order(1) //porque luego si se agregan o eliminan registros con los otros tests falla
    void testObtenerTodosLosPerfiles() {
        ArrayList<Perfil> listaPerfiles;
        listaPerfiles = (ArrayList<Perfil>) this.perfilService.obtenerTodos();
        
        for(Perfil perfil : listaPerfiles) {
            System.out.println(perfil.toString());
        }
        
        assertEquals(7, listaPerfiles.size()); //en principio son 7 perfiles en total
    }
    
    @Test
    @Order(2) //porque luego si se agregan o eliminan registros con los otros tests falla
    void testContarPerfiles() {
        Long total = perfilService.contar(); 
        
        assertEquals(7L, total);
    }
    
     @Test
    @Order(3) //porque luego si se agregan o eliminan registros con los otros tests falla
    void testGuardarPerfil() {
        Long idUsrNuevo, idPerfilNuevo;
        
        Usuario usrNuevo = this.crearUsuarioEjemplo();
        Perfil perfilNuevo = this.crearPerfilEjemplo(usrNuevo);

        usuarioService.guardar(usrNuevo);
        perfilService.guardar(perfilNuevo);
        
        idUsrNuevo = usrNuevo.getId();
        idPerfilNuevo = perfilNuevo.getId();
        
        //prueba si existe usuario
        assertTrue(usuarioService.existePorId(idUsrNuevo));
        assertEquals(usrNuevo, usuarioService.encontrarPorId(idUsrNuevo));
        
        //prueba si existe perfil
        assertTrue(perfilService.existePorId(idPerfilNuevo));
        assertEquals(perfilNuevo, perfilService.encontrarPorId(idPerfilNuevo));
        
        //prueba acceder al perfil desde el usuario
        usrNuevo = usuarioService.encontrarPorId(idUsrNuevo);
        assertEquals(perfilService.encontrarPorId_usuario(usrNuevo.getId()), perfilNuevo);
    }
    
    
    @Test
    @Order(4)
    void testEliminarPerfilPorId() {
        assertTrue(perfilService.existePorId(3L));
        
        perfilService.eliminarPorId(3L);
        
        assertFalse(perfilService.existePorId(3L));
    }
    
    
    @Test
    void testEncontrarPorIdExistente() {
        Perfil resultado = perfilService.encontrarPorId(1L);
        
        assertNotNull(resultado);
    }
    
    
    @Test
    void testEncontrarPorIdNoExistente() {
        Perfil resultado = perfilService.encontrarPorId(996L);
        
        assertNull(resultado);
    }
    
    
    @Test
    void testExistePorId() {
        Boolean resultado = perfilService.existePorId(2L);
        
        assertTrue(resultado);
    }
    
    
    @Test
    void testNoExistePorId() {
        Boolean resultado = perfilService.existePorId(9999L);
        
        assertFalse(resultado);
    }
    
}

*/