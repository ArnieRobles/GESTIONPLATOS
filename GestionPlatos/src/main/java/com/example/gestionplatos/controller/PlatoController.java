package com.example.gestionplatos.controller;

import com.example.gestionplatos.model.Plato;
import com.example.gestionplatos.service.PlatoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Controller
@RequestMapping("/platos")
public class PlatoController {

    private final PlatoService service;

    public PlatoController(PlatoService service) {
        this.service = service;
    }

    // Listar todos los platos
    @GetMapping
    public String listarPlatos(Model model) {
        model.addAttribute("platos", service.listarPlatos());
        return "listaPlatos";
    }

    // Mostrar formulario para agregar nuevo plato
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("plato", new Plato());
        return "formularioPlato";
    }

    // Guardar plato
    @PostMapping
    public String guardarPlato(@ModelAttribute Plato plato) {
        service.guardarPlato(plato);
        return "redirect:/platos";
    }

    // Editar un plato existente
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Plato plato = service.buscarPlatoPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("plato", plato);
        return "formularioPlato";
    }

    // Eliminar un plato
    @GetMapping("/eliminar/{id}")
    public String eliminarPlato(@PathVariable Long id) {
        service.eliminarPlato(id);
        return "redirect:/platos";
    }

    // Generar reporte en PDF
    @GetMapping("/reporte/pdf")
    public void generarReportePdf(HttpServletResponse response) throws IOException {
        service.generarReportePdf(response);
    }

    // Generar reporte en Excel
    @GetMapping("/reporte/excel")
    public void generarReporteExcel(HttpServletResponse response) throws IOException {
        service.generarReporteExcel(response);
    }
}
