package com.bolsadeideas.springboot.datajpa.app.config;

import com.bolsadeideas.springboot.datajpa.app.auth.handler.LoginSuccesHandler;
import com.bolsadeideas.springboot.datajpa.app.service.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SpringSecurityConfig {

    @Autowired
    private LoginSuccesHandler succesHandler;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JpaUserDetailsService userDetailService;

//    @Autowired
//    private DataSource dataSource;

    /*
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        PasswordEncoder encoder = this.passwordEncoder;

        //PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
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
     */

    /*
    @Bean
    AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder)
                .usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username=?")
                .authoritiesByUsernameQuery("SELECT u.username, a.authority FROM authorities a INNER JOIN users u on (a.user_id=u.id) WHERE u.username=?")
                .and().build();
    }
    */

    @Bean
    public UserDetailsService userDetailsService(AuthenticationManagerBuilder build) throws Exception {
        build.userDetailsService(userDetailService)
                .passwordEncoder(passwordEncoder);
        return build.getDefaultUserDetailsService();
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
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/listar", "/locale").permitAll()
                        //.requestMatchers("/ver/**").hasAnyRole("USER")
                        //.requestMatchers("/uploads/**").hasAnyRole("USER")
                        //.requestMatchers("/form/**").hasAnyRole("ADMIN")
                        //.requestMatchers("/eliminar/**").hasAnyRole("ADMIN")
                        //.requestMatchers("/factura/**").hasAnyRole("ADMIN")
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
