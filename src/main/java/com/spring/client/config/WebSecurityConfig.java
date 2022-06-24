package com.spring.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class WebSecurityConfig {

    private static final String[] WHITE_LIST_URLS = {
            "/hello",
            "/register",
            "/verifyRegistration*",
            "/resendVerifyToken*"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // disable cors and csrf
                .cors().and().csrf().disable()

                // berikan authorize request untuk semua url WHITE_LIST_URL
                .authorizeHttpRequests().antMatchers(WHITE_LIST_URLS).permitAll()

                // untuk url /api/** harus terautentikasi (username dan password)
                .antMatchers("/api/**").authenticated()
                .and()
                //  halaman login
                .oauth2Login(oauth2login -> oauth2login.loginPage("/oauth2/authorization/api-client-oidc"))

                // client untuk oauth
                .oauth2Client(Customizer.withDefaults())
        ;

        return http.build();
    }
}
