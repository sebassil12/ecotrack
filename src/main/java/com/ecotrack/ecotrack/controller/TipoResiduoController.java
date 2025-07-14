package com.ecotrack.ecotrack.controller;

import com.ecotrack.ecotrack.model.TipoResiduo;
import com.ecotrack.ecotrack.service.impl.TipoResiduoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tipos-residuo")
public class TipoResiduoController {

    @Autowired
    private TipoResiduoService tipoResiduoService;

    @GetMapping
    public String showTiposResiduo(Model model) {
        List<TipoResiduo> tiposResiduo = tipoResiduoService.getAll();
        model.addAttribute("tiposResiduo", tiposResiduo);
        model.addAttribute("nuevoTipoResiduo", new TipoResiduo());
        return "tipos-residuo"; // Renders tipos-residuo.html
    }

    @PostMapping
    public String createTipoResiduo(@ModelAttribute("nuevoTipoResiduo") TipoResiduo nuevoTipoResiduo) {
        tipoResiduoService.createTipoResiduo(nuevoTipoResiduo);
        return "redirect:/tipos-residuo";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        TipoResiduo tipoResiduo = tipoResiduoService.getById(id);
        model.addAttribute("editTipoResiduo", tipoResiduo);
        // Reuse the same template, but load the list again for consistency
        model.addAttribute("tiposResiduo", tipoResiduoService.getAll());
        return "tipos-residuo";
    }

    @PostMapping("/update/{id}")
    public String updateTipoResiduo(@PathVariable Long id, @ModelAttribute("editTipoResiduo") TipoResiduo updatedTipoResiduo) {
        tipoResiduoService.updateTipoResiduo(id, updatedTipoResiduo);
        return "redirect:/tipos-residuo";
    }

    @PostMapping("/delete/{id}")
    public String deleteTipoResiduo(@PathVariable Long id) {
        tipoResiduoService.deleteTipoResiduo(id);
        return "redirect:/tipos-residuo";
    }
}