package com.example.reddit.config;

import com.example.reddit.security.JwtAuthenticationFilter;
import com.example.reddit.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    UserDetailsService userDetailService;
    UserDetailsServiceImpl userDetailsService;
    JwtAuthenticationFilter jwtAuthenticationFilter;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(@Lazy PasswordEncoder passwordEncoder, @Lazy JwtAuthenticationFilter jwtAuthenticationFilter,@Lazy UserDetailsService userDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(passwordEncoder);
        authProvider.setUserDetailsService(userDetailService);
        return new ProviderManager(authProvider);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userDetailService)
                .passwordEncoder(passwordEncoder)
                //.getUserDetailsService().loadUserByUsername("test1")
        ;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests((auth) ->
               auth.requestMatchers(
                       new AntPathRequestMatcher("/api/**")

                       ).permitAll().requestMatchers(

                               new AntPathRequestMatcher("/configuration/ui"),
                               new AntPathRequestMatcher("/swagger-resources/**"),
                               new AntPathRequestMatcher("/configuration/security"),
                               new AntPathRequestMatcher("/swagger-ui.html"),
                               new AntPathRequestMatcher("/webjars/**"),
                               new AntPathRequestMatcher("/swagger-ui/**"),
                               new AntPathRequestMatcher("/javainuse-openapi/"),
                               new AntPathRequestMatcher("/v3/api-docs/**")

                       ).permitAll()
                       .anyRequest().authenticated()
        );
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}



