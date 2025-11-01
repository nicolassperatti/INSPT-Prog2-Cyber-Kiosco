package com.cyberkiosco.cyberkiosco_springboot.service;

import com.cyberkiosco.cyberkiosco_springboot.entity.Carrito;
import com.cyberkiosco.cyberkiosco_springboot.entity.Usuario;
import com.cyberkiosco.cyberkiosco_springboot.repository.CarritoRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.List;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")  // Activa la configuración de H2
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class CarritoServiceTest {
    
    @Autowired
    private CarritoService carritoService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private CarritoRepository carritoRepository;

    
    private Carrito crearCarritoEjemplo1() {
        Carrito carrito = new Carrito();
        carrito.setPrecio_total(45.50);
        carrito.setUsuario(usuarioService.encontrarPorId(1L));
        carrito.setFecha_compra(LocalDateTime.of(2025, 10, 1, 14, 30));
        
        return carrito;
    }

    private Carrito crearCarritoEjemplo2() {        
        Carrito carrito = new Carrito();
        carrito.setPrecio_total(22.30);
        carrito.setUsuario(usuarioService.encontrarPorId(2L));
        carrito.setFecha_compra(LocalDateTime.of(2025, 10, 5, 10, 0));
        
        return carrito;
    }

    private Carrito crearCarritoEjemplo3() {
        Carrito carrito = new Carrito();
        carrito.setPrecio_total(89.90);
        carrito.setUsuario(usuarioService.encontrarPorId(3L));
        carrito.setFecha_compra(LocalDateTime.of(2025, 10, 12, 18, 45));
        
        return carrito;
    }
    
    
    // Los tests se hacen en orden aleatorio excepto por el uso de @Order

    @Test
    @Order(1) //porque luego si se agregan o eliminan registros con los otros tests falla
    void testObtenerTodosLosCarritos() {
        ArrayList<Carrito> listaCarritos;
        listaCarritos = (ArrayList<Carrito>) this.carritoService.obtenerTodosLosCarritos();
        
        for(Carrito carrito : listaCarritos) {
            System.out.println(carrito.toString());
        }
        
        assertEquals(10, listaCarritos.size()); //en principio son 10 carritos en total
    }
    
    @Test
    @Order(2) //porque luego si se agregan o eliminan registros con los otros tests falla
    void testContarCarritos() {
        Long total = carritoService.contarCarritos(); 
        
        assertEquals(10L, total);
    }

    @Test
    @Order(3)
    void obtenerListaCarritosDeUsuario() {
        List<Carrito> listaCarritos;
        Usuario usr = usuarioService.encontrarPorId(2L);
        
        listaCarritos = carritoService.encontrarPorId_usuario(usr.getId());
        
        assertEquals(2, listaCarritos.size()); //el usuario con id = 2 tiene 2 carritos en principio
        
        //verificar que todos los carritos pertenezcan al usuario correcto
        for(int i = 0; i < listaCarritos.size(); i++) {
            assertEquals(usr, listaCarritos.get(i).getUsuario());
        }
    }
    
    @Test
    @Order(4)
    void obtenerCarritoAbiertoPorUsuarioExistente() {
        Usuario usr;
        Carrito car1, car2;
        
        usr = usuarioService.encontrarPorId(1L);
        
        //crear carrito abierto
        car1 = carritoService.obtenerCarritoAbiertoPorUsuario(usr);        
        
        //la misma funcion lo deveria devolver ya que ahora existe uno abierto
        car2 = carritoService.obtenerCarritoAbiertoPorUsuario(usr);
                
        assertEquals(car1, car2);
    }
    
    
    @Test
    @Order(5)
    void obtenerCarritoAbiertoPorUsuarioNuevo() {
        Usuario usr;
        Carrito car1, car2;
        int cantidadCarritos;
        List<Carrito> listaCarritos;
        
        usr = usuarioService.encontrarPorId(2L);
        
        car1 = carritoService.obtenerCarritoAbiertoPorUsuario(usr);
        
        listaCarritos = carritoRepository.findAllByUsuarioAndFechaCompraIsNull(usr);
                
        car2 = listaCarritos.get(0);
        
        cantidadCarritos = listaCarritos.size();
        
        assertEquals(car1, car2);
        assertEquals(cantidadCarritos, 1);
    }
    
    
    @Test
    void testEncontrarPorIdExistente() {
        Carrito resultado = carritoService.encontrarPorId(3L);
        
        assertNotNull(resultado);
    }
    
    
    @Test
    void testEncontrarPorIdNoExistente() {
        Carrito resultado = carritoService.encontrarPorId(9999L);
        
        assertNull(resultado);
    }
    
    
    @Test
    void testExistePorId() {
        Boolean resultado = carritoService.existePorId(2L);
        
        assertTrue(resultado);
    }
    
    
    @Test
    void testNoExistePorId() {
        Boolean resultado = carritoService.existePorId(9999L);
        
        assertFalse(resultado);
    }
    
    
    @Test
    void testGuardarCarrito() {
        Carrito carrito = crearCarritoEjemplo1();

        carritoService.guardarCarrito(carrito);
        
        assertTrue(carritoService.existePorId(carrito.getId()));
        assertTrue(carrito.equals(carritoService.encontrarPorId(carrito.getId())));
    }
    
    
    @Test
    void testEliminarCarritoPorId() {
        carritoService.eliminarCarritoPorId(1L);
        
        assertFalse(carritoService.existePorId(1L));
    }
    
    
    @Test
    void testGuardarListaCarritos() {
        List<Carrito> carritos = List.of(
                crearCarritoEjemplo2(),
                crearCarritoEjemplo3()
        );

        Long totalAntes = carritoService.contarCarritos();
        
        carritoService.guardarListaCarritos(carritos);
        
        assertTrue(carritoService.existePorId(carritos.get(0).getId()));
        assertTrue(carritos.get(0).equals(carritoService.encontrarPorId(carritos.get(0).getId())));
        
        assertTrue(carritoService.existePorId(carritos.get(1).getId()));
        assertTrue(carritos.get(1).equals(carritoService.encontrarPorId(carritos.get(1).getId())));
        
        Long totalDespues = carritoService.contarCarritos();
        
        assertEquals(2L, totalDespues - totalAntes);
    }
    
}
