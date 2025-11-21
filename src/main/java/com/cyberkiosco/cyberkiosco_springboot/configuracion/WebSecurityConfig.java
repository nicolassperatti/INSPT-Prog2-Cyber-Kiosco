package com.cyberkiosco.cyberkiosco_springboot.configuracion;

import com.cyberkiosco.cyberkiosco_springboot.security.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    // Bean para encriptar contraseñas. Es CRÍTICO no guardar contraseñas en texto plano.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // El proveedor que conecta tu servicio de base de datos con la seguridad
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Configuración de autorización de rutas
            .authorizeHttpRequests((requests) -> requests
                // Rutas públicas (recursos estáticos, login, home pública)
                .requestMatchers("/css/**", "/images/**", "/js/**", "/usuario/login", "/usuario/registro","/contacto").permitAll()
                .requestMatchers("/").permitAll()
                // Rutas que requieren rol específico (ejemplo)
                // .requestMatchers("/admin/**").hasAuthority("Administrador") 
                // Cualquier otra ruta requiere estar autenticado
                .anyRequest().authenticated()
            )
            // Configuración del formulario de Login
            .formLogin((form) -> form
                .loginPage("/usuario/login") // Tu URL actual para mostrar el HTML
                .loginProcessingUrl("/login_process") // URL interna que procesa el POST (Spring lo hace solo)
                .usernameParameter("mail") // El 'name' del input en tu HTML es 'mail'
                .passwordParameter("password") // El 'name' del input en tu HTML es 'password'
                .defaultSuccessUrl("/productos", true) // A donde ir si el login es exitoso
                .failureUrl("/usuario/login?error=true") // A donde ir si falla
                .permitAll()
            )
            // Configuración del Logout
            .logout((logout) -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/usuario/login?logout")
                .permitAll()
            );

        return http.build();
    }
}