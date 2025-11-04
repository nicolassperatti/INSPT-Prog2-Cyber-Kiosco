
package com.cyberkiosco.cyberkiosco_springboot.service;

import com.cyberkiosco.cyberkiosco_springboot.entity.Carrito;
import com.cyberkiosco.cyberkiosco_springboot.entity.CarritoProducto;
import com.cyberkiosco.cyberkiosco_springboot.entity.Producto;
import com.cyberkiosco.cyberkiosco_springboot.entity.embeddable.CarritoProductoKey;
import com.cyberkiosco.cyberkiosco_springboot.entity.exceptions.StockInsuficienteException;
import com.cyberkiosco.cyberkiosco_springboot.repository.CarritoProductoRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CarritoProductoService {
    
    @Autowired
    private CarritoProductoRepository carritoProductoRepository;
    
    @Autowired
    private CarritoService carritoService;
    
    @Autowired
    private ProductoService productoService;

    
    private Carrito encontrarCarritoPorId(Long id_carrito) {
        //Optional<Carrito> carrito = carritoService.encontrarPorId(id_carrito);
        Carrito carrito = carritoService.encontrarPorId(id_carrito);
        
        if(carrito == null) {
            throw new RuntimeException("No se encontro un carrito con id: " + id_carrito);
        }
        
        return carrito;
    }
    
    
    private Producto encontrarProductoPorId(Long id_producto) {
        //Optional<Producto> producto = productoRepository.findById(id_producto);
        Producto producto = productoService.encontrarPorId(id_producto);
        
        if(producto == null) {
            throw new RuntimeException("No se encontro un producto con id: " + id_producto);
        }
        
        return producto;
    }
    
    
    public CarritoProducto crearCarritoProducto(Carrito carrito, Producto producto, int cantidad_producto, double precio_producto) {

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
        
        return crearCarritoProducto(carrito, producto, cantidad_producto, precio_producto);
    }
    
    
    public void guardar(CarritoProducto carritoProducto) {
        carritoProductoRepository.save(carritoProducto);    //guarda y actualiza si ya existe
    }
    
    public void guardar(Carrito carrito, Producto producto, int cantidad_producto, double precio_producto) {
        CarritoProducto carritoProducto = this.crearCarritoProducto(carrito, producto, cantidad_producto, precio_producto);
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
    
    
    public void sumarCantidad_producto (CarritoProducto carritoProducto, int cantidadExtra) {
        int nuevaCantidad, cantidadStockProd;
        
        if(cantidadExtra < 0) {
            throw new IllegalArgumentException("La cantidad_producto no puede ser menor a cero.");
        }

        if(carritoProducto == null) {
            throw new IllegalArgumentException("CarritoProducto no puede ser null.");
        }
        
        nuevaCantidad = carritoProducto.getCantidad_producto() + cantidadExtra;
        cantidadStockProd = carritoProducto.getProducto().getStock();
        
        if(nuevaCantidad > cantidadStockProd) {
            throw new StockInsuficienteException("La cantidad demandada del producto es mayor al stock.");
        }
        
        carritoProducto.setCantidad_producto(nuevaCantidad);
        guardar(carritoProducto);
    }
    
    
    public boolean stockValidoParaCompra(CarritoProducto carritoProducto) {
        if(carritoProducto == null) {
            throw new IllegalArgumentException("el carritoProducto es null.");
        }
        
        return carritoProducto.getCantidad_producto() <= carritoProducto.getProducto().getStock();
    }
    
    
    public boolean stockValidoParaCompra(long idCarrito, long idProducto) {
        CarritoProducto carProd = this.encontrarPorId(idCarrito, idProducto);
        
        if(carProd == null) {
            throw new RuntimeException("no se encontro carritoProducto con idCarrito: " + idCarrito + " idProducto: " + idProducto);
        }
        
        return stockValidoParaCompra(carProd);
    }
    
    
    public boolean stocksDeCarritoValidosParaCompra(long idCarrito) {
        List<CarritoProducto> carProds;
        int cantCarProds, i;
        boolean stocksValidos;
        
        if(!carritoService.existePorId(idCarrito)) {
            throw new IllegalArgumentException("el carrito ingresado no existe."); 
        }
        
        carProds = this.listaDeCarritoProductoPorId_carrito(idCarrito);
        
        if(carProds.isEmpty()) {
            throw new RuntimeException("el carrito no tiene productos");
        }
        
        cantCarProds = carProds.size();
        i = 0;
        stocksValidos = true;
        
        while((i < cantCarProds) && stocksValidos) {
            stocksValidos = this.stockValidoParaCompra(carProds.get(i));
            i++;
        }
        
        return stocksValidos;
    }
    
    
    public void comprarProdEnCarrito(CarritoProducto carProd) {
        Producto prod;
        
        if(carProd == null) {
            throw new IllegalArgumentException("el carritoProducto es null.");
        }
        
        prod = carProd.getProducto();
        prod.restarStock(carProd.getCantidad_producto());
        productoService.guardarProducto(prod);
    }
    
    
    public void comprarProdEnCarrito(long idCarrito, long idProducto) {
        CarritoProducto carProd = this.encontrarPorId(idCarrito, idProducto);
        
        if(carProd == null) {
            throw new RuntimeException("no se encontro carritoProducto con idCarrito: " + idCarrito + " idProducto: " + idProducto);
        }
        
        comprarProdEnCarrito(carProd);
    }
    
    //para que se use el que pide la id como argumento
    private void comprarCarritoEntero(Carrito car) {
        List<CarritoProducto> carProds;
        int cantProdsEnCar;
        
        if(car == null) {
            throw new IllegalArgumentException("el Carrito es null.");
        }
        
        carProds = this.listaDeCarritoProductoPorId_carrito(car.getId());
        cantProdsEnCar = carProds.size();
        
        if(!carProds.isEmpty()) {
            for(int i = 0; i < cantProdsEnCar; i++) {
                comprarProdEnCarrito(carProds.get(i));
            }
        } 
    }
    
    
    public void comprarCarritoEntero(long idCarrito) {
        Carrito car = carritoService.encontrarPorId(idCarrito);
        
        comprarCarritoEntero(car);
        car.setFecha_compra(LocalDateTime.now());
        carritoService.guardarCarrito(car);
    }
    
    
}
