package com.raf.rs.webDomaci3.config;

import com.raf.rs.webDomaci3.domain.enums.Action;
import com.raf.rs.webDomaci3.exception.MyAccessDeniedHandler;
import com.raf.rs.webDomaci3.filters.CustomAuthenticationFilter;
import com.raf.rs.webDomaci3.filters.CustomAuthorizationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ProjectSecurityConfiguration {
    @Value("${jwt.token.secret}")
    private String jwtSecretKey;

    @Bean
    public WebMvcConfigurer corsConfigurer()
    {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/login").allowedMethods("POST", "OPTIONS").allowedOrigins("http://localhost:4200");
            }
        };
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter((authenticationManager(http.getSharedObject(AuthenticationConfiguration.class))), jwtSecretKey);
        customAuthenticationFilter.setFilterProcessesUrl("/login");
        return http.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and().authorizeHttpRequests()
                .requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
                .anyRequest().authenticated()
                .and().addFilter(customAuthenticationFilter)
                .addFilterBefore(new CustomAuthorizationFilter(jwtSecretKey), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().accessDeniedHandler(new MyAccessDeniedHandler()).and()
                .httpBasic(Customizer.withDefaults()).build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(10);
        threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
    }
}
