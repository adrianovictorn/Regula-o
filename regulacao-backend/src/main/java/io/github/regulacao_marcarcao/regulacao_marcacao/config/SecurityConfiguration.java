package io.github.regulacao_marcarcao.regulacao_marcacao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
        
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> {
                auth.requestMatchers("/login.html", "/js/**","/css/**","/images/**,/api/**" ).permitAll();
                auth.requestMatchers("/api/**").permitAll();
                auth.anyRequest().authenticated();
            })
            .formLogin(login -> login
            .loginPage("/login").permitAll()
            .defaultSuccessUrl("/index", true)
            )
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }


    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder){
        UserDetails userDetails = org.springframework.security.core.userdetails.User
        .builder()
        .username("isabela")
        .password(passwordEncoder.encode("isabela123"))
        .roles("ADMIN")
        .build();
        return new InMemoryUserDetailsManager(userDetails);
    } 
}
