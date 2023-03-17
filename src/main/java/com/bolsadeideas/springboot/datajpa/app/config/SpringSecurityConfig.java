package com.bolsadeideas.springboot.datajpa.app.config;

import com.bolsadeideas.springboot.datajpa.app.auth.handler.LoginSuccesHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {

    @Autowired
    private LoginSuccesHandler succesHandler;

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserDetails admin = User.withUsername("admin")
                .password(encoder.encode("12345"))
                .roles("ADMIN", "USER")
                .build();
        UserDetails user = User.withUsername("jc")
                .password(encoder.encode("12345"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(succesHandler)
                        .permitAll())
                .logout().permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/error_403")
                .and()
                .authorizeHttpRequests(authorize -> authorize
                        //.requestMatchers("/resources/**", "/signup", "/about").permitAll()
                        //.requestMatchers("/admin/**").hasRole("ADMIN")
                        //.requestMatchers("/db/**").access(new WebExpressionAuthorizationManager("hasRole('ADMIN') and hasRole('DBA')"))
                        // .requestMatchers("/db/**").access(AuthorizationManagers.allOf(AuthorityAuthorizationManager.hasRole("ADMIN"), AuthorityAuthorizationManager.hasRole("DBA")))
                        //.anyRequest().denyAll()
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/listar").permitAll()
                        .requestMatchers("/ver/**").hasAnyRole("USER")
                        .requestMatchers("/uploads/**").hasAnyRole("USER")
                        .requestMatchers("/form/**").hasAnyRole("ADMIN")
                        .requestMatchers("/eliminar/**").hasAnyRole("ADMIN")
                        .requestMatchers("/factura/**").hasAnyRole("ADMIN")
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
