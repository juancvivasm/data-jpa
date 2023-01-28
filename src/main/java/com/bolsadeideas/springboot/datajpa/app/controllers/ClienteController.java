package com.bolsadeideas.springboot.datajpa.app.controllers;

import com.bolsadeideas.springboot.datajpa.app.models.dao.IClienteDao;
import com.bolsadeideas.springboot.datajpa.app.models.entity.Cliente;
import com.bolsadeideas.springboot.datajpa.app.service.IClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.Map;
import java.util.Objects;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

    // Ahora se utiliza un service para tener la facilidad de utilizar muchos mas DAO
//    @Autowired
//    private IClienteDao clienteDao;

    @Autowired
    private IClienteService clienteService;

    @GetMapping(value = "/listar")
    public String listar(Model model) {
        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("clientes", clienteService.findAll());
        return "listar";
    }

    @GetMapping(value = "/form")
    public String crear(Map<String, Object> model) {
        Cliente cliente = new Cliente();
        model.put("cliente", cliente);
        model.put("titulo", "Formulario de Cliente");
        return "form";
    }

    @GetMapping(value = "/form/{id}")
    public String crear(@PathVariable Long id, Model model) {
        Cliente cliente = new Cliente();
        if(id < 0){
            return "redirect:/listar";
        }
        cliente = clienteService.findOne(id);
        model.addAttribute("cliente", cliente );
        model.addAttribute("titulo", "Editar Cliente" );
        return "form";
    }

    @PostMapping("/form")
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model, SessionStatus status) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de Cliente" );
            return "form";
        }
        clienteService.save(cliente);
        status.setComplete();
        return "redirect:/listar";
    }

    @GetMapping(value = "/form/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {

        if(id > 0){
            clienteService.delete(id);
        }

        return "redirect:/listar";
    }

}
