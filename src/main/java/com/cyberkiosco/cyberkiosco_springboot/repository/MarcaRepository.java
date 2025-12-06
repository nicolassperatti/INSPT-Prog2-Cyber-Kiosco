
package com.cyberkiosco.cyberkiosco_springboot.repository;

import com.cyberkiosco.cyberkiosco_springboot.entity.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {
    boolean existsByNombreAndIdNot(String nombre, Long id);
    boolean existsByNombre(String nombre);
}
