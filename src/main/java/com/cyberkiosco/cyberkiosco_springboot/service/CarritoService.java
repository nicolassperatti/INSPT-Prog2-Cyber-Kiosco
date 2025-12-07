
package com.cyberkiosco.cyberkiosco_springboot.service;

import com.cyberkiosco.cyberkiosco_springboot.entity.Carrito;
import com.cyberkiosco.cyberkiosco_springboot.entity.Final;
import com.cyberkiosco.cyberkiosco_springboot.entity.Usuario;
import com.cyberkiosco.cyberkiosco_springboot.repository.CarritoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CarritoService {
    
    @Autowired
    private CarritoRepository carritoRepository;
    
    @Autowired
    private UsuarioService usuarioService;
    
    
    public List<Carrito> obtenerTodosLosCarritos() {
        return carritoRepository.findAll();
    }
    
    public Carrito encontrarPorId(long id) {
        return carritoRepository.findById(id).orElse(null);
    }
    
    public boolean existePorId(long id) {
        return carritoRepository.existsById(id);
    }
    
    public void guardarCarrito(Carrito carrito) {
        carritoRepository.save(carrito); //guarda y actualiza si ya existe
        // si se elimina un producto y se ingresa uno con la misma id da error !!!
    }
    
    public void eliminarCarritoPorId(long id) {
        carritoRepository.deleteById(id);
    }
    
    public long contarCarritos() {
        return carritoRepository.count();
    }
    
    public void guardarListaCarritos(List<Carrito> listaCarritos) {
        carritoRepository.saveAll(listaCarritos);
    }
    
    public List<Carrito> encontrarPorId_usuario(long id_usuario) {
        Usuario usr = usuarioService.encontrarPorId(id_usuario);
        
        if(usr == null) {
            throw new IllegalArgumentException("El usuario cuyos carritos hay que buscar no existe.");
        }
        
        return encontrarPorUsuario(usr);
    }
    
    public List<Carrito> encontrarPorUsuario(Usuario usr) {
        // 1. VALIDACIÓN: ¿Es un usuario tipo Final?
        if (!(usr instanceof Final)) {
            // Si es Admin, lanzamos error.
            throw new IllegalArgumentException("El usuario ingresado no es de tipo Final y no puede comprar.");
        }

        // 2. CASTING: Convertimos Usuario a Final
        Final usuarioFinal = (Final) usr;
        List<Carrito> listaCarritos = carritoRepository.findByUsuarioFinal(usuarioFinal);

        return listaCarritos;
    }
    
    //es privada para que solo sea llamada cuando no exista otro carrito abierto
    private Carrito abrirCarritoParaUsuarioFinal(Final usuarioFinal) {
        Carrito carrito = new Carrito();
        carrito.setUsuarioFinal(usuarioFinal);
        
        carritoRepository.save(carrito);
        
        return carrito;
    }
    
    public Carrito obtenerCarritoAbiertoPorUsuario (Usuario usr) {
        // 1. VALIDACIÓN: ¿Es un usuario tipo Final?
        if (!(usr instanceof Final)) {
            // Si es Admin, lanzamos error.
            throw new IllegalArgumentException("El usuario ingresado no es de tipo Final y no puede comprar.");
        }

        // 2. CASTING: Convertimos Usuario a Final
        Final usuarioFinal = (Final) usr;
        List<Carrito> listaCarritos = carritoRepository.findAllByUsuarioFinalAndFechaCompraIsNull(usuarioFinal);
        Carrito carritoAbierto;
        
        if(listaCarritos.isEmpty()) {
            carritoAbierto = this.abrirCarritoParaUsuarioFinal(usuarioFinal);
        } else {
            carritoAbierto = listaCarritos.get(0);
            
            //elimina los carritos abiertos de mas de la bdd en caso que existan
            if(listaCarritos.size() > 1) {
                for(int i=1; i<listaCarritos.size(); i++) {
                    carritoRepository.delete(listaCarritos.get(i));
                }
            }
        }
        
        return carritoAbierto;
    }

    public List<Carrito> obtenerTodosLosCarritosCompradosPorUsuario(Usuario usr) {
        // 1. VALIDACIÓN: ¿Es un usuario tipo Final?
        if (!(usr instanceof Final)) {
            // Si es Admin, lanzamos error.
            throw new IllegalArgumentException("El usuario ingresado no es de tipo Final y no puede comprar.");
        }

        // 2. CASTING: Convertimos Usuario a Final
        Final usuarioFinal = (Final) usr;
        return carritoRepository.findAllByUsuarioFinalAndFechaCompraIsNotNull(usuarioFinal);
    }

    public Carrito obtenerCarritoPorUsuarioYCarrito(Long id, Usuario usuario){
        // 1. VALIDACIÓN: ¿Es un usuario tipo Final?
        if (!(usuario instanceof Final)) {
            // Si es Admin, lanzamos error.
            throw new IllegalArgumentException("El usuario ingresado no es de tipo Final y no puede comprar.");
        }

        // 2. CASTING: Convertimos Usuario a Final
        Final usuarioFinal = (Final) usuario;
        return carritoRepository.findByIdAndUsuarioFinal(id, usuarioFinal).orElse(null);
    }
}
