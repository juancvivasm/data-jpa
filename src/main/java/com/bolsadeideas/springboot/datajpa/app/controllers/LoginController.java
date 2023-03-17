package com.bolsadeideas.springboot.datajpa.app.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class LoginController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/login")
    String login(@RequestParam(value = "error", required = false) String error,
                 @RequestParam(value = "logout", required = false) String logout,
                 Model model, Principal principal, RedirectAttributes redirectAttributes) {
        if(principal != null){
            redirectAttributes.addFlashAttribute("info", "Ya ha iniciado sesión");
            return "redirect:/";
        }

        if(error != null){
            model.addAttribute("danger", "Nombre de usuario o contraseña incorrecto");
        }

        if(logout != null){
            model.addAttribute("info", "Ha cerrado sesión con éxito");
        }

        return "login";
    }
}
