
package com.cyberkiosco.cyberkiosco_springboot.service;

import com.cyberkiosco.cyberkiosco_springboot.dtos.ProductoDTO;
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
    
    public List<Producto> obtenerTodosLosProductos() {
        return this.productoRepository.findAll();
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
    
    public Page<Producto> obtenerProductosPorMarca(Marca marca, int page, int size) {
        Validacion.validarNotNull(marca, "Marca");
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return this.obtenerProductosPorMarca(marca, pageable);
    }

    public Page<Producto> obtenerProductosPorMarca(Marca marca, Pageable pageable){
        return this.productoRepository.findByMarca(marca, pageable);
    }
    public Page<Producto> obtenerProductosPorCategoria(Categoria categoria, Pageable pageable){
        return this.productoRepository.findByCategoria(categoria, pageable);
    }

    public List<Producto> obetenerProductosQueContinienen(String nombre){
        return this.productoRepository.findByNombreLikeIgnoreCase("%" + nombre + "%");
    }
    
    public Page<Producto> obetenerProductosQueContinienen(String nombre, Pageable pageable){
        return this.productoRepository.findByNombreLikeIgnoreCase("%" + nombre + "%", pageable);
    }
    
    public Page<Producto> obetenerProductosQueContinienen(String nombre, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return obetenerProductosQueContinienen(nombre, pageable);
    }
    
    public Page<Producto> obtenerProductosPorMarca_Id(Long id_marca,int page, int size) {
        Marca marca = marcaService.encontrarPorId(id_marca);
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return this.obtenerProductosPorMarca(marca, pageable);
    }
    
    public Page<Producto> obtenerProductosPorCategoria(Categoria categoria, int page, int size) {
        Validacion.validarNotNull(categoria, "Categoria");
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return productoRepository.findByCategoria(categoria,pageable);
    }
    
    public Page<Producto> obtenerProductosPorCategoria_Id(Long id_categoria, int page, int size) {
        Categoria categoria = categoriaService.encontrarPorId(id_categoria);
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return productoRepository.findByCategoria(categoria,pageable);
    }

    public ProductoDTO convertirAProductoDTO(Long id){
        Producto producto = encontrarPorId(id);
        ProductoDTO productoDTO = null;
        if(producto != null){
            productoDTO = new ProductoDTO();
            productoDTO.setNombre(producto.getNombre());
            productoDTO.setPrecio(producto.getPrecio());
            productoDTO.setStock(producto.getStock());
            productoDTO.setDescripcion(producto.getDescripcion());
            productoDTO.setIdmarca(producto.getMarca().getId());
            productoDTO.setIdcategoria(producto.getCategoria().getId());
            productoDTO.setImagen(producto.getImagen());
        }
        return productoDTO;
    }

    public void guardarProducto(ProductoDTO productoDTO, long id){
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre(productoDTO.getNombre());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setMarca(marcaService.encontrarPorId(productoDTO.getIdmarca()));
        producto.setCategoria(categoriaService.encontrarPorId(productoDTO.getIdcategoria()));
        producto.setImagen(productoDTO.getImagen());
        guardarProducto(producto);
    }

    public boolean existeProductoConMarcaSinId(ProductoDTO productoDTO, Long id){
        Marca marca = this.marcaService.encontrarPorId(productoDTO.getIdmarca());
        return this.productoRepository.existsByNombreAndMarcaAndIdNot(productoDTO.getNombre(), marca, id);
    }

    public boolean existeProductoConMarca(ProductoDTO productoDTO){
        Marca marca = this.marcaService.encontrarPorId(productoDTO.getIdmarca());
        return this.productoRepository.existsByNombreAndMarca(productoDTO.getNombre(), marca);
    }

    public void guardarProductoNuevo(ProductoDTO productoDTO){
        Producto producto = new Producto();
        producto.setNombre(productoDTO.getNombre());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setMarca(marcaService.encontrarPorId(productoDTO.getIdmarca()));
        producto.setCategoria(categoriaService.encontrarPorId(productoDTO.getIdcategoria()));
        // Hardcodeamos el nombre de la imagen terminando en .jpg
        producto.setImagen("producto_placeholder.jpg");
        guardarProducto(producto);
    }

    public void darDeBajaOAltaSegunMarca(long idmarca, boolean estadoAlto){
        List<Producto> productos = null;
        Marca marca = marcaService.encontrarPorId(idmarca);
        if(marca != null){
            productos = this.productoRepository.findAllByMarca(marca);
            for (Producto producto : productos) {
                producto.setActivo(estadoAlto);
                System.out.println("producto: " +producto.toString());
            }
            productoRepository.saveAll(productos);
        }
         
    }
}
