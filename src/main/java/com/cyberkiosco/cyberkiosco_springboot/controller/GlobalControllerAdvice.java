
package com.cyberkiosco.cyberkiosco_springboot.controller;

import java.time.LocalDate;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("anioActual")
    public int getAnioActual() {
        return LocalDate.now().getYear();
    }
    
}
