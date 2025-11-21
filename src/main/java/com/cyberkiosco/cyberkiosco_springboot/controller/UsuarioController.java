
package com.cyberkiosco.cyberkiosco_springboot.controller;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsuarioController {
    
    
    @GetMapping("/usuario/login")
    public String loginPage (Model model) {
        return "login";
    }
    
    
}

