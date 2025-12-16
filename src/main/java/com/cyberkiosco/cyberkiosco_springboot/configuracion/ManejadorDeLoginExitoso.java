package com.cyberkiosco.cyberkiosco_springboot.configuracion;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component // La marco como componente para poder inyectarla luego
public class ManejadorDeLoginExitoso implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                        HttpServletResponse response, 
                                        Authentication authentication) throws IOException, ServletException {
        
        Collection<? extends GrantedAuthority> autoridades = authentication.getAuthorities();
        String direccionDestino = "/productos";
        Iterator<? extends GrantedAuthority> iterador = autoridades.iterator();
        while (iterador.hasNext() && direccionDestino.equalsIgnoreCase("/productos")) {
            GrantedAuthority autoridad = iterador.next();
            
            if (autoridad.getAuthority().equals("Administrador") || autoridad.getAuthority().equals("Lector")) {
                direccionDestino = "/admin";
            }
        }
        
        // 4. Redirigo al usuario a la URL calculada
        response.sendRedirect(direccionDestino);
    }
}