package com.bolsadeideas.springboot.datajpa.app.view.xml;

import com.bolsadeideas.springboot.datajpa.app.models.entity.Cliente;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "clientes")
public class ClienteList {

    public List<Cliente> clientes;

    public ClienteList() {
    }

    public ClienteList(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    @XmlElement(name = "cliente")
    public List<Cliente> getClientes() {
        return clientes;
    }

}
