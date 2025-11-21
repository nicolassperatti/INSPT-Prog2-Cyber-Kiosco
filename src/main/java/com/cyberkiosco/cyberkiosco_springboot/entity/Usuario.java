
package com.cyberkiosco.cyberkiosco_springboot.entity;

import com.cyberkiosco.cyberkiosco_springboot.entity.exceptions.FondosInsuficientesException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Getter
@NoArgsConstructor 
@AllArgsConstructor
@ToString
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "id_rol")
    private Rol rol;
   
    private String nombre;
    private String apellido;
    private String mail;
    private String password;
    private double fondos;
    
    
    public void setRol(Rol rol) {
        if(rol == null) {
            throw new IllegalArgumentException("El rol del usuario no puede ser null.");
        }
        this.rol = rol;
    }
    
    
    private void validarString(String str, String nombreAtributo) {
        if (str == null) {
            throw new IllegalArgumentException( nombreAtributo + " no puede ser null.");
        }
        
        if(str.trim().isEmpty()) {
            // .trim() elimina los espacios en blanco al principio y al final del string
            throw new IllegalArgumentException( nombreAtributo + " no puede estar vacio/a.");
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
    
    
    public void setFondos(double fondos) {
        if (fondos < 0) {
            throw new IllegalArgumentException("Los fondos del usuario no pueden ser menores a 0");
        }
        this.fondos = fondos;
    }
    
    
    public void sumarFondos (double fondosSumados) {
        if (fondosSumados <= 0) {
            throw new IllegalArgumentException("Los fondos a sumar deben ser mayores a cero.");
        }
        this.fondos += fondosSumados;
    }
    
    
    public void restarFondos(double fondosRestados) {
        double fondosRestantes;
        if (fondos <= 0) {
            throw new IllegalArgumentException("Los fondos a restar deben ser mayores a cero.");
        } else {
            fondosRestantes = this.fondos - fondosRestados;
            
            if(fondosRestantes < 0) {
                throw new FondosInsuficientesException("No se cuenta con fondos suficientes para la operacion.");                
            }
        }
        
        this.fondos = fondosRestantes;
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
        if (Double.doubleToLongBits(this.fondos) != Double.doubleToLongBits(other.fondos)) {
            return false;
        }
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
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return Objects.equals(this.rol, other.rol);
    }

    
}
