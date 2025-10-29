
package com.cyberkiosco.cyberkiosco_springboot.controller;


import com.cyberkiosco.cyberkiosco_springboot.entity.Producto;
import com.cyberkiosco.cyberkiosco_springboot.service.ProductoService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    
    @Autowired
    private ProductoService productoService;
    
    @GetMapping("/")
    public String index(Model model) { 
        model.addAttribute("currentYear", LocalDate.now().getYear());
        
        List<Producto> listaProductos = productoService.obtenerTodosLosProductos();
        
        model.addAttribute("productos", listaProductos);
        
        return "index";
    }
}

