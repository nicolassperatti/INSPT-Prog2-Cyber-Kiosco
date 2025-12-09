package com.cyberkiosco.cyberkiosco_springboot.dtos;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductoDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "Debes ingresar un stock")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock; // Integer es mejor que int para validar nulos

    @NotNull(message = "Debes ingresar un precio")
    @DecimalMin(value = "0.5", message = "El precio debe ser mayor a 0.5") 
    // Ojo: @Min funciona bien con Double. 
    // Si necesitas validar decimales (ej: 0.50), usa @DecimalMin("0.1")
    private Double precio; 

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 1, max = 100, message = "La descripción debe tener entre 1 y 100 caracteres")
    private String descripcion;

    @NotNull(message = "La marca es obligatoria")
    private Long idmarca;

    @NotNull(message = "La categoría es obligatoria")
    private Long idcategoria;

    private String imagen;
}