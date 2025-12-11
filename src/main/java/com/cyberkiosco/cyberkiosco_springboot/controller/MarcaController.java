package com.cyberkiosco.cyberkiosco_springboot.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.cyberkiosco.cyberkiosco_springboot.dtos.MarcaDTO;
import com.cyberkiosco.cyberkiosco_springboot.entity.Marca;
import com.cyberkiosco.cyberkiosco_springboot.service.MarcaService;
import com.cyberkiosco.cyberkiosco_springboot.service.ProductoService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MarcaController {
    @Autowired
    MarcaService marcaService;

    @Autowired
    ProductoService productoService;

    @GetMapping("/admin/marcas")
    public String getMethodName(Model model) {
        model.addAttribute("rows", marcaService.obtenerTodos());
        model.addAttribute("columns", Arrays.asList("nombre"));
        model.addAttribute("menuMarca", true);
        return "admin";
    }

    @GetMapping("/admin/marca/editar/{id}")
    public String getEditar(Model model, @PathVariable Long id) {
        if (model.containsAttribute("marcaDTO")) {
            model.addAttribute("marcaeditar", true);
            model.addAttribute("id", id);

            return "admin"; 
        }

        
        MarcaDTO marcaDTO = marcaService.convertirAMarcaDTO(id);

        if (marcaDTO != null) {
            model.addAttribute("marcaDTO", marcaDTO);
            model.addAttribute("id", id);
            model.addAttribute("marcaeditar", true);
            return "admin"; 
        } else {
            return "redirect:/admin/marcas";
        }
    }

    @PostMapping("/admin/marca/editar/{id}")
    public String postMethodName(@Valid @ModelAttribute("marcaDTO") MarcaDTO marcaDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @PathVariable Long id) {

        if (marcaService.existeNombreEnOtraMarca(marcaDTO.getNombre(), id)) {
            bindingResult.rejectValue("nombre", "unique", "Ya existe una marca con este nombre");
        }


        if (bindingResult.hasErrors()) {

            redirectAttributes.addFlashAttribute("marcaDTO", marcaDTO);

            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.marcaDTO",
                    bindingResult);

 
            return "redirect:/admin/marca/editar/" + id;
        }
        Marca marca = this.marcaService.encontrarPorId(id);
        if (marca == null) {
            redirectAttributes.addFlashAttribute("error", "La marca que intenta editar no existe.");
            return "redirect:/admin/marcas";
        }

        marca.setNombre(marcaDTO.getNombre());
        marcaService.guardar(marca);


        return "redirect:/admin/marcas";
    }

    @GetMapping("/admin/marca/crear")
    public String devolverFormCrear(Model model) {
        if(!model.containsAttribute("marcaDTO")) {
            MarcaDTO marcaDTO = new MarcaDTO();
            model.addAttribute("marcaDTO", marcaDTO);
        }
        model.addAttribute("marcacrear", true);
        return "admin";
    }

    @PostMapping("/admin/marca/crear")
    public String postCrear(Model model, @Valid @ModelAttribute("marcaDTO") MarcaDTO marcaDTO,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(marcaService.existePorNombre(marcaDTO.getNombre())) {
            bindingResult.rejectValue("nombre", "unique", "Ya existe una marca con este nombre");
        }
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("marcaDTO", marcaDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.marcaDTO", bindingResult);
            return "redirect:/admin/marca/crear";
        }
        Marca marca = new Marca();
        marca.setNombre(marcaDTO.getNombre());
        marcaService.guardar(marca);
        return "redirect:/admin/marcas";
    }

    @GetMapping("/admin/marca/baja/{id}")
    public String getDarDeBaja(Model model, @PathVariable Long id) {
        String path = "redirect:/admin/marcas";
        if (marcaService.existePorId(id)) {
            marcaService.eliminarPorId(id);
            System.out.println("marca hola");
            productoService.darDeBajaOAltaSegunMarca(id,false);
            System.out.println("marca adios");
        }
        return path;
    }

    @GetMapping("/admin/marca/alta/{id}")
    public String getDarDeAlta(Model model, @PathVariable Long id) {
        String path = "redirect:/admin/marcas";
        if (marcaService.existePorId(id)) {
            marcaService.activarPorId(id);
            productoService.darDeBajaOAltaSegunMarca(id,true);
        }
        return path;
    }
}
