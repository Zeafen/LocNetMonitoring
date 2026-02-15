package com.zeafen.LocNetMonitoring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfiguration {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @Bean
    public PasswordEncoder passwordEncoder() {
        return passwordEncoder;
    }
}
