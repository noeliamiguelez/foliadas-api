package com.foliadas.foliadas_api.Controller;

import com.foliadas.foliadas_api.DTO.GrupoDTO;
import com.foliadas.foliadas_api.Service.GrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/grupos")
public class GrupoViewController {

    @Autowired
    private GrupoService grupoService;

    @GetMapping("/nuevo")
    public String nuevoGrupo(Model model){
        model.addAttribute("grupo", new GrupoDTO());
        return "grupos-form.html";
    }

    @PostMapping("/guardar")
    public String guardarGrupo(@ModelAttribute GrupoDTO grupoDTO){
        grupoService.create(grupoDTO);
        return "redirect:/grupos/lista";
    }

    @GetMapping("/lista")
    public String listarGrupos(Model model){
        model.addAttribute("grupos", grupoService.getAll());
        return "grupos-lista.html";
    }

    @GetMapping("/editar/{id}")
    public String editarGrupo(@PathVariable int id, Model model){
        GrupoDTO grupo= grupoService.getById(id);
        model.addAttribute("grupos", grupo);
        return "grupos-form.html";
    }

}
