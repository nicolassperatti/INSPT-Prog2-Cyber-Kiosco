package com.cyberkiosco.cyberkiosco_springboot.service;

import com.cyberkiosco.cyberkiosco_springboot.dtos.MarcaDTO;
import com.cyberkiosco.cyberkiosco_springboot.entity.Marca;
import com.cyberkiosco.cyberkiosco_springboot.repository.MarcaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarcaService {
    
    @Autowired
    private MarcaRepository marcaRepository;
    
    
    public List<Marca> obtenerTodos() {
        return marcaRepository.findAll();
    }
    
    public Marca encontrarPorId(long id) {
        return marcaRepository.findById(id).orElse(null);
    }
    
    public boolean existePorId(long id) {
        return marcaRepository.existsById(id);
    }
    
    public void guardar(Marca marca) {
        marcaRepository.save(marca); //guarda y actualiza si ya existe
    }
    
    public void cambiarEstadoPorId(long id, boolean estado) {
        Marca marca = marcaRepository.findById(id).get();
        if(marca != null){
            marca.setActivo(estado);
            marcaRepository.save(marca);
        }
    }
    
    public long contar() {
        return marcaRepository.count();
    }

    public MarcaDTO convertirAMarcaDTO(long id) {
        Marca marca = encontrarPorId(id);
        MarcaDTO marcaDTO = null;
        if (marca != null) {
            marcaDTO = new MarcaDTO();
            marcaDTO.setNombre(marca.getNombre());
        }
        return marcaDTO;
    }

    public boolean existePorNombre(String nombre) {
        return marcaRepository.existsByNombre(nombre);
    }

    public boolean existeNombreEnOtraMarca(String nombre, Long id) {
        return marcaRepository.existsByNombreAndIdNot(nombre, id);
    }
       
}

