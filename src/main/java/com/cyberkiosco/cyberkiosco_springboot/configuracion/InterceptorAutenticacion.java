
package com.cyberkiosco.cyberkiosco_springboot.configuracion;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class InterceptorAutenticacion implements HandlerInterceptor {

    @Override
    public boolean preHandle (
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler 
            ) throws Exception {
        
        boolean continuarConRequest = true;

        //intenta obtener sesion activa sin crear una nueva
        HttpSession session = request.getSession(false); 

        if (session == null || session.getAttribute("usrLogueado") == null) {
            response.sendRedirect("/usuario/login");
            continuarConRequest = false;
        }

        return continuarConRequest;
    }
}


