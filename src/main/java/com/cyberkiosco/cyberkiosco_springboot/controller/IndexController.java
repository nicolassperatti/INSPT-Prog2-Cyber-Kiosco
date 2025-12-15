
package com.cyberkiosco.cyberkiosco_springboot.controller;


import org.springframework.ui.Model;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.cyberkiosco.cyberkiosco_springboot.entity.Final;
import com.cyberkiosco.cyberkiosco_springboot.security.UserDetailsImpl;


@Controller
public class IndexController {
    
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
    public String verPaginaContacto(Model model) {
        return "contacto";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        return "redirect:/admin/marcas";
    }
    
}

