package com.cyberkiosco.cyberkiosco_springboot.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MarcaEditarDTO {
    @NotNull(message = "no puede ser nulo")
    @NotEmpty(message = "no puede estar vacio")
    private String nombre;

}
