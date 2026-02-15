package com.zeafen.LocNetMonitoring.config;

import com.zeafen.LocNetMonitoring.domain.stub.UserRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LocNetMonitoringAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = "";
        if (authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.equals(UserRole.ADMIN)))
            targetUrl = "/users";

        new DefaultRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
