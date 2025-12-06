package com.cyberkiosco.cyberkiosco_springboot.service;

import com.cyberkiosco.cyberkiosco_springboot.dtos.CategoriaDTO;
import com.cyberkiosco.cyberkiosco_springboot.entity.Categoria;
import com.cyberkiosco.cyberkiosco_springboot.repository.CategoriaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    
    public List<Categoria> obtenerTodos() {
        return categoriaRepository.findAll();
    }
    
    public Categoria encontrarPorId(long id) {
        return categoriaRepository.findById(id).orElse(null);
    }
    
    public boolean existePorId(long id) {
        return categoriaRepository.existsById(id);
    }
    
    public void guardar(Categoria categoria) {
        categoriaRepository.save(categoria); //guarda y actualiza si ya existe
    }
    
    public void eliminarPorId(long id) {
        categoriaRepository.deleteById(id);
    }
    
    public long contar() {
        return categoriaRepository.count();
    }

    public CategoriaDTO convertirACategoriaDTO(long id) {
        Categoria categoria = encontrarPorId(id);
        CategoriaDTO categoriaDTO = null;
        if (categoria != null) {
            categoriaDTO = new CategoriaDTO();
            categoriaDTO.setNombre(categoria.getNombre());
        }
        return categoriaDTO;
    }

    public boolean existePorNombre(String nombre) {
        return categoriaRepository.existsByNombre(nombre);
    }

    public boolean existeNombreEnOtraCategoria(String nombre, Long id) {
        return categoriaRepository.existsByNombreAndIdNot(nombre, id);
    }
       
}
