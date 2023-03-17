package com.bolsadeideas.springboot.datajpa.app.controllers;

import com.bolsadeideas.springboot.datajpa.app.models.entity.Cliente;
import com.bolsadeideas.springboot.datajpa.app.service.IClienteService;
import com.bolsadeideas.springboot.datajpa.app.service.UploadFileServiceImpl;
import com.bolsadeideas.springboot.datajpa.app.util.paginator.PageRender;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Map;


@Controller
@SessionAttributes("cliente")
public class ClienteController {

    // Ahora se utiliza un service para tener la facilidad de utilizar muchos mas DAO
//    @Autowired
//    private IClienteDao clienteDao;

    @Autowired
    private IClienteService clienteService;

//    @Autowired
//    ClienteService clienteService;

    @Autowired
    UploadFileServiceImpl uploadFileService;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping(value = {"/", "/listar"})
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
                         Authentication authentication) {

        if(authentication != null){
            log.info("Método:listar: Usuario autenticado. Username: ".concat(authentication.getName()));
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null){
            log.info("Método:listar: Usuario autenticado (Forma estática). Username: ".concat(auth.getName()));
        }

        if(hasRole("ROLE_ADMIN")){
            log.info("Usuario: ".concat(auth.getName()).concat(" CON acceso de administrador"));
        }else{
            log.info("Usuario: ".concat(auth.getName()).concat(" SIN acceso de administrador"));
        }

        Pageable pageable = PageRequest.of(page, 5);
        Page<Cliente> clientePage = clienteService.findAll(pageable);
        PageRender<Cliente> pageRender = new PageRender<>("/listar", clientePage);

        model.addAttribute("titulo", "Listado de clientes");
        //model.addAttribute("clientes", clienteService.findAll());
        model.addAttribute("clientes", clientePage);
        model.addAttribute("page", pageRender);
        return "listar";
    }

    @GetMapping(value = "/uploads/{filename:.+}")
    public ResponseEntity<Resource> verFoto(@PathVariable("filename") String filename) {

        Resource recurso = null;
        try {
            recurso = uploadFileService.load(filename);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
                .body(recurso);
    }

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes redirectAttributes) {
        //Cliente cliente = clienteService.findOne(id);
        Cliente cliente = clienteService.fetchByIdWithFacturas(id);
        if (cliente == null) {
            redirectAttributes.addFlashAttribute("error", "El cliente no existe en la base de datos");
            return "redirect:/listar";
        }
        model.put("cliente", cliente);
        model.put("titulo", "Detalle del cliente: " + cliente.getNombre());

        return "ver";
    }

    @GetMapping(value = "/form")
    public String crear(Map<String, Object> model) {
        Cliente cliente = new Cliente();
        model.put("cliente", cliente);
        model.put("titulo", "Formulario de Cliente");
        return "form";
    }

    @GetMapping(value = "/form/{id}")
    public String crear(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Cliente cliente = new Cliente();
        if (id < 0) {
            redirectAttributes.addFlashAttribute("danger", "Id incorrecto");
            return "redirect:/listar";
        }

        cliente = clienteService.findOne(id);
        if (cliente == null) {
            redirectAttributes.addFlashAttribute("danger", "El Id del cliente no existe");
            return "redirect:/listar";
        }

        model.addAttribute("cliente", cliente);
        model.addAttribute("titulo", "Editar Cliente");
        return "form";
    }

    @PostMapping("/form")
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model, @RequestParam("fotoUpload") MultipartFile foto, RedirectAttributes redirectAttributes, SessionStatus status) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de Cliente");
            return "form";
        }

        if (!foto.isEmpty()) {
            if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null && cliente.getFoto().length() > 0) {
                uploadFileService.delete(cliente.getFoto());
            }

            String uniqueFileName = null;
            try {
                uniqueFileName = uploadFileService.copy(foto);
                redirectAttributes.addFlashAttribute("info", "Has subido correctamente '" + uniqueFileName + "'");
                cliente.setFoto(uniqueFileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        clienteService.save(cliente);
        redirectAttributes.addFlashAttribute("success", "Cliente guardado con éxito");
        status.setComplete();
        return "redirect:/listar";
    }

    @GetMapping(value = "/form/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        if (id > 0) {
            Cliente cliente = clienteService.findOne(id);

            clienteService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Cliente eliminado con éxito");

            if (uploadFileService.delete(cliente.getFoto())) {
                redirectAttributes.addFlashAttribute("info", "Foto " + cliente.getFoto() + " eliminada con éxito");
            }

        }
        return "redirect:/listar";
    }

    private boolean hasRole(String role){
        SecurityContext context = SecurityContextHolder.getContext();
        if(context == null){
            return false;
        }

        Authentication auth = context.getAuthentication();
        if(auth == null){
            return false;
        }

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        for (GrantedAuthority authority: authorities){
            if(role.equals(authority.getAuthority())){
                log.info("Método: hasRole -> Usuario: ".concat(auth.getName()).concat(" ROLE: ".concat(authority.getAuthority())));
                return true;
            }
        }

        return false;
    }
}
