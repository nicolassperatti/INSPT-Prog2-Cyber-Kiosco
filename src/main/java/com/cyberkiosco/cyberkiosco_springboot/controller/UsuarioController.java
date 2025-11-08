
package com.cyberkiosco.cyberkiosco_springboot.controller;


import com.cyberkiosco.cyberkiosco_springboot.entity.Usuario;
import com.cyberkiosco.cyberkiosco_springboot.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class UsuarioController {
    
    @Autowired
    UsuarioService usrService;
    
    
    @PostMapping("/usuario/login/credenciales")
    public String usuarioLogin (
            @RequestParam String mail,
            @RequestParam String password,
            HttpSession sesion
        ) {
        
        String redireccion = "redirect:/usuario/login";
        Usuario usr = null;
        
        try {
            usr = usrService.obtenerPorMailYPassword(mail, password);
            
            if(usr == null) {
                throw new RuntimeException("no se ha encontrado un usuario con esas credenciales");
            }
            
            sesion.setAttribute("usrLogueado", usr);
            redireccion = "redirect:/productos";
            
        } catch (IllegalArgumentException ile) {
            System.out.println("ERROR: " + ile.getMessage());
        } catch (RuntimeException re) {
            System.out.println("ERROR: " + re.getMessage());
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        
        return redireccion;
    }
    
    
    @GetMapping("/usuario/login")
    public String loginPage (Model model) {
        return "login";
    }
    
    
}

