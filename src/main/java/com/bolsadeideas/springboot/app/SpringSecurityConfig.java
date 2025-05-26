package com.bolsadeideas.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain; 
import com.bolsadeideas.springboot.app.auth.handler.LoginSuccessHandler;
import com.bolsadeideas.springboot.app.models.service.JpaUserDetailsService;

@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SpringSecurityConfig {
	 
    @Autowired
    private LoginSuccessHandler successHandler;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
  
    @Autowired
    private JpaUserDetailsService userDetailService;

    @Autowired
    public void userDetailsService(AuthenticationManagerBuilder build) throws Exception {
       build.userDetailsService(userDetailService)
       .passwordEncoder(passwordEncoder);
    }
  
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { 
     return http
             .authorizeHttpRequests(
                 (authz) -> authz
                     .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/listar**", "/locale", "/api/clientes/**")
                     .permitAll()
                     .anyRequest().authenticated()
             )
             .formLogin(login -> login
                 .successHandler(successHandler)
                 .loginPage("/login")
                 .permitAll())
             .logout(logout -> logout.permitAll())
             .exceptionHandling(ex -> ex.accessDeniedPage("/error_404"))
             .build();
     }
}
