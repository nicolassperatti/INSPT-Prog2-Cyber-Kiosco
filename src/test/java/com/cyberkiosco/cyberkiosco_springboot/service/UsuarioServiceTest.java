
package com.cyberkiosco.cyberkiosco_springboot.service;

import com.cyberkiosco.cyberkiosco_springboot.entity.Usuario;
import java.util.ArrayList;
import java.util.List;
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
public class UsuarioServiceTest {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private RolService rolService;
    
    private Usuario crearUsuarioAdminEjemplo() {
        Usuario usr = new Usuario();
        usr.setNombre("Rodrigo");
        usr.setApellido("Perez");
        usr.setMail("rodirgoPerez@email.com");
        usr.setPassword("abc123");
        usr.setFondos(8740.45);
        usr.setRol(rolService.encontrarPorId(1L)); // administrador
        
        return usr;
    }
    
    private Usuario crearUsuarioEjemplo() {
        Usuario usr = new Usuario();
        usr.setNombre("Lucia");
        usr.setApellido("Aragon");
        usr.setMail("luciaAragon@email.com");
        usr.setPassword("def456");
        usr.setFondos(12364.99);
        usr.setRol(rolService.encontrarPorId(2L)); // usuario normal
        
        return usr;
    }

    @Test
    @Order(1) //porque luego si se agregan o eliminan registros con los otros tests falla
    void testObtenerTodosLosUsuarios() {
        ArrayList<Usuario> listaUsuarios;
        listaUsuarios = (ArrayList<Usuario>) this.usuarioService.obtenerTodos();
        
        for(Usuario usr : listaUsuarios) {
            System.out.println(usr.toString());
        }
        
        assertEquals(7, listaUsuarios.size()); //en principio son 7 usuarios en total
    }
    
    @Test
    @Order(2) //porque luego si se agregan o eliminan registros con los otros tests falla
    void testContarUsuarios() {
        Long total = usuarioService.contar(); 
        
        assertEquals(7L, total);
    }
    
    @Test
    void testEncontrarPorIdExistente() {
        Usuario resultado = usuarioService.encontrarPorId(1L);
        
        assertNotNull(resultado);
    }
    
    
    @Test
    void testEncontrarPorIdNoExistente() {
        Usuario resultado = usuarioService.encontrarPorId(996L);
        
        assertNull(resultado);
    }
    
    
    @Test
    void testExistePorId() {
        Boolean resultado = usuarioService.existePorId(2L);
        
        assertTrue(resultado);
    }
    
    
    @Test
    void testNoExistePorId() {
        Boolean resultado = usuarioService.existePorId(9999L);
        
        assertFalse(resultado);
    }
    
    
    @Test
    @Order(3) //porque luego si se agregan o eliminan registros con los otros tests falla
    void testGuardarUsuario() {
        Usuario usrNuevo = this.crearUsuarioEjemplo();

        usuarioService.guardar(usrNuevo);
        
        assertTrue(usuarioService.existePorId(usrNuevo.getId()));
        assertEquals(usrNuevo, usuarioService.encontrarPorId(usrNuevo.getId()));
    }
    
    
    @Test
    @Order(4)
    void testEliminarUsuarioPorId() {
        assertTrue(usuarioService.existePorId(3L));
        
        usuarioService.eliminarPorId(3L);
        
        assertFalse(usuarioService.existePorId(3L));
    }
    
    @Test
    @Order(5)
    void testObtenerUsuariosPorRol() {
        List<Usuario> usrsAdmin;
        
        usrsAdmin = usuarioService.obtenerUsuariosPorRol_id(1L); // obtiene a los admins
        
        assertEquals(usrsAdmin.size(), 1);
        
        usuarioService.guardar(this.crearUsuarioAdminEjemplo()); // agrego un admin
        
        usrsAdmin = usuarioService.obtenerUsuariosPorRol_id(1L); // obtiene a los admins
        
        assertEquals(usrsAdmin.size(), 2);
    }
    
    @Test
    void testObtenerPorMailYPassword() {
        Usuario usr1, usr2;
        
        usr1 = usuarioService.obtenerPorMailYPassword("admin@example.com", "admin123");
        usr2 = usuarioService.obtenerPorMailYPassword("usuarioInexistente@email.com", "abc123");
        
        assertEquals(usr1, usuarioService.encontrarPorId(1L)); //se encontro al admin
        assertNull(usr2);
    }
    
}