package com.cyberkiosco.cyberkiosco_springboot.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "lector")
@DiscriminatorValue("LECTOR")
@PrimaryKeyJoinColumn(name = "id_usuario")
@Getter
@NoArgsConstructor
@ToString(callSuper = true)
public class Lector extends Usuario {
    public Lector(String nombre, String apellido, String mail, String password){
        this.setNombre(nombre);
        this.setApellido(apellido);
        this.setMail(mail);
        this.setPassword(password);
    }
}
