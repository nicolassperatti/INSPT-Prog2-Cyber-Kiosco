
package com.cyberkiosco.cyberkiosco_springboot.entity;

import com.cyberkiosco.cyberkiosco_springboot.entity.auxiliar.Validacion;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor 
@AllArgsConstructor
public class Marca {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_marca")
    private Long id;
    
    private String nombre;

    @Column(columnDefinition = "boolean default true")
    private boolean activo;
    
    
    public void setId(long id) {
        if (id < 0) {
            throw new IllegalArgumentException("El id_marca no puede ser menor a cero.");
        }
        this.id = id;
    }
    
    public void setNombre(String nombre) {
        Validacion.validarString(nombre, "nombre de marca");
        this.nombre = nombre;
    }

    public void setActivo(boolean activo){
        this.activo = activo;
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
        final Marca other = (Marca) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Marca{" + "id=" + id + ", nombre=" + nombre + '}';
    }
    
}
