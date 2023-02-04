package com.bolsadeideas.springboot.datajpa.app.controllers;

import com.bolsadeideas.springboot.datajpa.app.models.entity.Cliente;
import com.bolsadeideas.springboot.datajpa.app.models.entity.Factura;
import com.bolsadeideas.springboot.datajpa.app.models.entity.ItemFactura;
import com.bolsadeideas.springboot.datajpa.app.models.entity.Producto;
import com.bolsadeideas.springboot.datajpa.app.service.IClienteService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/factura")
@SessionAttributes("factura")
public class FacturaController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private IClienteService clienteService;

    @GetMapping(value = "/form/{clienteId}")
    public String crear(@PathVariable(value = "clienteId") Long clienteId, Model model, RedirectAttributes redirectAttributes) {
        Cliente cliente = clienteService.findOne(clienteId);
        if (cliente == null) {
            redirectAttributes.addFlashAttribute("error", "El cliente no existe en la base de datos");
            return "redirect:/listar";
        }
        Factura factura = new Factura();
        factura.setCliente(cliente);

        model.addAttribute("factura", factura);
        model.addAttribute("titulo", "Crear Factura");

        return "factura/form";
    }

    @GetMapping(value = "/cargar-productos", produces = {"application/json"})
    public @ResponseBody List<Producto> cargarProductos(@RequestParam(value = "term") String term){
        return clienteService.findByNombre(term);
    }

    @PostMapping("/form")
    public String guardar(@Valid Factura factura,
                          BindingResult result,
                          @RequestParam(value = "item_id[]", required = false) Long[] itemId,
                          @RequestParam(value = "cantidad[]", required = false) Integer[] cantidad,
                          Model model, RedirectAttributes redirectAttributes, SessionStatus status) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Crear Factura");
            return "factura/form";
        }

        if (itemId == null || itemId.length == 0) {
            model.addAttribute("titulo", "Crear Factura");
            model.addAttribute("error", "Error: La Factura debe tener detalle de productos");
            return "factura/form";
        }

        for (int i = 0; i < itemId.length; i++){
            Producto producto = clienteService.findProductoById(itemId[i]);

            ItemFactura linea = new ItemFactura();
            linea.setCantidad(cantidad[i]);
            linea.setProducto(producto);
            factura.addItemFactura(linea);

            log.info("FacturaController:guardar: ID: "+ itemId[i].toString() +" CANTIDAD: " + cantidad[i].toString());
        }

        clienteService.saveFactura(factura);
        status.setComplete();

        redirectAttributes.addFlashAttribute("success", "Factura guardada con éxito");
        return "redirect:/ver/" + factura.getCliente().getId();
    }

}

