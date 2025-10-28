
package com.cyberkiosco.cyberkiosco_springboot.controller;


import java.time.LocalDate;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    
    @GetMapping("/")
    public String index(Model model) { 
        model.addAttribute("currentYear", LocalDate.now().getYear());
        return "index";
    }
}