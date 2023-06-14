package com.bolsadeideas.springboot.datajpa.app.controllers;

import com.bolsadeideas.springboot.datajpa.app.service.IClienteService;
import com.bolsadeideas.springboot.datajpa.app.view.xml.ClienteList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cliente")
public class ClienteRestController {

    @Autowired
    private IClienteService clienteService;

    // En teoria deberia funcionar con dualidad
    // -> /api/cliente/listar?format=json
    // -> /api/cliente/listar?format=xml (Falla: Unable to locate object to be marshalled in model)
    @GetMapping(value = "/listar")
    public ClienteList listar(){
        return new ClienteList(clienteService.findAll());
    }
}
