
package com.cyberkiosco.cyberkiosco_springboot.service;

import com.cyberkiosco.cyberkiosco_springboot.dtos.ProductoDTO;
import com.cyberkiosco.cyberkiosco_springboot.entity.Categoria;
import com.cyberkiosco.cyberkiosco_springboot.entity.Marca;
import com.cyberkiosco.cyberkiosco_springboot.entity.Producto;
import com.cyberkiosco.cyberkiosco_springboot.entity.auxiliar.Validacion;
import com.cyberkiosco.cyberkiosco_springboot.repository.ProductoRepository;

import java.util.Iterator;
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
    
    
    public Page<Producto> obtenerTodosLosProductosActivos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return obtenerTodosLosProductosActivos(pageable);
    }
    
    public Page<Producto> obtenerTodosLosProductosActivos(Pageable pageable){
        return this.productoRepository.findAllByActivoTrue(pageable);
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
    
    public void cambiarEstadoPorId(long id, boolean estado) {
        Producto producto = productoRepository.findById(id).get();
        if(producto != null){
            producto.setActivo(estado);
            guardarProducto(producto);
        }
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
        return this.productoRepository.findByMarcaAndActivoTrue(marca, pageable);
    }
    public Page<Producto> obtenerProductosPorCategoria(Categoria categoria, Pageable pageable){
        return this.productoRepository.findByCategoriaAndActivoTrue(categoria, pageable);
    }

    public List<Producto> obetenerProductosQueContinienen(String nombre){
        return this.productoRepository.findByNombreLikeIgnoreCase("%" + nombre + "%");
    }
    
    public Page<Producto> obetenerProductosQueContinienen(String nombre, Pageable pageable){
        return this.productoRepository.findByNombreLikeIgnoreCaseAndActivoTrue("%" + nombre + "%", pageable);
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
        return productoRepository.findByCategoriaAndActivoTrue(categoria,pageable);
    }
    
    public Page<Producto> obtenerProductosPorCategoria_Id(Long id_categoria, int page, int size) {
        Categoria categoria = categoriaService.encontrarPorId(id_categoria);
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return productoRepository.findByCategoriaAndActivoTrue(categoria,pageable);
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
        Producto producto = null;
        if(productoRepository.existsById(id)){
            producto = productoRepository.findById(id).get();
            producto.setId(id);
            producto.setImagen(productoDTO.getImagen());
        }
        else{
            producto = new Producto();
            producto.setImagen("producto_placeholder.jpg");
        }
        producto.setNombre(productoDTO.getNombre());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setMarca(marcaService.encontrarPorId(productoDTO.getIdmarca()));
        producto.setCategoria(categoriaService.encontrarPorId(productoDTO.getIdcategoria()));
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

    public boolean hayProductosDescontinuados(List<Producto> productos){
        boolean resultado = false;
        int i = 0;
        while(i < productos.size() && !resultado){
            Producto producto = productos.get(i);
            if(!producto.isActivo()){
                resultado = true;
            }
            i++;
        }
        return resultado;
    }

    /*public void guardarProductoNuevo(ProductoDTO productoDTO){
        Producto producto = new Producto();
        producto.setNombre(productoDTO.getNombre());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setMarca(marcaService.encontrarPorId(productoDTO.getIdmarca()));
        producto.setCategoria(categoriaService.encontrarPorId(productoDTO.getIdcategoria()));
        // Hardcodeamos el nombre de la imagen terminando en .jpg
        
        guardarProducto(producto);
    }*/

    public void darDeBajaVariosProductos(List<Producto> productos, boolean estado){
        for (Producto producto : productos) {
            producto.setActivo(estado);
        }
        productoRepository.saveAll(productos);
    }

    public void darDeBajaOAltaSegunMarca(long idmarca, boolean estado){
        List<Producto> productos = null;
        Marca marca = marcaService.encontrarPorId(idmarca);
        if(marca != null){
            productos = this.productoRepository.findAllByMarca(marca);
            darDeBajaVariosProductos(productos, estado);
        }
         
    }

    public void darDeBajaOAltaSegunCategoria(long idcategoria, boolean estado){
        List<Producto> productos = null;
        Categoria categoria = categoriaService.encontrarPorId(idcategoria);
        if(categoria != null){
            productos = this.productoRepository.findAllByCategoria(categoria);
            darDeBajaVariosProductos(productos, estado);
        }
    }
}
