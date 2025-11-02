package com.cyberkiosco.cyberkiosco_springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cyberkiosco.cyberkiosco_springboot.entity.Carrito;
import com.cyberkiosco.cyberkiosco_springboot.entity.CarritoProducto;
import com.cyberkiosco.cyberkiosco_springboot.entity.Producto;
import com.cyberkiosco.cyberkiosco_springboot.entity.Usuario;
import com.cyberkiosco.cyberkiosco_springboot.entity.exceptions.StockInsuficienteException;
import com.cyberkiosco.cyberkiosco_springboot.service.CarritoProductoService;
import com.cyberkiosco.cyberkiosco_springboot.service.CarritoService;
import com.cyberkiosco.cyberkiosco_springboot.service.ProductoService;
import com.cyberkiosco.cyberkiosco_springboot.service.UsuarioService;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private CarritoProductoService carritoProductoService;

    @Autowired
    private UsuarioService usuarioService;
    
    
    @PostMapping("/agregar")
    public String agregarProducto(@RequestParam long idProducto, @RequestParam int cantidad) {
        
        String redireccion = "redirect:/productos";
        boolean valido = false;
        
        Producto producto = productoService.encontrarPorId(idProducto);
        //usuario hardcodeado
        Usuario usr = usuarioService.encontrarPorId(1L); 
        //obtiene o crea el carrito abierto
        Carrito carrito = carritoService.obtenerCarritoAbiertoPorUsuario(usr);
        //obtiene el CarritoProducto si ya existe, sino null
        CarritoProducto carritoProducto = carritoProductoService.encontrarPorId(carrito.getId(), producto.getId());
                
        try {
            //si el producto ya estaba en el carrito sumar la nueva cantidad
            if(carritoProducto != null) {
                carritoProducto.sumarCantidad_producto(cantidad);
                carritoProductoService.guardar(carritoProducto);
            } else {
                carritoProductoService.guardar(carrito, producto, cantidad, producto.getPrecio());
            }
            
            valido = true;
        } catch (IllegalArgumentException ilae) {
            System.out.println("ERROR: " + ilae.getMessage());
        } catch (StockInsuficienteException sie) {
            System.out.println("ERROR: " + sie.getMessage());
        } catch (Exception e) {
            System.out.println("Error desconocido");
        }
        
        // si no se pudo agregar a carrito quedarse en la pagina
        if (!valido) {
            redireccion = "redirect:/producto_detalle/" + producto.getId();
        } 
        
        return redireccion;
    }
    
}
