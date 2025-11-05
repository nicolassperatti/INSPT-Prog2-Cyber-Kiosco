
package com.cyberkiosco.cyberkiosco_springboot.repository;

import com.cyberkiosco.cyberkiosco_springboot.entity.Carrito;
import com.cyberkiosco.cyberkiosco_springboot.entity.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    List<Carrito> findByUsuario(Usuario usuario);
    
    //para obtener carritos abiertos y no cerrados
    List<Carrito> findAllByUsuarioAndFechaCompraIsNull(Usuario usuario);
    List<Carrito> findAllByUsuarioAndFechaCompraIsNotNull(Usuario usuario);
}