package com.intakhab.hospitalmanagementhackonit.Config;

import com.intakhab.hospitalmanagementhackonit.Enum.UserRole;
import com.intakhab.hospitalmanagementhackonit.ServiceImpl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomSuccessHandler customSuccessHandler;
    private final CustomUserDetailsService customUserDetailsServices;

    public SecurityConfig(CustomSuccessHandler customSuccessHandler, CustomUserDetailsService customUserDetailsServices) {
        this.customSuccessHandler = customSuccessHandler;
        this.customUserDetailsServices = customUserDetailsServices;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        System.out.println(http);
        http.csrf().disable()
                .authorizeRequests()
                .requestMatchers("/send_message").permitAll()
                .requestMatchers("/forgot", "/reset_password","/static/**").permitAll()
                .requestMatchers("/register", "/home","/checkAvailability").permitAll()
                .requestMatchers("/Doctor","/about","/contact").permitAll()
                .requestMatchers("/patient/**").hasRole("PATIENT")
                .requestMatchers("/doctor/**").hasRole("DOCTOR")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/home", true).permitAll()
                .successHandler(customSuccessHandler)
                .and()
                .exceptionHandling()
                .accessDeniedPage("/error")
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/home").permitAll();

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsServices).passwordEncoder(passwordEncoder());
    }



}
