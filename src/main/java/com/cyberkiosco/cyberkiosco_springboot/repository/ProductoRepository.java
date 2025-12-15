
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
    
    Page<Producto> findByMarcaAndActivoTrue(Marca marca, Pageable pageable);
    Page<Producto> findByCategoriaAndActivoTrue(Categoria categoria, Pageable pageable);
    List<Producto> findByNombreLikeIgnoreCase(String nombre);
    Page<Producto> findByNombreLikeIgnoreCaseAndActivoTrue(String nombre, Pageable pageable);
    boolean existsByNombreAndMarcaAndIdNot(String nombre, Marca marca, Long id );
    boolean existsByNombreAndMarca(String nombre, Marca marca);
    List<Producto> findAllByMarca(Marca marca);
    List<Producto> findAllByCategoria(Categoria categoria);
    Page<Producto> findAllByActivoTrue(Pageable pageable);;
}
