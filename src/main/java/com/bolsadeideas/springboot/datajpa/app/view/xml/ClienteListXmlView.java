package com.bolsadeideas.springboot.datajpa.app.view.xml;

import com.bolsadeideas.springboot.datajpa.app.models.entity.Cliente;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.xml.MarshallingView;

import java.util.Map;

@Component("listar.xml")
public class ClienteListXmlView extends MarshallingView {

    @Autowired
    public ClienteListXmlView(Jaxb2Marshaller marshaller) {
        super(marshaller);
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        model.remove("titulo");
        Page<Cliente> clientes = (Page<Cliente>) model.get("clientes");
        model.remove("clientes");
        model.remove("page");
        model.put("clienteList", new ClienteList(clientes.getContent()));

        super.renderMergedOutputModel(model, request, response);
    }

}
