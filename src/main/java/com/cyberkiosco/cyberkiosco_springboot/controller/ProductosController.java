
package com.cyberkiosco.cyberkiosco_springboot.controller;


import com.cyberkiosco.cyberkiosco_springboot.entity.Categoria;
import com.cyberkiosco.cyberkiosco_springboot.entity.Marca;
import com.cyberkiosco.cyberkiosco_springboot.entity.Producto;
import com.cyberkiosco.cyberkiosco_springboot.service.CategoriaService;
import com.cyberkiosco.cyberkiosco_springboot.service.MarcaService;
import com.cyberkiosco.cyberkiosco_springboot.service.ProductoService;
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

    @Autowired
    private MarcaService marcaService;

    @Autowired
    private CategoriaService categoriaService;
    
    @GetMapping("/productos") // significa: Este metodo del controlador debe ejecutarse cuando alguien haga una petición GET a esta URL
    public String verSeccionProductos(Model model,
            @PageableDefault(size = 9, sort = "id", 
            direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Long idmarca) { 
        
        Page<Producto> paginaProductos;
        
        if(((nombre == null) || nombre.trim().isBlank()) && ((idmarca == null) || (idmarca <= 0))) { //si no hay nombre ni marca
            paginaProductos = productoService.obtenerTodosLosProductos(pageable);
        } else if((nombre == null || nombre.trim().isBlank()) &&(idmarca != null && idmarca > 0)){
            Marca marca = marcaService.encontrarPorId(idmarca);
            paginaProductos = productoService.obtenerProductosPorMarca(marca, pageable);
        } else {
            paginaProductos = productoService.obetenerProductosQueContinienen(nombre, pageable);
        }
        
        model.addAttribute("paginaProductos", paginaProductos);        
        
        // Mantiene el texto en la barra
        model.addAttribute("nombre", nombre);
        
        return "index";
    }

    @GetMapping("/productos_por_categoria")
    public String getMethodName(Model model, @PageableDefault(size = 9, sort = "id", 
    direction = Sort.Direction.ASC) Pageable pageable, @RequestParam Long idcategoria) {
        Page<Producto> paginaProductos;
        String redireccion = "redirect:/productos";
        if(idcategoria != null && idcategoria > 0){
            Categoria categoria = this.categoriaService.encontrarPorId(idcategoria);
            if(categoria != null){
                paginaProductos = this.productoService.obtenerProductosPorCategoria(categoria, pageable);
                model.addAttribute("paginaProductos",paginaProductos);
                redireccion = "index";
            }
        } 
        return redireccion;
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

        return redireccion;
    }
}

