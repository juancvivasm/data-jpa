package com.bolsadeideas.springboot.datajpa.app.view.json;

import com.bolsadeideas.springboot.datajpa.app.models.entity.Cliente;
import com.bolsadeideas.springboot.datajpa.app.view.xml.ClienteList;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Map;

@Component("listar.json")
public class ClienteListJsonView extends MappingJackson2JsonView {

    @Override
    protected Object filterModel(Map<String, Object> model) {

        model.remove("titulo");
        Page<Cliente> clientes = (Page<Cliente>) model.get("clientes");
        model.remove("clientes");
        model.remove("page");
        model.put("clientes", clientes.getContent());

        return super.filterModel(model);
    }
}
