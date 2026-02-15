package com.zeafen.LocNetMonitoring.config;

import com.zeafen.LocNetMonitoring.domain.stub.StubUser;
import com.zeafen.LocNetMonitoring.domain.stub.UserRole;
import com.zeafen.LocNetMonitoring.domain.stub.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Autowired
    private LocNetMonitoringAuthenticationSuccessHandler successHandler;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsersService users;

    /**
     * Configures global authentication, by setting up user session creating
     *
     * @param auth authentication builder
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        auth.userDetailsService(login -> {
            StubUser user = users.getUserByName(login);
            if (user == null) {
                throw new UsernameNotFoundException("Такой пользователь не существует");
            }

            return new User(
                    user.getName(),
                    user.getPassword(),
                    Collections.singleton(UserRole.values()[user.getRole()])
            );
        }).passwordEncoder(passwordEncoder);
    }

    /**
     * Configures authentication routing
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    authorizationManagerRequestMatcherRegistry.
                            requestMatchers("/login")
                            .permitAll();
                    authorizationManagerRequestMatcherRegistry.anyRequest().authenticated();
                })
                .formLogin(httpSecurityFormLoginConfigurer -> {
                    httpSecurityFormLoginConfigurer.loginPage("/login");
                    httpSecurityFormLoginConfigurer.successHandler(successHandler);
                    httpSecurityFormLoginConfigurer.permitAll();
                })
                .logout(httpSecurityLogoutConfigurer -> {
                    httpSecurityLogoutConfigurer
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/login")
                            .permitAll();
                });

        return http.build();
    }
}
