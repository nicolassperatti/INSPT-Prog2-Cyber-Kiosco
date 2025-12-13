
package com.cyberkiosco.cyberkiosco_springboot.repository;

import com.cyberkiosco.cyberkiosco_springboot.entity.Marca;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {
    boolean existsByNombreAndIdNot(String nombre, Long id);
    boolean existsByNombre(String nombre);
    @NativeQuery(value = "SELECT DISTINCT p.marca FROM Producto p WHERE p.categoria.id = :idcategoria")
    List<Marca> findByCategoria(@Param("idcategoria") long idcategoria);
}
