
package com.cyberkiosco.cyberkiosco_springboot.service;

import com.cyberkiosco.cyberkiosco_springboot.entity.Categoria;
import com.cyberkiosco.cyberkiosco_springboot.entity.Marca;
import com.cyberkiosco.cyberkiosco_springboot.entity.Producto;
import com.cyberkiosco.cyberkiosco_springboot.entity.auxiliar.Validacion;
import com.cyberkiosco.cyberkiosco_springboot.repository.ProductoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public class ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private MarcaService marcaService;
    
    @Autowired
    private CategoriaService categoriaService;
    
    
    public Page<Producto> obtenerTodosLosProductos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return obtenerTodosLosProductos(pageable);
    }
    
    public Page<Producto> obtenerTodosLosProductos(Pageable pageable){
        return this.productoRepository.findAll(pageable);
    }
    public Producto encontrarPorId(long id) {
        return productoRepository.findById(id).orElse(null);
    }
    
    public boolean existePorId(long id) {
        return productoRepository.existsById(id);
    }
    
    public void guardarProducto(Producto producto) {
        productoRepository.save(producto); //guarda y actualiza si ya existe
        // si se elimina un producto y se ingresa uno con la misma id da error !!!
    }
    
    public void eliminarProductoPorId(long id) {
        productoRepository.deleteById(id);
    }
    
    public long contarProductos() {
        return productoRepository.count();
    }
    
    public void guardarListaProductos(List<Producto> listaProductos) {
        productoRepository.saveAll(listaProductos);
    }
    
    public List<Producto> obtenerProductosPorMarca(Marca marca) {
        Validacion.validarNotNull(marca, "Marca");
        return productoRepository.findByMarca(marca);
    }
    
    public List<Producto> obtenerProductosPorMarca_Id(Long id_marca) {
        Marca marca = marcaService.encontrarPorId(id_marca);
        return productoRepository.findByMarca(marca);
    }
    
    public List<Producto> obtenerProductosPorCategoria(Categoria categoria) {
        Validacion.validarNotNull(categoria, "Categoria");
        return productoRepository.findByCategoria(categoria);
    }
    
    public List<Producto> obtenerProductosPorCategoria_Id(Long id_categoria) {
        Categoria categoria = categoriaService.encontrarPorId(id_categoria);
        return productoRepository.findByCategoria(categoria);
    }
}
