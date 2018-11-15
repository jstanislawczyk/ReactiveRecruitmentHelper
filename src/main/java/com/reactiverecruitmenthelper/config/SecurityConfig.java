package com.reactiverecruitmenthelper.config;

import com.reactiverecruitmenthelper.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@AllArgsConstructor
public class SecurityConfig {

    private UserRepository userRepository;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .pathMatchers("/users/**").hasAuthority("ADMIN")
                .pathMatchers(HttpMethod.POST, "/jobs/**").permitAll()
                .pathMatchers("/jobs/**").hasAuthority("RECRUITER")
                .anyExchange().permitAll()
                .and().httpBasic()
                .and().csrf().disable()
                .build();
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService() {
        return (email) -> userRepository.findByEmail(email);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
