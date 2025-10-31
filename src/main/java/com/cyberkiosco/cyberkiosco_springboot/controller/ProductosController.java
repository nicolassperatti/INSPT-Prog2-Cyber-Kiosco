
package com.cyberkiosco.cyberkiosco_springboot.controller;


import com.cyberkiosco.cyberkiosco_springboot.entity.Producto;
import com.cyberkiosco.cyberkiosco_springboot.service.ProductoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ProductosController {
    
    @Autowired
    private ProductoService productoService;
    
    @GetMapping("/productos") // significa: Este método del controlador debe ejecutarse cuando alguien haga una petición GET a esta URL
    public String index(Model model,
            @PageableDefault(size = 9, sort = "id", 
            direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) String nombre) { 
        
        Page<Producto> paginaProductos;
        
        if((nombre == null) || (nombre.trim().isBlank())) { //si no hay nombre de producto para busqueda
            paginaProductos = productoService.obtenerTodosLosProductos(pageable);
        } else {
            paginaProductos = productoService.obetenerProductosQueContinienen(nombre, pageable);
        }
        
        model.addAttribute("paginaProductos", paginaProductos);        
        
        // Mantiene el texto en la barra
        model.addAttribute("nombre", nombre);
        
        return "index";
    }
    
    @GetMapping("/producto_detalle/{id}")
    public String verProducto(@PathVariable Long id, Model model) {
        Producto producto = productoService.encontrarPorId(id);
        String redireccion;

        if (producto == null) {
            redireccion =  "redirect:/productos"; // Si no existe
        } else {
            model.addAttribute("producto", producto); 
            redireccion = "producto_detalle"; // nombre del archivo HTML 
        }

        return redireccion; // nombre del template HTML que mostrarás
    }
}

