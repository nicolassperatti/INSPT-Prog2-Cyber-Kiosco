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
    /**
     * metodo que sirve para guardar una marca o para actualizar una
     * @param marca marca a guardar o actualizar
     */
    public void guardar(Marca marca) {
        marcaRepository.save(marca); 
    }
    
    /**
     * Cambia el estado de una marca, si esta desactivada
     * se reactiva y viceversa
     * @param id id de la marca a buscar
     * @param estado estado de la marca, debe ser un boolean
     */
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

    /**
     * metodo que recibe el id de una marca,
     * la busca, verifica que exista,
     * si existe, devuelve un dto solamente
     * con el nombre
     * @param id id de la marca a buscar
     * @return el dto de marca si esta existe, sino null
     */
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

