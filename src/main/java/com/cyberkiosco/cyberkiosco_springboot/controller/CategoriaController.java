package com.cyberkiosco.cyberkiosco_springboot.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.cyberkiosco.cyberkiosco_springboot.dtos.CategoriaDTO;
import com.cyberkiosco.cyberkiosco_springboot.entity.Categoria;
import com.cyberkiosco.cyberkiosco_springboot.service.CategoriaService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CategoriaController {
    @Autowired
    CategoriaService categoriaService;

    @GetMapping("/admin/categorias")
    public String getMethodName(Model model) {
        model.addAttribute("rows", categoriaService.obtenerTodos());
        model.addAttribute("columns", Arrays.asList("nombre"));
        model.addAttribute("menuCategoria", true);
        return "admin";
    }

    @GetMapping("/admin/categoria/editar/{id}")
    public String getEditar(Model model, @PathVariable Long id) {

        // CASO 1: ¿Viene rebotado de un error? (Tiene Flash Attributes)
        if (model.containsAttribute("categoriaDTO")) {
            // Genial, Spring ya puso el DTO con lo que escribió el usuario y los errores.
            // Solo nos falta activar el modal.
            model.addAttribute("categoriaeditar", true);
            model.addAttribute("id", id); // Aseguramos el ID para el action del form

            return "admin"; // Devolvemos la vista (NO redirect)
        }

        // CASO 2: Es una petición nueva (El usuario hizo clic en "Editar")
        CategoriaDTO categoriaDTO = categoriaService.convertirACategoriaDTO(id);

        if (categoriaDTO != null) {
            model.addAttribute("categoriaDTO", categoriaDTO);
            model.addAttribute("id", id);
            model.addAttribute("categoriaeditar", true); // Activamos el modal
            return "admin"; // Devolvemos la vista
        } else {
            // ID no existe, ahí sí redirigimos
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

        // 1. Si hay errores
        if (bindingResult.hasErrors()) {
            // Guardamos los datos "sucios" para que no tenga que escribir de nuevo
            redirectAttributes.addFlashAttribute("categoriaDTO", categoriaDTO);
            // Guardamos los errores (LA CLAVE DEBE SER EXACTA)
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.categoriaDTO",
                    bindingResult);

            // VOLVEMOS a la ruta de editar (al GET de arriba)
            return "redirect:/admin/categoria/editar/" + id;
        }

        // 2. Verificar que la categoria existe
        Categoria categoria = this.categoriaService.encontrarPorId(id);
        if (categoria == null) {
            // La categoria no existe, informar al usuario
            redirectAttributes.addFlashAttribute("error", "La categoria que intenta editar no existe.");
            return "redirect:/admin/categorias";
        }

        // 3. Si todo está bien, actualizar la categoria
        categoria.setNombre(categoriaDTO.getNombre());
        categoriaService.guardar(categoria);

        // Éxito: volvemos a la lista general
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

    @GetMapping("/admin/categoria/eliminar/{id}")
    public String getEliminar(Model model, @PathVariable Long id) {
        String path = "redirect:/admin/categorias";
        if (categoriaService.existePorId(id)) {
            categoriaService.eliminarPorId(id);
        }
        return path;
    }
}

