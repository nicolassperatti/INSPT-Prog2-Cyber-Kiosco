
package com.cyberkiosco.cyberkiosco_springboot.controller;


import com.cyberkiosco.cyberkiosco_springboot.dtos.ProductoDTO;
import com.cyberkiosco.cyberkiosco_springboot.entity.Categoria;
import com.cyberkiosco.cyberkiosco_springboot.entity.Marca;
import com.cyberkiosco.cyberkiosco_springboot.entity.Producto;
import com.cyberkiosco.cyberkiosco_springboot.service.CategoriaService;
import com.cyberkiosco.cyberkiosco_springboot.service.MarcaService;
import com.cyberkiosco.cyberkiosco_springboot.service.ProductoService;

import jakarta.validation.Valid;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;



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

    @GetMapping("/productos_por_marca")
    public String getProductosPorMarca(Model model, @PageableDefault(size = 9, sort = "id", 
    direction = Sort.Direction.ASC) Pageable pageable, @RequestParam Long idmarca) {
        Page<Producto> paginaProductos;
        String redireccion = "redirect:/productos";
        if(idmarca != null && idmarca > 0){
            Marca marca = this.marcaService.encontrarPorId(idmarca);
            if(marca != null){
                paginaProductos = this.productoService.obtenerProductosPorMarca(marca, pageable);
                model.addAttribute("paginaProductos",paginaProductos);
                redireccion = "index";
            }
        } 
        return redireccion;
    }

    @GetMapping("/productos_por_categoria")
    public String getProductosPorCategoria(Model model, @PageableDefault(size = 9, sort = "id", 
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

    @GetMapping("/admin/productos")
    public String getMenuProductos(Model model) {
        model.addAttribute("rows",productoService.obtenerTodosLosProductos());
        model.addAttribute("columns", Arrays.asList("nombre"));
        model.addAttribute("menuProducto", true);
        return "admin";
    }

    @GetMapping("/admin/producto/editar/{id}")
    public String getEditForm(@PathVariable Long id, Model model) {
        
        if (model.containsAttribute("productoDTO")) {
            model.addAttribute("productoEditar", true);
            model.addAttribute("id", id);
            model.addAttribute("marcas", marcaService.obtenerTodos());
            model.addAttribute("categorias", categoriaService.obtenerTodos());
            return "admin";
        }

        ProductoDTO productoDTO = productoService.convertirAProductoDTO(id);
        System.out.println("test: " + productoDTO);
        if(productoDTO != null){
            model.addAttribute("productoDTO", productoDTO);
            model.addAttribute("productoEditar", true);
            model.addAttribute("id", id);
            model.addAttribute("marcas", marcaService.obtenerTodos());
            model.addAttribute("categorias", categoriaService.obtenerTodos());
            return "admin";
        }
        else{
            return "redirect:/admin/productos";
        }
    }

    @PostMapping("/admin/producto/editar/{id}")
    public String postMethodName(@Valid @ModelAttribute("productoDTO") ProductoDTO productoDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, @PathVariable Long id ) {
        System.out.println(productoDTO.toString());
        if(productoService.existeProductoConMarcaSinId(productoDTO, id)){
            bindingResult.rejectValue("idmarca", "unique", "Ya existe un producto con ese nombre y esa marca");
        }

        if(!marcaService.existePorId(productoDTO.getIdmarca())){
            bindingResult.rejectValue("idmarca", "notfound", "No existe esa marca");
        }

        if(!categoriaService.existePorId(productoDTO.getIdcategoria())){
            bindingResult.rejectValue("idcategoria", "notfound", "no existe esa categoria");
        }

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("productoDTO", productoDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productoDTO", bindingResult);
            return "redirect:/admin/producto/editar/" + id;
        }
        Producto producto = productoService.encontrarPorId(id);
        productoDTO.setImagen(producto.getImagen());
        System.out.println(producto.getImagen());
        productoService.guardarProducto(productoDTO,id);
        return "redirect:/admin/productos";
    }

    @GetMapping("/admin/producto/crear")
    public String devolverFormCrear(Model model) {
        if(!model.containsAttribute("productoDTO")) {
            ProductoDTO productoDTO = new ProductoDTO();
            model.addAttribute("productoDTO", productoDTO);
        }
        model.addAttribute("productoCrear", true);
        model.addAttribute("marcas", marcaService.obtenerTodos());
        model.addAttribute("categorias", categoriaService.obtenerTodos());
        return "admin";
    }

    @PostMapping("/admin/producto/crear")
    public String postCrear(Model model, @Valid @ModelAttribute("productoDTO") ProductoDTO productoDTO,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        
        if(productoService.existeProductoConMarca(productoDTO)){
            bindingResult.rejectValue("idmarca", "unique", "Ya existe un producto con ese nombre y esa marca");
        }

        if(!marcaService.existePorId(productoDTO.getIdmarca())){
            bindingResult.rejectValue("idmarca", "notfound", "No existe esa marca");
        }

        if(!categoriaService.existePorId(productoDTO.getIdcategoria())){
            bindingResult.rejectValue("idcategoria", "notfound", "no existe esa categoria");
        }

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("productoDTO", productoDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productoDTO", bindingResult);
            return "redirect:/admin/producto/crear";
        }
        
        productoService.guardarProductoNuevo(productoDTO);
        return "redirect:/admin/productos";
    }
    
    @GetMapping("/admin/producto/eliminar/{id}")
    public String getEliminar(Model model, @PathVariable Long id) {
        String path = "redirect:/admin/productos";
        if (productoService.existePorId(id)) {
            productoService.eliminarProductoPorId(id);
        }
        return path;
    }
    
}

