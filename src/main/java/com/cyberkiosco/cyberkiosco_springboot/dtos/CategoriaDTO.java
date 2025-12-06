package com.cyberkiosco.cyberkiosco_springboot.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoriaDTO {
    @NotBlank(message = "no puede estar vacio")
    private String nombre;

}

