package com.cyberkiosco.cyberkiosco_springboot.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.cyberkiosco.cyberkiosco_springboot.dtos.CategoriaDTO;
import com.cyberkiosco.cyberkiosco_springboot.entity.Categoria;
import com.cyberkiosco.cyberkiosco_springboot.service.CategoriaService;
import com.cyberkiosco.cyberkiosco_springboot.service.ProductoService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CategoriaController {

    private final ProductoService productoService;
    @Autowired
    CategoriaService categoriaService;

    CategoriaController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/admin/categorias")
    public String getMethodName(Model model) {
        model.addAttribute("rows", categoriaService.obtenerTodos());
        model.addAttribute("columns", Arrays.asList("nombre"));
        model.addAttribute("menuCategoria", true);
        return "admin";
    }

    @GetMapping("/admin/categoria/editar/{id}")
    public String getEditar(Model model, @PathVariable Long id) {


        if (model.containsAttribute("categoriaDTO")) {
            model.addAttribute("categoriaeditar", true);
            model.addAttribute("id", id);

            return "admin"; 
        }

        CategoriaDTO categoriaDTO = categoriaService.convertirACategoriaDTO(id);

        if (categoriaDTO != null) {
            model.addAttribute("categoriaDTO", categoriaDTO);
            model.addAttribute("id", id);
            model.addAttribute("categoriaeditar", true);
            return "admin"; 
        } else {
            
            return "redirect:/admin/categorias";
        }
    }

    @PostMapping("/admin/categoria/editar/{id}")
    public String postMethodName(@Valid @ModelAttribute("categoriaDTO") CategoriaDTO categoriaDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @PathVariable Long id) {

        if (categoriaService.existeNombreEnOtraCategoria(categoriaDTO.getNombre(), id)) {
            bindingResult.rejectValue("nombre", "unique", "Ya existe una categoria con este nombre");
        }


        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("categoriaDTO", categoriaDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.categoriaDTO",bindingResult);
            return "redirect:/admin/categoria/editar/" + id;
        }


        Categoria categoria = this.categoriaService.encontrarPorId(id);
        if (categoria == null) {
            redirectAttributes.addFlashAttribute("error", "La categoria que intenta editar no existe.");
            return "redirect:/admin/categorias";
        }


        categoria.setNombre(categoriaDTO.getNombre());
        categoriaService.guardar(categoria);


        return "redirect:/admin/categorias";
    }

    @GetMapping("/admin/categoria/crear")
    public String devolverFormCrear(Model model) {
        if(!model.containsAttribute("categoriaDTO")) {
            CategoriaDTO categoriaDTO = new CategoriaDTO();
            model.addAttribute("categoriaDTO", categoriaDTO);
        }
        model.addAttribute("categoriacrear", true);
        return "admin";
    }

    @PostMapping("/admin/categoria/crear")
    public String postCrear(Model model, @Valid @ModelAttribute("categoriaDTO") CategoriaDTO categoriaDTO,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(categoriaService.existePorNombre(categoriaDTO.getNombre())) {
            bindingResult.rejectValue("nombre", "unique", "Ya existe una categoria con este nombre");
        }
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("categoriaDTO", categoriaDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.categoriaDTO", bindingResult);
            return "redirect:/admin/categoria/crear";
        }
        Categoria categoria = new Categoria();
        categoria.setNombre(categoriaDTO.getNombre());
        categoriaService.guardar(categoria);
        return "redirect:/admin/categorias";
    }

    @GetMapping("/admin/categoria/cambiar_estado/{id}/{estado}")
    public String getEliminar(Model model, @PathVariable Long id, @PathVariable Boolean estado) {
        String path = "redirect:/admin/categorias";
        if (categoriaService.existePorId(id)) {
            categoriaService.cambiarEstadoPorId(id, estado);
            productoService.darDeBajaOAltaSegunCategoria(id, estado);
        }
        return path;
    }
}

