package com.example.VirtualFridge;

import com.example.VirtualFridge.dataManagerImpl.PostgresUserManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.sql.Array;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
        throws Exception{
        http
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/api/v1.0/hello",
                        "/api/v1.0/user/register",
                        "/api/v1.0/user/all",
                        "/api/v1.0/recipe/createtable",
                        "/api/v1.0/shoppinglist/createtable"
                        ).permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        final CorsConfiguration conf = new CorsConfiguration();
        conf.setAllowedOrigins(
                Arrays.asList(
                        "http://localhost:7878",
                        "http://localhost:4200",
                        "https://lioquacht-religion.github.io"
                )
        );
        conf.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        conf.setExposedHeaders(Arrays.asList("Authorization", "content-type", "x-requested-with"));
        conf.setAllowedHeaders(Arrays.asList("Authorization", "content-type", "x-requested-with"));
        conf.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", conf);
        return source;



    }

    @Bean
    public UserDetailsService userDetailsService(
            //BCryptPasswordEncoder encoder
    ){
        return PostgresUserManager.getPostgresUserManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
