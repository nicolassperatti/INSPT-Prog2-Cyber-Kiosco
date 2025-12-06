package com.cyberkiosco.cyberkiosco_springboot.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.cyberkiosco.cyberkiosco_springboot.dtos.MarcaDTO;
import com.cyberkiosco.cyberkiosco_springboot.entity.Marca;
import com.cyberkiosco.cyberkiosco_springboot.service.MarcaService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class MarcaController {
    @Autowired
    MarcaService marcaService;

    @GetMapping("/admin/marcas")
    public String getMethodName(Model model) {
        model.addAttribute("rows", marcaService.obtenerTodos());
        model.addAttribute("columns", Arrays.asList("nombre"));
        model.addAttribute("menuMarca", true);
        return "admin";
    }

    @GetMapping("/admin/marca/editar/{id}")
    public String getEditar(Model model, @PathVariable Long id) {

        // CASO 1: ¿Viene rebotado de un error? (Tiene Flash Attributes)
        if (model.containsAttribute("marcaDTO")) {
            // Genial, Spring ya puso el DTO con lo que escribió el usuario y los errores.
            // Solo nos falta activar el modal.
            model.addAttribute("marcaeditar", true);
            model.addAttribute("id", id); // Aseguramos el ID para el action del form

            return "admin"; // Devolvemos la vista (NO redirect)
        }

        // CASO 2: Es una petición nueva (El usuario hizo clic en "Editar")
        MarcaDTO marcaDTO = marcaService.convertirAMarcaDTO(id);

        if (marcaDTO != null) {
            model.addAttribute("marcaDTO", marcaDTO);
            model.addAttribute("id", id);
            model.addAttribute("marcaeditar", true); // Activamos el modal
            return "admin"; // Devolvemos la vista
        } else {
            // ID no existe, ahí sí redirigimos
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

        // 1. Si hay errores
        if (bindingResult.hasErrors()) {
            // Guardamos los datos "sucios" para que no tenga que escribir de nuevo
            redirectAttributes.addFlashAttribute("marcaDTO", marcaDTO);
            // Guardamos los errores (LA CLAVE DEBE SER EXACTA)
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.marcaDTO",
                    bindingResult);

            // VOLVEMOS a la ruta de editar (al GET de arriba)
            return "redirect:/admin/marca/editar/" + id;
        }

        // 2. Si todo está bien
        Marca marca = this.marcaService.encontrarPorId(id);
        if (marca != null) {
            marca.setNombre(marcaDTO.getNombre());
            marcaService.guardar(marca);
        }

        // Éxito: volvemos a la lista general
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

    @GetMapping("/admin/marca/eliminar/{id}")
    public String getEliminar(Model model, @PathVariable Long id) {
        String path = "redirect:/admin/marcas";
        if (marcaService.existePorId(id)) {
            marcaService.eliminarPorId(id);
        }
        return path;
    }
}
