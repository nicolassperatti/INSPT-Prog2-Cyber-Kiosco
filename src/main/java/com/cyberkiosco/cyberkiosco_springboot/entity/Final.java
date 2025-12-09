package com.cyberkiosco.cyberkiosco_springboot.entity;

import com.cyberkiosco.cyberkiosco_springboot.entity.exceptions.FondosInsuficientesException;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Representa un usuario Final (cliente).
 * 
 * Esta clase extiende Usuario y se almacena en la tabla 'final'.
 * La tabla 'final' contiene el id_usuario (FK a usuario) y el campo 'fondos',
 * que es específico solo de los usuarios finales.
 * 
 * Los administradores NO tienen fondos, por eso esta lógica está aquí
 * y no en la clase base Usuario.
 */
@Entity
@Table(name = "final")
@DiscriminatorValue("FINAL")
@PrimaryKeyJoinColumn(name = "id_usuario")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Final extends Usuario {
    
    /**
     * Fondos disponibles del usuario final.
     * Este campo solo existe en la tabla 'final', no en 'admin'.
     */
    @Column(name = "fondos")
    private double fondos;
    
    /**
     * Constructor que permite crear un Final con los datos básicos y fondos iniciales.
     * Útil cuando creas un nuevo usuario final.
     */
    public Final(String nombre, String apellido, String mail, String password, double fondos) {
        // Llamamos al constructor de la clase padre
        this.setNombre(nombre);
        this.setApellido(apellido);
        this.setMail(mail);
        this.setPassword(password);
        this.setFondos(fondos);
    }
    
    /**
     * Establece los fondos del usuario.
     * Valida que no sean negativos.
     */
    public void setFondos(double fondos) {
        if (fondos < 0) {
            throw new IllegalArgumentException("Los fondos del usuario no pueden ser menores a 0");
        }
        this.fondos = fondos;
    }
    
    /**
     * Suma fondos al saldo actual del usuario.
     * Valida que la cantidad a sumar sea positiva.
     */
    public void sumarFondos(double fondosSumados) {
        if (fondosSumados <= 0) {
            throw new IllegalArgumentException("Los fondos a sumar deben ser mayores a cero.");
        }
        this.fondos += fondosSumados;
    }
    
    /**
     * Resta fondos del saldo actual del usuario.
     * Valida que haya fondos suficientes antes de restar.
     */
    public void restarFondos(double fondosRestados) {
        if (fondosRestados <= 0) {
            throw new IllegalArgumentException("Los fondos a restar deben ser mayores a cero.");
        }
        
        double fondosRestantes = this.fondos - fondosRestados;
        
        if (fondosRestantes < 0) {
            throw new FondosInsuficientesException("No se cuenta con fondos suficientes para la operacion.");
        }
        
        this.fondos = fondosRestantes;
    }
}