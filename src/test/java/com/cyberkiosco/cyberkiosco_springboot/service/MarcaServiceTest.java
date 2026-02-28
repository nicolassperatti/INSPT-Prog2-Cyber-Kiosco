
package com.cyberkiosco.cyberkiosco_springboot.service;

import com.cyberkiosco.cyberkiosco_springboot.entity.Marca;
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
public class MarcaServiceTest {
    
    @Autowired
    private MarcaService marcaService;
    
    @Autowired
    private ProductoService productoService;
    
    
    private Marca crearMarcaEjemplo() {
        Marca marcaNueva = new Marca();
        marcaNueva.setNombre("Saladisima");
        
        return marcaNueva;
    }
    
    
    @Test
    @Order(1) //porque luego si se agregan o eliminan registros con los otros tests falla
    void testObtenerTodosLasMarcas() {
        ArrayList<Marca> listaMarcas;
        listaMarcas = (ArrayList<Marca>) this.marcaService.obtenerTodos();
        
        for(Marca marca : listaMarcas) {
            System.out.println(marca.toString());
        }
        
        assertEquals(4, listaMarcas.size()); //en principio son 4 marcas en total
    }
    
    @Test
    @Order(2) //porque luego si se agregan o eliminan registros con los otros tests falla
    void testContarMarcas() {
        Long total = marcaService.contar(); 
        
        assertEquals(4L, total);
    }
    
    @Test
    @Order(3) //porque luego si se agregan o eliminan registros con los otros tests falla
    void testGuardarMarca() {
        Marca marcaNueva = this.crearMarcaEjemplo();

        marcaService.guardar(marcaNueva);
        
        assertTrue(marcaService.existePorId(marcaNueva.getId()));
        assertEquals(marcaNueva, marcaService.encontrarPorId(marcaNueva.getId()));
    }
    
    
    
    
    
    @Test
    void testEncontrarPorIdExistente() {
        Marca resultado = marcaService.encontrarPorId(1L);
        
        assertNotNull(resultado);
    }
    
    
    @Test
    void testEncontrarPorIdNoExistente() {
        Marca resultado = marcaService.encontrarPorId(996L);
        
        assertNull(resultado);
    }
    
    
    @Test
    void testExistePorId() {
        Boolean resultado = marcaService.existePorId(2L);
        
        assertTrue(resultado);
    }
    
    
    @Test
    void testNoExistePorId() {
        Boolean resultado = marcaService.existePorId(9999L);
        
        assertFalse(resultado);
    }
    
}

