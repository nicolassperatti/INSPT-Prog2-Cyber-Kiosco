
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
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ProductosController {
    
    @Autowired
    private ProductoService productoService;
    
    @GetMapping("/productos")
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
}


