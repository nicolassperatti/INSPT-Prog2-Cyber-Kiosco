
package com.cyberkiosco.cyberkiosco_springboot.service;

import com.cyberkiosco.cyberkiosco_springboot.entity.Categoria;
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
public class CategoriaServiceTest {
    
    @Autowired
    private CategoriaService categoriaService;
    
    @Autowired
    private ProductoService productoService;
    
    
    private Categoria crearCategoriaEjemplo() {
        Categoria categoriaNueva = new Categoria();
        categoriaNueva.setNombre("Coleccionables");
        
        return categoriaNueva;
    }
    
    
    @Test
    @Order(1) //porque luego si se agregan o eliminan registros con los otros tests falla
    void testObtenerTodosLasCategorias() {
        ArrayList<Categoria> listaCategorias;
        listaCategorias = (ArrayList<Categoria>) categoriaService.obtenerTodos();
        
        for(Categoria categoria : listaCategorias) {
            System.out.println(categoria.toString());
        }
        
        assertEquals(2, listaCategorias.size()); //en principio son 2 categorias en total
    }
    
    @Test
    @Order(2) //porque luego si se agregan o eliminan registros con los otros tests falla
    void testContarCategorias() {
        Long total = categoriaService.contar(); 
        
        assertEquals(2L, total);
    }
    
    @Test
    @Order(3) //porque luego si se agregan o eliminan registros con los otros tests falla
    void testGuardarCategoria() {
        Categoria categoriaNueva = crearCategoriaEjemplo();

        categoriaService.guardar(categoriaNueva);
        
        assertTrue(categoriaService.existePorId(categoriaNueva.getId()));
        assertEquals(categoriaNueva, categoriaService.encontrarPorId(categoriaNueva.getId()));
    }
    
    
    
    
    
    @Test
    void testEncontrarPorIdExistente() {
        Categoria resultado = categoriaService.encontrarPorId(1L);
        
        assertNotNull(resultado);
    }
    
    
    @Test
    void testEncontrarPorIdNoExistente() {
        Categoria resultado = categoriaService.encontrarPorId(996L);
        
        assertNull(resultado);
    }
    
    
    @Test
    void testExistePorId() {
        Boolean resultado = categoriaService.existePorId(2L);
        
        assertTrue(resultado);
    }
    
    
    @Test
    void testNoExistePorId() {
        Boolean resultado = categoriaService.existePorId(9999L);
        
        assertFalse(resultado);
    }
    
}

