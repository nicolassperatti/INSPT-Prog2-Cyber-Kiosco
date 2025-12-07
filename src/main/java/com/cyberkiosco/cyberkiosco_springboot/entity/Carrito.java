
package com.cyberkiosco.cyberkiosco_springboot.entity;

import com.cyberkiosco.cyberkiosco_springboot.entity.auxiliar.Validacion;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Entity
@Getter
@NoArgsConstructor 
@AllArgsConstructor
public class Carrito {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito")
    private Long id;
    
    @Column(name = "precio_total")
    private double precioTotal;
    
    @Column(name = "fecha_compra")
    private LocalDateTime fechaCompra;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Final usuarioFinal;
    
    
    public void setId(long id) {
        if (id < 0) {
            throw new IllegalArgumentException("El id_carrito no puede ser menor a cero.");
        }
        this.id = id;
    }
    
    public void setPrecio_total (double precioTotal) {
        if (precioTotal < 0) {
            throw new IllegalArgumentException("El precio_total no puede ser menor a cero.");
        }
        this.precioTotal = precioTotal;
    }

    public void setFecha_compra(LocalDateTime fechaCompra) {
        if(fechaCompra == null) {
            throw new IllegalArgumentException("La fecha_compra no puede ser null.");
        }
       
        this.fechaCompra = fechaCompra;
    }

    public void setUsuarioFinal(Final usuarioFinal) {
        Validacion.validarNotNull(usuarioFinal, "usuarioFinal");
        this.usuarioFinal = usuarioFinal;
    }

    @Override
    public String toString() {
        return "Carrito{" + "id=" + id + ", precioTotal=" + precioTotal + ", fechaCompra=" + fechaCompra + ", usuario=" + usuarioFinal + '}';
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
        final Carrito other = (Carrito) obj;
        if (Double.doubleToLongBits(this.precioTotal) != Double.doubleToLongBits(other.precioTotal)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.fechaCompra, other.fechaCompra)) {
            return false;
        }
        return Objects.equals(this.usuarioFinal, other.usuarioFinal);
    }

}
