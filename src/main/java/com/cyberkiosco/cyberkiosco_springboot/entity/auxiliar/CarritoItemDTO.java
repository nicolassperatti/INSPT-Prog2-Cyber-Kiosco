package com.cyberkiosco.cyberkiosco_springboot.entity.auxiliar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarritoItemDTO {
    private long idproducto;
    private int cantidad;
    private double precio;

}
