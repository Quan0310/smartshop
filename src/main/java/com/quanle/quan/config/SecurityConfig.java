package com.quanle.quan.config;

import com.quanle.quan.exception.BadRequestException;
import com.quanle.quan.security.jwt.AuthEntryPointJwt;
import com.quanle.quan.security.jwt.TokenAuthenticationFilter;
import com.quanle.quan.security.oauth2.CustomOAuth2UserService;
import com.quanle.quan.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.quanle.quan.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.quanle.quan.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.quanle.quan.security.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@Configuration
@EnableWebMvc
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.cors.allowedOrigins}")
    private String[] allowedOrigins;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Autowired
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter();
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(new AuthEntryPointJwt()))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/api/test/**").permitAll().anyRequest().permitAll())
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authorization -> authorization.baseUri("/oauth2/authorize")
                                .authorizationRequestRepository(cookieAuthorizationRequestRepository()))
                        .redirectionEndpoint(redirection -> redirection.baseUri("/oauth2/callback/*"))
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService))
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler(oAuth2AuthenticationFailureHandler));
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
