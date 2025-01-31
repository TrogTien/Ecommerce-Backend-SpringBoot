package com.example.shopapp.configs;

import com.example.shopapp.filters.JwtTokenFilter;
import com.example.shopapp.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;

    @Value("${api.base.path}")
    private String apiBasePath;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http    .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(
                            apiBasePath + "/users/register",
                            apiBasePath + "/users/login"
                        ).permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers(HttpMethod.POST,  apiBasePath + "/products/uploads/**").hasRole(Role.ADMIN)

                        .requestMatchers(HttpMethod.GET, apiBasePath + "/categories/**").permitAll()
                        .requestMatchers(HttpMethod.POST, apiBasePath + "/categories").hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.PUT, apiBasePath + "/categories/**").hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.DELETE, apiBasePath + "/categories/**").hasRole(Role.ADMIN)

                        .requestMatchers(HttpMethod.GET, apiBasePath + "/products/**").permitAll()
                        .requestMatchers(HttpMethod.POST, apiBasePath + "/products/**").hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.PUT, apiBasePath + "/products/**").hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.DELETE, apiBasePath + "/products/**").hasRole(Role.ADMIN)

                        .requestMatchers(HttpMethod.GET, apiBasePath + "/orders/**").permitAll()
                        .requestMatchers(HttpMethod.POST, apiBasePath + "/orders/**").hasAnyRole(Role.USER, Role.ADMIN)
                        .requestMatchers(HttpMethod.PUT, apiBasePath + "/orders/**").hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.DELETE, apiBasePath + "/orders/**").hasRole(Role.ADMIN)

                        .requestMatchers(HttpMethod.GET, apiBasePath + "/order_details/**").hasAnyRole(Role.ADMIN, Role.USER)
                        .requestMatchers(HttpMethod.POST, apiBasePath + "/order_details/**").hasRole(Role.USER)
                        .requestMatchers(HttpMethod.PUT, apiBasePath + "/order_details/**").hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.DELETE, apiBasePath + "/order_details/**").hasRole(Role.ADMIN)

                        .anyRequest().authenticated())
        ;

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "x-auth-token"));
        configuration.setExposedHeaders(List.of("x-auth-token"));
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
