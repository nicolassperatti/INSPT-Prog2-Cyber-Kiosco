package com.cyberkiosco.cyberkiosco_springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.cyberkiosco.cyberkiosco_springboot.service.MarcaService;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MarcaController {
    @Autowired MarcaService marcaService;

    @GetMapping("/admin/marca")
    public String getMethodName(Model model) {
        model.addAttribute("marca", marcaService.obtenerTodos());
        model.addAttribute("menuMarca", true);
        return new String();
    }
    
}
