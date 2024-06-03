package com.app_api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.app_api.resource.enums.UserRoleEnum.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {
            "/api/v1/files/**",
            "/api/v1/auth/**",
            "/api/v1/hello",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .requestMatchers("/api/v1/user/**").hasAnyRole(admin.name(), seller.name(), whser.name(), sellerEmp.name(), whserEmp.name())
                                .requestMatchers("/api/v1/dashboard/**").hasAnyRole(seller.name(), whser.name(), sellerEmp.name(), whserEmp.name())
                                .requestMatchers("/api/v1/product/**").hasAnyRole(seller.name(), whser.name(), sellerEmp.name(), whserEmp.name())
                                .requestMatchers("/api/v1/country/**").hasAnyRole(seller.name(), whser.name(), sellerEmp.name(), whserEmp.name())
                                .requestMatchers("/api/v1/order/**").hasAnyRole(seller.name(), whser.name(), sellerEmp.name(), whserEmp.name())
                                .requestMatchers("/api/v1/shipment/**").hasAnyRole(seller.name(), whser.name(), sellerEmp.name(), whserEmp.name())
                                .requestMatchers("/api/v1/palet/**").hasAnyRole(seller.name(), whser.name(), sellerEmp.name(), whserEmp.name())
                                .requestMatchers("/api/v1/invoice/**").hasAnyRole(seller.name(), whser.name())
                                .anyRequest()
                                .authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()));
        return http.build();
    }
}
