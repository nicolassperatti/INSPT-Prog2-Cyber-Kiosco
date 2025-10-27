
package com.cyberkiosco.cyberkiosco_springboot.repository;

import com.cyberkiosco.cyberkiosco_springboot.entity.Categoria;
import com.cyberkiosco.cyberkiosco_springboot.entity.Marca;
import com.cyberkiosco.cyberkiosco_springboot.entity.Producto;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    Page<Producto> findByMarca(Marca marca, Pageable pageable);
    List<Producto> findByCategoria(Categoria categoria);
}
