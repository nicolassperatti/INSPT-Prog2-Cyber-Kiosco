package com.cyberkiosco.cyberkiosco_springboot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Clase base abstracta para todos los tipos de usuarios.
 * Utiliza estrategia JOINED: cada subclase tiene su propia tabla,
 * pero comparten los campos comunes en la tabla 'usuario'.
 * 
 * La columna 'tipo_usuario' actúa como discriminador para identificar
 * qué tipo de usuario es cada registro (ADMIN o FINAL).
 */
@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_usuario", discriminatorType = DiscriminatorType.STRING)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public abstract class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;
    
    private String nombre;
    private String apellido;
    private String mail;
    private String password;
    
    // NOTA: 'fondos' NO está aquí porque solo Final lo tiene
    // Admin no necesita fondos, por eso usamos JOINED strategy
    
    /**
     * Valida que un string no sea null ni vacío.
     * Método auxiliar reutilizable para validaciones.
     */
    private void validarString(String str, String nombreAtributo) {
        if (str == null) {
            throw new IllegalArgumentException(nombreAtributo + " no puede ser null.");
        }
        
        if (str.trim().isEmpty()) {
            // .trim() elimina los espacios en blanco al principio y al final del string
            throw new IllegalArgumentException(nombreAtributo + " no puede estar vacio/a.");
        }
    }
    
    public void setNombre(String nombre) {
        validarString(nombre, "nombre");
        this.nombre = nombre;
    }
    
    public void setApellido(String apellido) {
        validarString(apellido, "apellido");
        this.apellido = apellido;
    }
    
    public void setMail(String mail) {
        validarString(mail, "mail");
        this.mail = mail;
    }
    
    public void setPassword(String password) {
        validarString(password, "password");
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Usuario other = (Usuario) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.apellido, other.apellido)) {
            return false;
        }
        if (!Objects.equals(this.mail, other.mail)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, apellido, mail, password);
    }
}