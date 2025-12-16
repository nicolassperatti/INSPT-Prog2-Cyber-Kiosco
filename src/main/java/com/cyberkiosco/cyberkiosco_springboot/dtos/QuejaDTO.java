package com.cyberkiosco.cyberkiosco_springboot.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuejaDTO {
    @NotBlank(message = "la razon no puede estar vacia")
    private String razonQueja;
    @NotNull(message = "el id debe existir")
    private Long idcarrito;
    @NotBlank(message = "la queja no puede estar vacia")
    private String queja;
}
