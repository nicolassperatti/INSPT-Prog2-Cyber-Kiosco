
package com.cyberkiosco.cyberkiosco_springboot.repository;

import com.cyberkiosco.cyberkiosco_springboot.entity.Carrito;
import com.cyberkiosco.cyberkiosco_springboot.entity.Final;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    // Buscamos por la variable 'usuarioFinal'
    List<Carrito> findByUsuarioFinal(Final usuarioFinal);
    
    // Para obtener carritos abiertos
    List<Carrito> findAllByUsuarioFinalAndFechaCompraIsNull(Final usuarioFinal);

    // Para el historial de compras
    List<Carrito> findAllByUsuarioFinalAndFechaCompraIsNotNull(Final usuarioFinal);
    
    Optional<Carrito> findByIdAndUsuarioFinal(Long id, Final usuarioFinal);
}