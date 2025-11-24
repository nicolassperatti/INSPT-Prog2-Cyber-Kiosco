package com.cyberkiosco.cyberkiosco_springboot.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.cyberkiosco.cyberkiosco_springboot.dtos.MarcaEditarDTO;
import com.cyberkiosco.cyberkiosco_springboot.entity.Marca;
import com.cyberkiosco.cyberkiosco_springboot.service.MarcaService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@Controller
public class MarcaController {
    @Autowired MarcaService marcaService;

    @GetMapping("/admin/marcas")
    public String getMethodName(Model model) {
        model.addAttribute("rows", marcaService.obtenerTodos());
        model.addAttribute("columns", Arrays.asList("nombre"));
        return "admin";
    }

    @GetMapping("/admin/marca/editar/{id}")
    public String getEditar(Model model, @PathVariable int id) {
        model.addAttribute("marcaeditar", true);
        MarcaEditarDTO marcaEditarDTO = new MarcaEditarDTO();
        marcaEditarDTO.setNombre(marcaService.encontrarPorId(id).getNombre());
        model.addAttribute(marcaEditarDTO);
        model.addAttribute("id", id);
        return "admin";
    }

    @PostMapping("/admin/marca/editarpost/{id}")
    public String postMethodName(@Valid @ModelAttribute("marcaEditarDTO") MarcaEditarDTO marcaEditarDTO, BindingResult bindingResult, @PathVariable int id, Model model) {
        //TODO: process POST request
        String path = "redirect:/admin/marcas";
        if(bindingResult.hasErrors()){
            path = "redirect:/admin/marca/editar/" + id;
            System.out.println(path);
        }
        else{
            Marca marca = this.marcaService.encontrarPorId(id);
            marca.setNombre(marcaEditarDTO.getNombre());
            marcaService.guardar(marca);
        }

        return path;
    }
    
    
    
}
