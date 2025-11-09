
package com.cyberkiosco.cyberkiosco_springboot.configuracion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private InterceptorAutenticacion intAutenticacion;
    

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(intAutenticacion)
                .addPathPatterns("/productos/**", "/carrito/**") // rutas en las que pedira autenticar
                .excludePathPatterns("/usuario/**"); // rutas en las que no lo pedira
    }
    
}

