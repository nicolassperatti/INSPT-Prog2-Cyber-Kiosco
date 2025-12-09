package com.cyberkiosco.cyberkiosco_springboot.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Representa un usuario Administrador.
 * 
 * Esta clase extiende Usuario y se almacena en la tabla 'admin'.
 * La tabla 'admin' solo contiene el id_usuario (FK a usuario),
 * ya que Admin no tiene campos adicionales propios.
 * 
 */
@Entity
@Table(name = "admin")
@DiscriminatorValue("ADMIN")
@PrimaryKeyJoinColumn(name = "id_usuario")
@Getter
@NoArgsConstructor
@ToString(callSuper = true)
public class Admin extends Usuario {
    
    
    /**
     * Constructor que permite crear un Admin con los datos básicos.
     * Útil cuando creas un nuevo administrador.
     */
    public Admin(String nombre, String apellido, String mail, String password) {
        // Llamamos al constructor de la clase padre
        // El id se genera automáticamente por JPA
        this.setNombre(nombre);
        this.setApellido(apellido);
        this.setMail(mail);
        this.setPassword(password);
    }
}