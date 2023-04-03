package com.bolsadeideas.springboot.datajpa.app.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import java.io.IOException;
import java.util.Locale;

@Component
public class LoginSuccesHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Locale locale = localeResolver.resolveLocale(request);
        String mensaje = String.format(messageSource.getMessage("text.login.success", null, locale), authentication.getName());

        SessionFlashMapManager sessionFlashMapManager = new SessionFlashMapManager();
        FlashMap flashMap = new FlashMap();
        flashMap.put("success", mensaje);
        sessionFlashMapManager.saveOutputFlashMap(flashMap, request, response);

        if(authentication != null){
            logger.info(mensaje);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
