package com.cyberkiosco.cyberkiosco_springboot.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cyberkiosco.cyberkiosco_springboot.entity.Carrito;
import com.cyberkiosco.cyberkiosco_springboot.entity.Producto;
import com.cyberkiosco.cyberkiosco_springboot.entity.auxiliar.CarritoItemDTO;
import com.cyberkiosco.cyberkiosco_springboot.service.CarritoProductoService;
import com.cyberkiosco.cyberkiosco_springboot.service.CarritoService;
import com.cyberkiosco.cyberkiosco_springboot.service.ProductoService;
import com.cyberkiosco.cyberkiosco_springboot.service.UsuarioService;


@Controller
@RequestMapping("/compras")
public class CompraController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private CarritoProductoService carritoProductoService;

    @Autowired
    private UsuarioService usuarioService;
    
    @ResponseBody
    @GetMapping("/test")
    public String getMethodName() {

        //manejando producto primero
        long idproducto = 1l;//se simula que llegan estos ids desde la pagina web
        long idproducto2 = 2l;
        int cantidad1 = 2;//idem que con los ids
        int cantidad2 = 3;
        Producto producto = productoService.encontrarPorId(idproducto);
        Producto producto2 = productoService.encontrarPorId(idproducto2);
        producto.restarStock(cantidad1);
        producto2.restarStock(cantidad2);
        productoService.guardarProducto(producto);
        productoService.guardarProducto(producto2);
        // restar el stock del producto funciona sin problemas

        //probando carrito ahora, funciona
        List<CarritoItemDTO> cItemDTOs = new ArrayList<>();
        cItemDTOs.add(new CarritoItemDTO(idproducto, cantidad1, producto.getPrecio()));
        cItemDTOs.add(new CarritoItemDTO(idproducto2, cantidad2, producto2.getPrecio()));
        double precio_total = this.obtenerPrecioTotal(cItemDTOs);
        Carrito carrito = new Carrito();
        carrito.setFecha_compra(LocalDateTime.now());
        carrito.setPrecio_total(precio_total);
        carrito.setUsuario(usuarioService.encontrarPorId(1l));
        this.carritoService.guardarCarrito(carrito);
        this.guardarCarritoProducto(carrito, cItemDTOs);
        return "hola mundo";
    }
    

    //funcion auxiliar
    private double obtenerPrecioTotal(List<CarritoItemDTO> carritoItemDTOs){
        double acum = 0.0;
        for (CarritoItemDTO carritoItemDTO : carritoItemDTOs) {
            acum += carritoItemDTO.getCantidad() * carritoItemDTO.getPrecio();
        }
        return Math.round((acum * 100.0)) / 100.0;
    }

    private void guardarCarritoProducto(Carrito carrito, List<CarritoItemDTO> carritoItemDTOs){
        for (CarritoItemDTO carritoItemDTO : carritoItemDTOs) {
            this.carritoProductoService.guardar(carrito.getId(), carritoItemDTO.getIdproducto(), carritoItemDTO.getCantidad(), carritoItemDTO.getPrecio());
        }
    }
}
