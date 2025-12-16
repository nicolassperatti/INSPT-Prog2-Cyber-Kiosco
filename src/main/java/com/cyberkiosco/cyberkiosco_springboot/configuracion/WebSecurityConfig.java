package com.cyberkiosco.cyberkiosco_springboot.configuracion;

import com.cyberkiosco.cyberkiosco_springboot.security.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
//import org.springframework.security.config.Customizer;
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
    @Autowired ManejadorDeLoginExitoso manejadorDeLoginExitoso;

    // Bean para encriptar contraseñas. Es CRÍTICO no guardar contraseñas en texto plano.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // El proveedor que conecta el servicio de base de datos con la seguridad
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
                // Rutas públicas
                .requestMatchers("/css/**", "/images/**", "/js/**", "/usuario/login","/logout").permitAll()
                .requestMatchers("/").permitAll()
                //autorizacion para usuarios finales (clientes)
                //primero configuramos la autorizacion a productos
                .requestMatchers("/productos","/producto_detalle/**","/producto_por_marca","/producto_por_categoria").hasAuthority("Usuario")
                //luego la de carritos
                .requestMatchers("/carrito","/carrito/agregar","/carrito/sacar","/carrito/comprar").hasAuthority("Usuario")
                .requestMatchers("/carrito/lista_compras","/carrito/detalle_compra/**").hasAuthority("Usuario")
                //por ultimo contactos
                .requestMatchers("/contactos").hasAuthority("Usuario")

                //autorizacion para usuarios admin (administradores)
                //quien puede ver los menus de admin
                .requestMatchers("/admin","/admin/marcas","/admin/productos","/admin/categorias").hasAnyAuthority("Administrador","Lector") 
                //quien puede editar las marcas, categorias y productos
                .requestMatchers("/admin/marca/editar/**","/admin/categoria/editar/**","/admin/producto/editar/**").hasAuthority("Administrador")
                //quien puede crear las marcas, categorias y productos
                .requestMatchers("/admin/marca/crear/**","/admin/categoria/crear/**","/admin/producto/crear/**").hasAuthority("Administrador")
                //quien puede cambiar el estado de las marcas, categorias y productos
                .requestMatchers("/admin/marca/cambiar_estado/**","/admin/categoria/cambiar_estado/**","/admin/producto/cambiar_estado/**").hasAuthority("Administrador")
                // Cualquier otra ruta requiere estar autenticado
                .anyRequest().authenticated()
            )
            // Configuración del formulario de Login
            .formLogin((form) -> form
                .loginPage("/usuario/login") // URL actual para mostrar el HTML
                .loginProcessingUrl("/login_process") // URL interna que procesa el POST (Spring lo hace solo)
                .usernameParameter("mail") // El 'name' del input en el HTML es 'mail'
                .passwordParameter("password") // El 'name' del input en el HTML es 'password'
                .successHandler(manejadorDeLoginExitoso)
                .failureUrl("/usuario/login?error=true") // A donde ir si falla
                .permitAll()
            )
            // Configuración del Logout
            .logout((logout) -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/usuario/login?logout")
                .permitAll()
            );

            /*
            http.logout(new Customizer<LogoutConfigurer<HttpSecurity>>() {
                @Override
                public void customize(LogoutConfigurer<HttpSecurity> logout) {
                    logout.logoutUrl("/logout");
                    logout.logoutSuccessUrl("/usuario/login?logout");
                    logout.permitAll();
                }
            });
            */

        return http.build();
    }
}