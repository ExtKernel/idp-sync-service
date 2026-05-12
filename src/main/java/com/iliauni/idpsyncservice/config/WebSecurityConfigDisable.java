package com.iliauni.idpsyncservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Profile("test")
@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class WebSecurityConfigDisable {
    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            SecurityConfig.KeycloakAuthenticationConverter authenticationConverter
    ) throws Exception {
        http.oauth2ResourceServer(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);
        http.sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(AbstractHttpConfigurer::disable);
        http.exceptionHandling(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(requests -> requests
                .anyRequest().permitAll());

        return http.build();
    }
}
