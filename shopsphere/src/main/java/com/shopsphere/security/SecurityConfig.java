package com.shopsphere.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter =
                jwtAuthenticationFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

                // =================================================
                // CSRF
                // =================================================

                .csrf(csrf ->
                        csrf.disable()
                )


                // =================================================
                // CORS
                // =================================================

                .cors(cors -> {
                })


                // =================================================
                // SESSION
                // JWT IS STATELESS
                // =================================================

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )


                // =================================================
                // AUTHORIZATION
                // =================================================

                .authorizeHttpRequests(auth -> auth


                        // =========================================
                        // REGISTER
                        // PUBLIC
                        // =========================================

                        .requestMatchers(
                                "/api/users/register"
                        ).permitAll()


                        // =========================================
                        // LOGIN
                        // PUBLIC
                        // =========================================

                        .requestMatchers(
                                "/api/users/login"
                        ).permitAll()


                        // =========================================
                        // GET PRODUCTS
                        // PUBLIC
                        // =========================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/products/**"
                        ).permitAll()


                        // =========================================
                        // CREATE PRODUCT
                        // ADMIN
                        // =========================================

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/products/**"
                        ).hasRole("ADMIN")


                        // =========================================
                        // UPDATE PRODUCT
                        // ADMIN
                        // =========================================

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/products/**"
                        ).hasRole("ADMIN")


                        // =========================================
                        // DELETE PRODUCT
                        // ADMIN
                        // =========================================

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/products/**"
                        ).hasRole("ADMIN")


                        // =========================================
                        // GET ALL ORDERS
                        // ADMIN
                        // =========================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/orders"
                        ).hasRole("ADMIN")


                        // =========================================
                        // UPDATE ORDER STATUS
                        // ADMIN
                        // =========================================

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/orders/*/status"
                        ).hasRole("ADMIN")


                        // =========================================
                        // ADMIN ORDER DETAILS
                        // ADMIN
                        // =========================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/orders/admin/**"
                        ).hasRole("ADMIN")


                        // =========================================
                        // OTHER ORDER APIs
                        // LOGGED-IN USERS
                        // =========================================

                        .requestMatchers(
                                "/api/orders/**"
                        ).authenticated()


                        // =========================================
                        // EVERYTHING ELSE
                        // AUTHENTICATED
                        // =========================================

                        .anyRequest().authenticated()
                )


                // =================================================
                // JWT FILTER
                // =================================================

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );


        return http.build();
    }
}