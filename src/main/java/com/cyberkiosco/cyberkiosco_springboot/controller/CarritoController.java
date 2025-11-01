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
import com.cyberkiosco.cyberkiosco_springboot.service.CarritoProductoService;
import com.cyberkiosco.cyberkiosco_springboot.service.CarritoService;
import com.cyberkiosco.cyberkiosco_springboot.service.ProductoService;
import com.cyberkiosco.cyberkiosco_springboot.service.UsuarioService;


@Controller
@RequestMapping("/compras")
public class CarritoController {

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

        
        return "hola mundo";
    }
   
}
