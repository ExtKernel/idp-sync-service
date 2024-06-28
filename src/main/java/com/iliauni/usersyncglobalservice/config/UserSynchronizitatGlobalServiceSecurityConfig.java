package com.iliauni.usersyncglobalservice.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class UserSynchronizitatGlobalServiceSecurityConfig {

    @Value("${principalRoleName}")
    private String principalRoleName;

    @Component
    static class KeycloakAuthoritiesConverter implements Converter<Jwt, List<SimpleGrantedAuthority>> {
        @Override
        @SuppressWarnings({ "rawtypes", "unchecked" })
        public List<SimpleGrantedAuthority> convert(Jwt jwt) {
            final var realmAccess = (Map<String, Object>) jwt.getClaims().getOrDefault("realm_access", Map.of());
            final var roles = (List<String>) realmAccess.getOrDefault("roles", List.of());

            return roles.stream().map(SimpleGrantedAuthority::new).toList();
        }
    }

    @Component
    @AllArgsConstructor
    static class KeycloakAuthenticationConverter implements Converter<Jwt, JwtAuthenticationToken> {
        private final KeycloakAuthoritiesConverter authoritiesConverter;

        @Override
        public JwtAuthenticationToken convert(Jwt jwt) {
            return new JwtAuthenticationToken(jwt, authoritiesConverter.convert(jwt), jwt.getClaimAsString(StandardClaimNames.PREFERRED_USERNAME));
        }

    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            KeycloakAuthenticationConverter authenticationConverter
    ) throws Exception {
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(authenticationConverter)));
        http.cors(AbstractHttpConfigurer::disable);
        http.sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(AbstractHttpConfigurer::disable);

        // Return 401 (unauthorized) instead of 302 (redirect to login) when
        // authorization is missing or invalid
        http.exceptionHandling(entry -> entry.authenticationEntryPoint((request, response, authException) -> {
            response.addHeader(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"Restricted Content\"");
            response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }));

        http.authorizeHttpRequests(requests -> requests
                .requestMatchers("/secured/**").hasAuthority(principalRoleName)
                .anyRequest().permitAll());

        return http.build();
    }
}
