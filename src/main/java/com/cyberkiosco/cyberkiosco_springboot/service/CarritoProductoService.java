
package com.cyberkiosco.cyberkiosco_springboot.service;

import com.cyberkiosco.cyberkiosco_springboot.entity.Carrito;
import com.cyberkiosco.cyberkiosco_springboot.entity.CarritoProducto;
import com.cyberkiosco.cyberkiosco_springboot.entity.Producto;
import com.cyberkiosco.cyberkiosco_springboot.entity.embeddable.CarritoProductoKey;
import com.cyberkiosco.cyberkiosco_springboot.entity.exceptions.StockInsuficienteException;
import com.cyberkiosco.cyberkiosco_springboot.repository.CarritoProductoRepository;
import com.cyberkiosco.cyberkiosco_springboot.repository.CarritoRepository;
import com.cyberkiosco.cyberkiosco_springboot.repository.ProductoRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CarritoProductoService {
    
    @Autowired
    private CarritoProductoRepository carritoProductoRepository;
    
    @Autowired
    private CarritoRepository carritoRepository;
    
    @Autowired
    private ProductoRepository productoRepository;

    
    private Carrito encontrarCarritoPorId(Long id_carrito) {
        Optional<Carrito> carrito = carritoRepository.findById(id_carrito);
        
        if(carrito.isEmpty()) {
            throw new RuntimeException("No se encontro un carrito con id: " + id_carrito);
        }
        
        return carrito.get();
    }
    
    
    private Producto encontrarProductoPorId(Long id_producto) {
        Optional<Producto> producto = productoRepository.findById(id_producto);
        
        if(producto.isEmpty()) {
            throw new RuntimeException("No se encontro un producto con id: " + id_producto);
        }
        
        return producto.get();
    }
    
    
    public CarritoProducto crearCarritoProdcuto(Carrito carrito, Producto producto, int cantidad_producto, double precio_producto) {

        if(cantidad_producto > producto.getStock()) {
            throw new StockInsuficienteException("La cantidad demandada del producto es mayor al stock.");
        }
        
        CarritoProductoKey key = new CarritoProductoKey(carrito.getId(), producto.getId());
        
        CarritoProducto carritoProducto = new CarritoProducto();
        carritoProducto.setId(key);
        carritoProducto.setProducto(producto);
        carritoProducto.setCarrito(carrito);
        carritoProducto.setCantidad_producto(cantidad_producto);
        carritoProducto.setPrecio_producto(precio_producto);
        
        return carritoProducto;
    }
    
    
    public CarritoProducto crearCarritoProducto(Long id_carrito, Long id_producto, int cantidad_producto, double precio_producto) {
        Carrito carrito = encontrarCarritoPorId(id_carrito);
        Producto producto = encontrarProductoPorId(id_producto);
        
        return crearCarritoProdcuto(carrito, producto, cantidad_producto, precio_producto);
    }
    
    
    public void guardar(CarritoProducto carritoProducto) {
        carritoProductoRepository.save(carritoProducto);    //guarda y actualiza si ya existe
    }
    
    public void crearGuardar(Carrito carrito, Producto producto, int cantidad_producto, double precio_producto) {
        CarritoProducto carritoProducto = this.crearCarritoProdcuto(carrito, producto, cantidad_producto, precio_producto);
        this.guardar(carritoProducto);
    }
    
    
    public void guardar(Long id_carrito, Long id_producto, int cantidad_producto, double precio_producto) {
        CarritoProducto carritoProducto = crearCarritoProducto(id_carrito, id_producto, cantidad_producto, precio_producto);
        guardar(carritoProducto);
    }
    
    
    public boolean existePorId(CarritoProductoKey key) {
        return carritoProductoRepository.existsById(key);
    }
    
    
    public boolean existePorId(Long id_carrito, Long id_producto) {
        CarritoProductoKey key = new CarritoProductoKey(id_carrito, id_producto);
        return existePorId(key);
    }
    
    
    public CarritoProducto encontrarPorId(CarritoProductoKey key) {
        return carritoProductoRepository.findById(key).orElse(null);
    }
    
    
    public CarritoProducto encontrarPorId(Long id_carrito, Long id_producto) {
        CarritoProductoKey key = new CarritoProductoKey(id_carrito, id_producto);
        return this.encontrarPorId(key);
    }
    
    public void eliminarPorId(CarritoProductoKey key) {
        
        if(!this.existePorId(key)) {
            throw new RuntimeException("No se encontro el carrito_producto a eliminar.");
        }
 
        carritoProductoRepository.deleteById(key);
    }
    
    
    public void eliminarPorId(Long id_carrito, Long id_producto) {
        CarritoProductoKey key = new CarritoProductoKey(id_carrito, id_producto);
        
        this.eliminarPorId(key);
    }
    
    //obtiene todos los Objetos CarritoProducto que tienen un id_carrito
    public List<CarritoProducto> listaDeCarritoProductoPorId_carrito(Long id_carrito) {
        return carritoProductoRepository.findByCarrito_Id(id_carrito);
    }
    
    //obtiene todos los Objetos CarritoProducto que tienen un id_producto
    public List<CarritoProducto> listaDeCarritoProductoPorId_producto(Long id_producto) {
        return carritoProductoRepository.findByProducto_Id(id_producto);
    }
    
    //obtiene todos los productos de una lista de CarritoProducto
    public List<Producto> listaDeProductosDeListaCarritoProducto(List<CarritoProducto> listaCarritoProducto) {
        ArrayList<Producto> productos = new ArrayList<>();
        Producto productoAcutal;
        
        for(CarritoProducto carritoProducto : listaCarritoProducto) {
            productoAcutal = carritoProducto.getProducto();
            productos.add(productoAcutal);
        }
        
        return productos;
    }
    
    
    //obtiene todos los carritos de una lista de CarritoProducto
    public List<Carrito> listaDeCarritosDeListaCarritoProducto(List<CarritoProducto> listaCarritoProducto) {
        ArrayList<Carrito> carritos = new ArrayList<>();
        Carrito carritoAcutal;
        
        for(CarritoProducto carritoProducto : listaCarritoProducto) {
            carritoAcutal = carritoProducto.getCarrito();
            carritos.add(carritoAcutal);
        }
        
        return carritos;
    }
    
    //obtiene todos los productos que se encuentran en un carrito
    public List<Producto> obtenerTodosLosProductosEnCarrito(Long id_carrito) {
        List<CarritoProducto> listaCarritoProducto = listaDeCarritoProductoPorId_carrito(id_carrito);
        List<Producto> listaProductosEnCarrito = listaDeProductosDeListaCarritoProducto(listaCarritoProducto);
    
        return listaProductosEnCarrito;
    }
    
    //obtiene todos los carritos que contienen un producto especifico
    public List<Carrito> obtenerTodosLosCarritosConProducto(Long id_producto) {
        List<CarritoProducto> listaCarritoProducto = listaDeCarritoProductoPorId_producto(id_producto);
        List<Carrito> listaCarritosConProducto = listaDeCarritosDeListaCarritoProducto(listaCarritoProducto);
    
        return listaCarritosConProducto;
    }
    
}
