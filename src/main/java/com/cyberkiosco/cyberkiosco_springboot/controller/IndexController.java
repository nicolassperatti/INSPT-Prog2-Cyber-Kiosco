
package com.cyberkiosco.cyberkiosco_springboot.controller;


import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    
    @GetMapping("/")
    public String index(Model model) { 
       
        return "redirect:/productos";
    }
}

