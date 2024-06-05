package com.pnt.shopapp.configurations;

import com.pnt.shopapp.filters.JwtTokenFilter;
import com.pnt.shopapp.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.http.HttpMethod.*;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity

public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(buildPath("users/register"), buildPath("users/login")).permitAll()
                        .requestMatchers(GET, buildPath("categories**")).hasAnyRole(Role.USER, Role.ADMIN)
                        .requestMatchers(POST, buildPath("categories/**")).hasRole(Role.ADMIN)
                        .requestMatchers(PUT, buildPath("categories/**")).hasRole(Role.ADMIN)
                        .requestMatchers(DELETE, buildPath("categories/**")).hasRole(Role.ADMIN)
                        .requestMatchers(GET, buildPath("products**")).hasAnyRole(Role.USER, Role.ADMIN)
                        .requestMatchers(POST, buildPath("products/**")).hasRole(Role.ADMIN)
                        .requestMatchers(PUT, buildPath("products/**")).hasRole(Role.ADMIN)
                        .requestMatchers(DELETE, buildPath("products/**")).hasRole(Role.ADMIN)
                        .requestMatchers(POST, buildPath("orders/**")).hasAnyRole(Role.USER)
                        .requestMatchers(GET, buildPath("orders/**")).hasAnyRole(Role.USER, Role.ADMIN)
                        .requestMatchers(PUT, buildPath("orders/**")).hasRole(Role.ADMIN)
                        .requestMatchers(DELETE, buildPath("orders/**")).hasRole(Role.ADMIN)
                        .requestMatchers(POST, buildPath("order_details/**")).hasAnyRole(Role.USER)
                        .requestMatchers(GET, buildPath("order_details/**")).hasAnyRole(Role.USER, Role.ADMIN)
                        .requestMatchers(PUT, buildPath("order_details/**")).hasRole(Role.ADMIN)
                        .requestMatchers(DELETE, buildPath("order_details/**")).hasRole(Role.ADMIN)
                        .anyRequest().authenticated()
                );
        httpSecurity.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration configuration=new CorsConfiguration();
                configuration.setAllowedOrigins(java.util.List.of("*"));
                configuration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE","OPTIONS"));
                configuration.setAllowedHeaders(java.util.List.of("authorization", "content-type","x-auth-token"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                httpSecurityCorsConfigurer.configurationSource(source);
                };
        });
        return httpSecurity.build();
    }

    private String buildPath(String endpoint) {
        return String.format("%s/%s", apiPrefix, endpoint);
    }
}
