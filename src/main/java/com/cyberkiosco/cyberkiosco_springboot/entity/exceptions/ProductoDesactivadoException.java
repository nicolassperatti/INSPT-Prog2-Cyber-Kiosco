package com.cyberkiosco.cyberkiosco_springboot.entity.exceptions;

public class ProductoDesactivadoException extends RuntimeException {
    public ProductoDesactivadoException(String message){
        super(message);
    }
}
