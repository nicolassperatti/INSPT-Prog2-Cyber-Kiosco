
package com.cyberkiosco.cyberkiosco_springboot.entity;

import com.cyberkiosco.cyberkiosco_springboot.entity.embeddable.CarritoProductoKey;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor 
@AllArgsConstructor
public class CarritoProducto {

    @EmbeddedId
    private CarritoProductoKey id;
    
    @ManyToOne
    @MapsId("id_carrito")
    @JoinColumn(name = "id_carrito")
    private Carrito carrito;
    
    @ManyToOne
    @MapsId("id_producto")
    @JoinColumn(name = "id_producto")
    private Producto producto;

    private int cantidad_producto;
    private double precio_producto;

    
    public void setId(CarritoProductoKey id) {
        if(id == null) {
            throw new IllegalArgumentException("CarritoProductoKey no puede ser null.");
        }
        this.id = id;
    }
    
    public void setCarrito(Carrito carrito) {
        if(carrito == null) {
            throw new IllegalArgumentException("Carrito no puede ser null.");
        }
        this.carrito = carrito;
    }

    public void setProducto(Producto producto) {
        if(producto == null) {
            throw new IllegalArgumentException("Producto no puede ser null.");
        }
        this.producto = producto;
    }
    
    public void setCantidad_producto(int cantidad_producto) {
        if (cantidad_producto < 0) {
            throw new IllegalArgumentException("La cantidad_producto no puede ser menor a cero.");
        }
        this.cantidad_producto = cantidad_producto;
    }

    public void setPrecio_producto(double precio_producto) {
        if (precio_producto < 0) {
            throw new IllegalArgumentException("El precio_producto no puede ser menor a cero.");
        }
        this.precio_producto = precio_producto;
    }

    public void sumarCantidad_producto (int cantidadExtra) {
        if(cantidadExtra < 0) {
            throw new IllegalArgumentException("La cantidad_producto no puede ser menor a cero.");
        }        
        this.setCantidad_producto(this.cantidad_producto + cantidadExtra);
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
        final CarritoProducto other = (CarritoProducto) obj;
        if (this.cantidad_producto != other.cantidad_producto) {
            return false;
        }
        if (Double.doubleToLongBits(this.precio_producto) != Double.doubleToLongBits(other.precio_producto)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.carrito, other.carrito)) {
            return false;
        }
        return Objects.equals(this.producto, other.producto);
    }
    
    
}
