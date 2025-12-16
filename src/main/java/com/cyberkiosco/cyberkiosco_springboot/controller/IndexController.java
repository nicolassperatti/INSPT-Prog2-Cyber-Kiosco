
package com.cyberkiosco.cyberkiosco_springboot.controller;


import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.cyberkiosco.cyberkiosco_springboot.dtos.QuejaDTO;
import com.cyberkiosco.cyberkiosco_springboot.entity.Carrito;
import com.cyberkiosco.cyberkiosco_springboot.entity.Final;
import com.cyberkiosco.cyberkiosco_springboot.entity.Usuario;
import com.cyberkiosco.cyberkiosco_springboot.security.UserDetailsImpl;
import com.cyberkiosco.cyberkiosco_springboot.service.CarritoService;
import com.cyberkiosco.cyberkiosco_springboot.service.UsuarioService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class IndexController {

    private final UsuarioService usuarioService;

    @Autowired CarritoService carritoService;

    IndexController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    
    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        String redireccion = null;
        if (userDetailsImpl.getUsuarioReal() instanceof Final){
            redireccion = "redirect:/productos";
        }else{
            redireccion = "redirect:/admin";
        }
        return redireccion;
    }
    
    @GetMapping("/contacto")
    public String verPaginaContacto(Model model, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        Usuario usuario = userDetailsImpl.getUsuarioReal();
        if(!model.containsAttribute("quejaDTO")){
            model.addAttribute("quejaDTO",new QuejaDTO());
        }
        
        if(usuario != null){
            model.addAttribute("idusuario",usuario.getId());
            model.addAttribute("compras",carritoService.obtenerTodosLosCarritosCompradosPorUsuario(usuario));
        }
        model.addAttribute("causas", Arrays.asList("compra mal hecha","falta producto","producto vencido","producto equivocado"));
        return "contacto";
    }

    @PostMapping("/contacto/{idusuario}")
    public String postMethodName(@Valid @ModelAttribute("quejaDTO") QuejaDTO quejaDTO, BindingResult bindingResult, @PathVariable Long idusuario, RedirectAttributes redirectAttributes) {
        if(carritoService.obtenerCarritoPorUsuarioYCarrito(quejaDTO.getIdcarrito(), usuarioService.encontrarPorId(idusuario)) == null){
            bindingResult.rejectValue("idcarrito", "no existe", "ese carrito no pertenece a ese id o no existe");
        }

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("quejaDTO",quejaDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.quejaDTO",bindingResult);
            return "redirect:/contacto";
        }
        Carrito carrito = carritoService.obtenerCarritoPorUsuarioYCarrito(quejaDTO.getIdcarrito(), usuarioService.encontrarPorId(idusuario));
        if(carrito != null){
            redirectAttributes.addFlashAttribute("mensaje","verificaremos que sucedio, muchas gracias");
        }
        return "redirect:/contacto";
    }
    

    @GetMapping("/admin")
    public String admin(Model model) {
        return "redirect:/admin/marcas";
    }
    
}

