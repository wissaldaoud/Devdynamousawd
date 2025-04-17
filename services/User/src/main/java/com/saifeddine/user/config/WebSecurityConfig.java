package com.saifeddine.user.config;

import com.saifeddine.user.filter.JwtRequestFilter;
import com.saifeddine.user.service.SecurityUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final SecurityUserService securityUserService;

    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    public WebSecurityConfig(JwtRequestFilter jwtRequestFilter,
                             SecurityUserService securityUserService) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.securityUserService = securityUserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints for authentication
                        .requestMatchers("/auth/login", "/auth/register").permitAll()
                        .requestMatchers("/users/createUser").permitAll()
                        .requestMatchers("/users/getAllUsers").permitAll()

                        // Swagger/OpenAPI documentation
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/webjars/**",
                                "/swagger-resources/**"
                        ).permitAll()
                        // User Controller endpoints (authenticated)
                        .requestMatchers(HttpMethod.GET, "/users/{id}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/users/role/{role}").authenticated()
                        .requestMatchers(HttpMethod.POST, "/users/{userId}/request-role").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/users/{userId}/profile").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/users/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/users/{id}").authenticated()
                        // User operations
                        .requestMatchers(HttpMethod.GET, "/users/{id}").authenticated()
                        .requestMatchers(HttpMethod.POST, "/users/{userId}/request-role").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/users/{userId}/profile").authenticated()

                        // Profile Controller endpoints (authenticated)
                        .requestMatchers("/profile/**").authenticated()

                        // Admin Controller endpoints (admin only)
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")

                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // Add JWT filter
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // Use the value from application properties
        config.setAllowedOrigins(List.of(allowedOrigins.split(",")));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept",
                "Origin",
                "X-Requested-With",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
        ));
        config.setExposedHeaders(Arrays.asList("Authorization", "Content-Disposition"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(securityUserService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}