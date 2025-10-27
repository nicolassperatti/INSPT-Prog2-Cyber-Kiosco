package com.cyberkiosco.cyberkiosco_springboot.service;

import com.cyberkiosco.cyberkiosco_springboot.entity.Producto;
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
class ProductoServiceTest {


    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private MarcaService marcaService;
    
    @Autowired
    private CategoriaService categoriaService;

    
    private Producto crearProductoEjemplo1() {
        Producto prodNuevo = new Producto();
        prodNuevo.setNombre("Agua Mineral Saborizada");
        prodNuevo.setStock(10);
        prodNuevo.setPrecio(15.0);
        prodNuevo.setImagen("img/aguaMineralSaborizada.jpg");
        prodNuevo.setMarca(marcaService.encontrarPorId(2L));
        prodNuevo.setCategoria(categoriaService.encontrarPorId(2L));
        
        return prodNuevo;
    }
    
    private Producto crearProductoEjemplo2() {
        Producto prodNuevo = new Producto();
        prodNuevo.setNombre("Barra de Fijoles");
        prodNuevo.setStock(35);
        prodNuevo.setPrecio(20.0);
        prodNuevo.setImagen("img/barraDeFijoles.png");
        prodNuevo.setMarca(marcaService.encontrarPorId(1L));
        prodNuevo.setCategoria(categoriaService.encontrarPorId(1L));
        
        return prodNuevo;
    }
    
    private Producto crearProductoEjemplo3() {
        Producto prodNuevo = new Producto();
        prodNuevo.setNombre("Galletas Integrales");
        prodNuevo.setStock(50);
        prodNuevo.setPrecio(15.5);
        prodNuevo.setImagen("img/galletasIntegrales.png");
        prodNuevo.setMarca(marcaService.encontrarPorId(1L));
        prodNuevo.setCategoria(categoriaService.encontrarPorId(1L));
        
        return prodNuevo;
    }
    
    // Los tests se hacen en orden aleatorio excepto por el uso de @Order

    @Test
    @Order(1) //porque luego si se agregan o eliminan registros con los otros tests falla
    void testObtenerTodosLosProductos() {
        List<Producto> listaProductos;
        listaProductos = this.productoService.obtenerTodosLosProductos(0,5).getContent();
        
        for(Producto producto : listaProductos) {
            System.out.println(producto.toString());
        }
        
        // assertEquals(15, listaProductos.size()); //en principio son 15 productos en total
        assertEquals(5, listaProductos.size());//para probar la paginacion
    }
    
    
    @Test
    @Order(2) //porque luego si se agregan o eliminan registros con los otros tests falla
    void testContarProductos() {
        Long total = productoService.contarProductos(); 
        assertEquals(15L, total);
    }

    
    @Test
    @Order(3) //porque luego si se agregan o eliminan registros con los otros tests falla
    void testEncontrarPorMarca() {
        Long id_marca = 1L; 
        List<Producto> listaProductos = productoService.obtenerProductosPorMarca_Id(id_marca);
        assertEquals(4, listaProductos.size()); //en principio a 4 productos de la marca con id 1
    }
    
    
    @Test
    @Order(4) //porque luego si se agregan o eliminan registros con los otros tests falla
    void testEncontrarPorCategoria() {
        Long id_categoria = 1L; 
        List<Producto> listaProductos = productoService.obtenerProductosPorCategoria_Id(id_categoria);
        assertEquals(8, listaProductos.size()); //en principio a 8 productos de la categoira con id 1
    }
    
    
    @Test
    void testEncontrarPorIdExistente() {
        Producto resultado = productoService.encontrarPorId(1L);
        
        assertNotNull(resultado);
        assertEquals("Chips Picantes", resultado.getNombre());
    }

    
    @Test
    void testEncontrarPorIdNoExistente() {
        Producto resultado = productoService.encontrarPorId(12345L);
        
        assertNull(resultado);
    }

    
    @Test
    void testExistePorId() {
        Boolean resultado = productoService.existePorId(2L);
        
        assertTrue(resultado);
    }
    
    
    @Test
    void testNoExistePorId() {
        Boolean resultado = productoService.existePorId(8521L);
        
        assertFalse(resultado);
    }

    
    @Test
    void testGuardarProducto() {
        Producto producto = crearProductoEjemplo1(); 

        productoService.guardarProducto(producto); // +1 producto cantidad actual 16
        
        assertTrue(productoService.existePorId(producto.getId()));
        assertEquals("Agua Mineral Saborizada", productoService.encontrarPorId(producto.getId()).getNombre());
    }

    
    @Test
    void testEliminarProductoPorId() {
        productoService.eliminarProductoPorId(1L); // -1 producto cantidad acutal 15
        
        assertFalse(productoService.existePorId(1L));
    }

    
    @Test
    void testGuardarListaProductos() {
        List<Producto> productos = List.of(
                crearProductoEjemplo2(),
                crearProductoEjemplo3()
        );

        Long totalAntes = productoService.contarProductos();
        
        productoService.guardarListaProductos(productos);
        
        assertTrue(productoService.existePorId(productos.get(0).getId()));
        assertEquals(productos.get(0).getNombre(), productoService.encontrarPorId(productos.get(0).getId()).getNombre());
        
        assertTrue(productoService.existePorId(productos.get(0).getId()));
        assertEquals(productos.get(1).getNombre(), productoService.encontrarPorId(productos.get(1).getId()).getNombre());
        
        Long totalDespues = productoService.contarProductos();
        
        assertEquals(2L, totalDespues - totalAntes);
    }
    
}
